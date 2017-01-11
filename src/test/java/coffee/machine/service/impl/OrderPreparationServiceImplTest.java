package coffee.machine.service.impl;

import coffee.machine.dao.*;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.User;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.OrderPreparationService;
import coffee.machine.service.exception.ServiceException;
import data.test.entity.AccountsData;
import data.test.entity.UsersData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY;
import static data.test.entity.AddonsData.*;
import static data.test.entity.DrinksData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by oleksij.onysymchuk@gmail on 11.01.2017.
 */
public class OrderPreparationServiceImplTest {
    public static final String NO_SERVICE_EXCEPTION_HAD_BEEN_THROWN = "No service exception had been thrown.";
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private DrinkDao drinkDao;
    @Mock
    private AddonDao addonDao;
    @Mock
    private AccountDao accountDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private AbstractConnection connection;

    private OrderPreparationService service = OrderPreparationServiceImpl.getInstance();

    private Account coffeeMachineAccount = AccountsData.COFFEE_MACHINE.getCopy();
    private User user = UsersData.A.user;
    private Account userAccount = AccountsData.USER_A.getCopy();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ((OrderPreparationServiceImpl) service).setDaoFactory(daoFactory);
        when(daoFactory.getAccountDao(connection)).thenReturn(accountDao);
        when(daoFactory.getOrderDao(connection)).thenReturn(orderDao);
        when(daoFactory.getAddonDao(connection)).thenReturn(addonDao);
        when(daoFactory.getDrinkDao(connection)).thenReturn(drinkDao);
        when(daoFactory.getConnection()).thenReturn(connection);
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


    @Test
    public void testPrepareOrderThrowsExceptionOnEmptyOrder() throws Exception {
        Order emptyOrder = getEmptyOrder();
        //emptyOrder = getFilledOrder();
        try {
            service.prepareOrder(emptyOrder);
        } catch (ServiceException e) {
            assertEquals(YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY, e.getMessageKey());
            return;
        }
        fail(NO_SERVICE_EXCEPTION_HAD_BEEN_THROWN);
    }

    @Test
    public void testPrepareOrderDoNotStoresDataOnEmptyOrder() throws Exception {
        Order emptyOrder = getEmptyOrder();
        // emptyOrder = getFilledOrder();
        try {
            service.prepareOrder(emptyOrder);
        } catch (ServiceException ignored) {
        }
        checkNothingWasUpdated();
    }

    private void checkNothingWasUpdated() {
        verify(accountDao, never()).update(any());
        verify(drinkDao, never()).updateQuantityAllInList(any());
        verify(addonDao, never()).updateQuantityAllInList(any());
        verify(orderDao, never()).insert(any());
    }


    @Test
    public void testPrepareOrderThrowsExceptionOnOrderWithNegativeDrinkQuantity() throws Exception {
        Order orderWithNegativeDrinkQuantity = getOrderWithNegativeDrinkQuantity();
        // orderWithNegativeDrinkQuantity= getEmptyOrder();
        try {
            service.prepareOrder(orderWithNegativeDrinkQuantity);
        } catch (ServiceException e) {
            assertEquals(QUANTITY_SHOULD_BE_NON_NEGATIVE, e.getMessageKey());
            return;
        }
        fail(NO_SERVICE_EXCEPTION_HAD_BEEN_THROWN);
    }

    @Test
    public void testPrepareOrderUpdatesOrderDataOnCorrectFilledOrder() {
        Order order = getFilledOrder();
        // order = getEmptyOrder();
        Order testOrder = getPreparedForStoreOrder();
        Order preparedOrder = service.prepareOrder(order);
        testOrder.setDate(preparedOrder.getDate());
        testOrder.calculateTotalCost();
        verify(orderDao).insert(testOrder);
    }

    @Test
    public void testPrepareOrderUpdatesQuantitesOfDrinksInDatabaseOnCorrectFilledOrder() {
        Order order = getFilledOrder();
        // order = getEmptyOrder();
        List<Drink> drinks = getDrinksReadyForUpdateQuantity();
        service.prepareOrder(order);
        verify(drinkDao).updateQuantityAllInList(drinks);
    }

    @Test
    public void testPrepareOrderUpdatesQuantitesOfAddonsInDatabaseOnCorrectFilledOrder() {
        Order order = getFilledOrder();
        // order = getEmptyOrder();
        List<Item> addons = getAddonsReadyForUpdateQuantity();
        service.prepareOrder(order);
        verify(addonDao).updateQuantityAllInList(addons);
    }

