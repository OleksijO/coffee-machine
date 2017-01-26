package coffee.machine.service.impl;

import coffee.machine.dao.*;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.user.User;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.service.OrderPreparationService;
import coffee.machine.service.exception.ServiceException;
import data.test.entity.AccountsData;
import data.test.entity.UsersData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.ERROR_PREPARE_ORDER_PRODUCT_NO_LONGER_AVAILABLE;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.ERROR_PREPARE_ORDER_USER_HAS_NOT_ENOUGH_MONEY;
import static data.test.entity.AddonsData.*;
import static data.test.entity.DrinksData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class OrderPreparationServiceImplTest {
    private static final String NO_SERVICE_EXCEPTION_HAD_BEEN_THROWN = "No service exception had been thrown.";
    @Mock
    private DaoManagerFactory daoFactory;
    @Mock
    private DrinkDao drinkDao;
    @Mock
    private AddonDao addonDao;
    @Mock
    private AccountDao accountDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private DaoManager daoManager;

    private OrderPreparationService service = OrderPreparationServiceImpl.getInstance();

    private Account coffeeMachineAccount = AccountsData.COFFEE_MACHINE.getCopy();
    private User user = UsersData.A.user;
    private Account userAccount = AccountsData.USER_A.getCopy();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ((GenericService)service).setDaoFactory(daoFactory);
        when(daoFactory.createDaoManager()).thenReturn(daoManager);
        when(daoManager.getAccountDao()).thenReturn(accountDao);
        when(daoManager.getOrderDao()).thenReturn(orderDao);
        when(daoManager.getAddonDao()).thenReturn(addonDao);
        when(daoManager.getDrinkDao()).thenReturn(drinkDao);
        when(accountDao.getByUserId(user.getId())).thenReturn(java.util.Optional.ofNullable(userAccount));
        when(accountDao.getById(1)).thenReturn(Optional.of(coffeeMachineAccount));
        when(drinkDao.getAllByIds(getDrinkIdsSet())).thenReturn(getDrinksFromDatabaseByIds());
        when(addonDao.getAllByIds(getAddonIdsSet())).thenReturn(getAddonsFromDatabaseByIds());
    }

    private TreeSet<Integer> getDrinkIdsSet() {
        return new TreeSet<Integer>(){{add(1);add(6);add(11);}};
    }
    private TreeSet<Integer> getAddonIdsSet() {
        return new TreeSet<Integer>(){{add(7);add(9);}};
    }


    @Test(expected = NullPointerException.class)
    public void testPrepareOrderThrowsNullPointerIfOrderIsNull()  {
            service.prepareOrder(null);

    }


    @Test
    public void testPrepareOrderUpdatesOrderDataIfOrderHasProductsWithPositiveQuantity() {
        Order order = getFilledOrder();
        Order testOrder = getPreparedForStoreOrder();
        Order preparedOrder = service.prepareOrder(order);
        testOrder.setDate(preparedOrder.getDate());
        testOrder.calculateTotalCost();
        verify(orderDao).insert(testOrder);
    }

    @Test
    public void testPrepareOrderUpdatesQuantitiesOfDrinksInDatabaseIfOrderHasProductsWithPositiveQuantity() {
        Order order = getFilledOrder();
        List<Drink> drinks = getDrinksReadyForUpdateQuantity();
        service.prepareOrder(order);
        verify(drinkDao).updateQuantityAllInList(drinks);
    }

    @Test
    public void testPrepareOrderUpdatesQuantietesOfAddonsInDatabaseIfOrderHasProductsWithPositiveQuantity() {
        Order order = getFilledOrder();
        List<Product> addons = getAddonsReadyForUpdateQuantity();
        service.prepareOrder(order);
        verify(addonDao).updateQuantityAllInList(addons);
    }

    @Test
    public void testPrepareOrderWithdrawsCreditsFromUserAccountIfOrderHasProductsWithPositiveQuantity() {
        Order order = getFilledOrder();
        Order testOrder = getPreparedForStoreOrder();
        testOrder.calculateTotalCost();
        long newUserAccountAmount = userAccount.getAmount() - testOrder.getTotalCost();
        Account testUserAccount = new Account.Builder()
                .setAmount(newUserAccountAmount)
                .setId(userAccount.getId())
                .build();
        service.prepareOrder(order);
        verify(accountDao).update(testUserAccount);
    }

    @Test
    public void testPrepareAddsCreditsToCoffeeMachineAccountIfOrderHasProductsWithPositiveQuantity() {
        Order order = getFilledOrder();
        Order testOrder = getPreparedForStoreOrder();
        testOrder.calculateTotalCost();
        long newCoffeeMachineAccountAmount = coffeeMachineAccount.getAmount() + testOrder.getTotalCost();
        Account testCoffeeMachineAccount = new Account.Builder()
                .setAmount(newCoffeeMachineAccountAmount)
                .setId(coffeeMachineAccount.getId())
                .build();
        service.prepareOrder(order);
        verify(accountDao).update(testCoffeeMachineAccount);
    }

    @Test
    public void testPrepareOrderThrowsExceptionIfUserHasNotEnoughMoney()  {
        Order order = getFilledOrder();
        Account userAccountWithNoMoney = AccountsData.USER_A.getCopy();
        userAccountWithNoMoney.withdraw(userAccountWithNoMoney.getAmount());
        when(accountDao.getByUserId(user.getId())).thenReturn(java.util.Optional.of(userAccountWithNoMoney));
        try {
            service.prepareOrder(order);
        } catch (ServiceException e) {
            assertEquals(ERROR_PREPARE_ORDER_USER_HAS_NOT_ENOUGH_MONEY, e.getMessageKey());
            return;
        }
        fail(NO_SERVICE_EXCEPTION_HAD_BEEN_THROWN);
    }

    @Test
    public void testPrepareOrderThrowsExceptionIfNotEnoughDrink()  {
        Order order = getFilledOrder();
        order.getDrinks().get(1).setQuantity(100500);
        try {
            service.prepareOrder(order);
        } catch (ServiceException e) {
            assertEquals(ERROR_PREPARE_ORDER_PRODUCT_NO_LONGER_AVAILABLE, e.getMessageKey());
            assertEquals(e.getAdditionalMessage(), order.getDrinks().get(1).getName());
            return;
        }
        fail(NO_SERVICE_EXCEPTION_HAD_BEEN_THROWN);
    }

    @Test
    public void testPrepareOrderThrowsExceptionIfNotEnoughAddon()  {
        Order order = getFilledOrder();
        order.getDrinks().get(1).getAddons().get(0).setQuantity(100500);
        try {
            service.prepareOrder(order);
        } catch (ServiceException e) {
            assertEquals(ERROR_PREPARE_ORDER_PRODUCT_NO_LONGER_AVAILABLE, e.getMessageKey());
            assertEquals(e.getAdditionalMessage(), order.getDrinks().get(1).getAddons().get(0).getName());
            return;
        }
        fail(NO_SERVICE_EXCEPTION_HAD_BEEN_THROWN);
    }

    private Order getFilledOrder() {
        return new Order.Builder()
                .setUserId(user.getId())
                .setDrinks(getTestDrinks())
                .build();
    }

    private Order getPreparedForStoreOrder() {
        return new Order.Builder()
                .setUserId(user.getId())
                .setDrinks(new ArrayList<Drink>() {
                    {
                        Drink drink = WATER.getCopy();
                        drink.setAddons(new ArrayList<>());
                        drink.setQuantity(1);
                        add(drink);

                        drink = ESPRESSO.getCopy();
                        drink.setAddons(getAddonsForEveryDrinkInOrderToStore());
                        drink.setQuantity(2);
                        add(drink);


                        drink = MOCACCINO.getCopy();
                        drink.setAddons(getAddonsForEveryDrinkInOrderToStore());
                        drink.setQuantity(3);
                        add(drink);
                    }
                })
                .build();
    }

    private ArrayList<Product> getAddonsForEveryDrinkInOrderToStore() {
        return new ArrayList<Product>() {{
            Product addon = SUGAR.getCopy();
            addon.setQuantity(1);
            add(addon);
            addon = CREAM.getCopy();
            addon.setQuantity(2);
            add(addon);
            Collections.sort(this);
        }};
    }

    private List<Drink> getDrinksReadyForUpdateQuantity() {
        return new ArrayList<Drink>() {
            {
                Drink drink = WATER.getCopy();
                drink.setAddons(new ArrayList<>());
                drink.setQuantity(WATER.drink.getQuantity() - 1);
                add(drink);

                drink = ESPRESSO.getCopy();
                drink.setAddons(getAddonsForDrinkReadyForUpdateQuantity());
                drink.setQuantity(ESPRESSO.drink.getQuantity() - 2);
                add(drink);


                drink = MOCACCINO.getCopy();
                drink.setAddons(getAddonsForDrinkReadyForUpdateQuantity());
                drink.setQuantity(MOCACCINO.drink.getQuantity() - 3);
                add(drink);
            }

            private ArrayList<Product> getAddonsForDrinkReadyForUpdateQuantity() {
                return new ArrayList<Product>(){{
                    add(SUGAR.getCopy());
                    add(MILK.getCopy());
                    add(CREAM.getCopy());
                    add(CINNAMON.getCopy());
                    Collections.sort(this);
                }};
            }
        };
    }

    private List<Product> getAddonsReadyForUpdateQuantity() {
        return new ArrayList<Product>() {{
            Product addon = SUGAR.getCopy();
            addon.setQuantity(SUGAR.addon.getQuantity() - 1 * (0 + 2 + 3));
            add(addon);
            addon = CREAM.getCopy();
            addon.setQuantity(CREAM.addon.getQuantity() - 2 * (0 + 2 + 3));
            add(addon);
        }};
    }

    private List<Drink> getTestDrinks() {
        return new ArrayList<Drink>() {{
            add(new Drink.Builder().setId(1).setQuantity(1).build());
            add(new Drink.Builder().setId(6).setQuantity(2).addAddons(getTestAddons()).build());
            add(new Drink.Builder().setId(11).setQuantity(3).addAddons(getTestAddons()).build());
        }};
    }

    private List<Drink> getDrinksFromDatabaseByIds() {
        return new ArrayList<Drink>() {{
            add(WATER.getCopy());
            add(ESPRESSO.getCopy());
            add(MOCACCINO.getCopy());
        }};
    }

    private List<Product> getTestAddons() {
        return new ArrayList<Product>() {{
            add(new Product.Builder().setId(7).setQuantity(1).build());
            add(new Product.Builder().setId(9).setQuantity(2).build());
        }};
    }

    private List<Product> getAddonsFromDatabaseByIds() {
        return new ArrayList<Product>() {{
            add(SUGAR.getCopy());
            add(CREAM.getCopy());
        }};
    }
}