    @Test
    public void testPrepareOrderWithdrawsCreditsFromUserAccountOnCorrectFilledOrder() {
        Order order = getFilledOrder();
        // order = getEmptyOrder();
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
    public void testPrepareAddsCreditsToCoffeeMachineAccountOnCorrectFilledOrder() {
        Order order = getFilledOrder();
        // order = getEmptyOrder();
        Order testOrder = getPreparedForStoreOrder();
        testOrder.calculateTotalCost();
        long newCofeeMachineAccountAmount = coffeeMachineAccount.getAmount() + testOrder.getTotalCost();
        Account testCoffeeMachineAccount = new Account.Builder()
                .setAmount(newCofeeMachineAccountAmount)
                .setId(coffeeMachineAccount.getId())
                .build();
        service.prepareOrder(order);
        verify(accountDao).update(testCoffeeMachineAccount);
    }

    @Test
    public void testPrepareOrderDoNotStoresDataOnOrderWithNegativeDrinkQuantity() throws Exception {
        Order order = getOrderWithNegativeDrinkQuantity();
        // order = getFilledOrder();
        try {
            service.prepareOrder(order);
        } catch (ServiceException ignored) {
        }
        checkNothingWasUpdated();
    }

    @Test
    public void testPrepareOrderThrowsExceptionOnOrderWithNegativeAddonQuantity() throws Exception {
        Order order = getOrderWithNegativeAddonQuantity();
        // order = getFilledOrder();
        try {
            service.prepareOrder(order);
        } catch (ServiceException e) {
            assertEquals(QUANTITY_SHOULD_BE_NON_NEGATIVE, e.getMessageKey());
            return;
        }
        fail(NO_SERVICE_EXCEPTION_HAD_BEEN_THROWN);
    }

    @Test
    public void testPrepareOrderDoNotStoresDataOnOrderWithNegativeAddonQuantity() throws Exception {
        Order order = getOrderWithNegativeAddonQuantity();
        // order = getFilledOrder();
        try {
            service.prepareOrder(order);
        } catch (ServiceException ignored) {
        }
        checkNothingWasUpdated();
    }


    private Order getEmptyOrder() {
        return new Order.Builder()
                .setId(user.getId())
                .build();
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

    private ArrayList<Item> getAddonsForEveryDrinkInOrderToStore() {
        return new ArrayList<Item>() {{
            Item addon = SUGAR.getCopy();
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
                drink.setAddons(new ArrayList<Item>(){{
                    add(SUGAR.getCopy());
                    add(MILK.getCopy());
                    add(CREAM.getCopy());
                    add(CINNAMON.getCopy());
                    Collections.sort(this);
                }});
                drink.setQuantity(ESPRESSO.drink.getQuantity() - 2);
                add(drink);


                drink = MOCACCINO.getCopy();
                drink.setAddons(new ArrayList<Item>(){{
                    add(SUGAR.getCopy());
                    add(MILK.getCopy());
                    add(CREAM.getCopy());
                    add(CINNAMON.getCopy());
                    Collections.sort(this);
                }});
                drink.setQuantity(MOCACCINO.drink.getQuantity() - 3);
                add(drink);
            }
        };
    }

    private List<Item> getAddonsReadyForUpdateQuantity() {
        return new ArrayList<Item>() {{
            Item addon = SUGAR.getCopy();
            addon.setQuantity(SUGAR.addon.getQuantity() - 1 * (0 + 2 + 3));
            add(addon);
            addon = CREAM.getCopy();
            addon.setQuantity(CREAM.addon.getQuantity() - 2 * (0 + 2 + 3));
            add(addon);
        }};
    }

    private Order getOrderWithNegativeDrinkQuantity() {
        return new Order.Builder()
                .setUserId(user.getId())
                .setDrinks(getTestDrinksWithNegativeQuantity())
                .build();

    }

    private Order getOrderWithNegativeAddonQuantity() {
        return new Order.Builder()
                .setUserId(user.getId())
                .setDrinks(getTestDrinksWithNegativeAddonQuantity())
                .build();
    }

    private List<Drink> getTestDrinks() {
        return new ArrayList<Drink>() {{
            add(new Drink.Builder().setId(1).setQuantity(1).build());
            add(new Drink.Builder().setId(2).setQuantity(0).build());
            add(new Drink.Builder().setId(6).setQuantity(2).addAddons(getTestAddons()).build());
            add(new Drink.Builder().setId(10).setQuantity(0).addAddons(getTestAddons()).build());
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


    private List<Drink> getTestDrinksWithNegativeQuantity() {
        List<Drink> drinks = getTestDrinks();
        drinks.stream()
                .findAny()
                .orElseThrow(IllegalStateException::new)
                //.setQuantity(0);
                .setQuantity(-1);
        return drinks;
    }

    private List<Item> getTestAddons() {
        return new ArrayList<Item>() {{
            add(new Item.Builder().setId(7).setQuantity(1).build());
            add(new Item.Builder().setId(8).setQuantity(0).build());
            add(new Item.Builder().setId(9).setQuantity(2).build());
            add(new Item.Builder().setId(13).setQuantity(0).build());
        }};
    }

    private List<Item> getAddonsFromDatabaseByIds() {
        return new ArrayList<Item>() {{
            add(SUGAR.getCopy());
            add(CREAM.getCopy());
        }};
    }

    private List<Drink> getTestDrinksWithNegativeAddonQuantity() {
        List<Drink> drinks = getTestDrinks();
        drinks.stream()
                .filter((drink) -> (drink.getQuantity() > 0))
                .filter((drink) -> (drink.getAddons().size() > 0))
                .findAny()
                .orElseThrow(IllegalStateException::new)
                .getAddons().stream()
                .findAny()
                .orElseThrow(IllegalStateException::new)
                //.setQuantity(0);
                .setQuantity(-1);
        return drinks;
    }

}