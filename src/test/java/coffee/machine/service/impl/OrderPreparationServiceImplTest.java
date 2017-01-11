package coffee.machine.service.impl;

import coffee.machine.dao.*;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.User;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.OrderPreparationService;
import coffee.machine.service.exception.ServiceException;
import data.test.entity.Accounts;
import data.test.entity.Users;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private Account coffeeMachineAccount = Accounts.COFFEE_MACHINE.getCopy();
    private User user = Users.A.user;
    private Account userAccount = Accounts.USER_A.getCopy();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ((OrderPreparationServiceImpl)service).setDaoFactory(daoFactory);
        when(daoFactory.getAccountDao(connection)).thenReturn(accountDao);
        when(daoFactory.getOrderDao(connection)).thenReturn(orderDao);
        when(daoFactory.getAddonDao(connection)).thenReturn(addonDao);
        when(daoFactory.getDrinkDao(connection)).thenReturn(drinkDao);
        when(daoFactory.getConnection()).thenReturn(connection);
        when(accountDao.getByUserId(user.getId())).thenReturn(java.util.Optional.ofNullable(userAccount));
        when(accountDao.getById(1)).thenReturn(Optional.of(coffeeMachineAccount));
        when(drinkDao.getAllByIds(any())).thenReturn(getDrinksFromDatabaseByIds());
        when(addonDao.getAllByIds(any())).thenReturn(getAddonsFromDatabaseByIds());
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
        fail("No service exception had been thrown.");
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
        try {
            service.prepareOrder(orderWithNegativeDrinkQuantity);
        } catch (ServiceException e) {
            assertEquals(QUANTITY_SHOULD_BE_NON_NEGATIVE, e.getMessageKey());
            return;
        }
        fail("No service exception had been thrown.");
    }

    @Test
    public void testPrepareOrderUpdatesOrderDataOnCorrectFilledOrder(){
        fail(); //TODO
    }

    @Test
    public void testPrepareOrderUpdatesQuantitesOfDrinksInDatabaseOnCorrectFilledOrder(){
        fail(); //TODO
    }

    @Test
    public void testPrepareOrderUpdatesQuantitesOfAddonsInDatabaseOnCorrectFilledOrder(){
        fail(); //TODO
    }

    @Test
    public void testPrepareOrderWithdrowsCreditsFromUserAccountOnCorrectFilledOrder(){
        fail(); //TODO
    }

    @Test
    public void testPrepareAddsCreditsToCoffeeMachineAccountOnCorrectFilledOrder(){
        fail(); //TODO
    }

    @Test
    public void testPrepareOrderDoNotStoresDataOnOrderWithNegativeDrinkQuantity() throws Exception {
        Order order = getOrderWithNegativeDrinkQuantity();
        try {
            service.prepareOrder(order);
        } catch (ServiceException ignored) {
        }
        checkNothingWasUpdated();
    }

    @Test
    public void testPrepareOrderThrowsExceptionOnOrderWithNegativeAddonQuantity() throws Exception {
        Order emptyOrder = getOrderWithNegativeAddonQuantity();
        try {
            service.prepareOrder(emptyOrder);
        } catch (ServiceException e) {
            assertEquals(QUANTITY_SHOULD_BE_NON_NEGATIVE, e.getMessageKey());
            return;
        }
        fail("No service exception had been thrown.");
    }

    @Test
    public void testPrepareOrderDoNotStoresDataOnOrderWithNegativeAddonQuantity() throws Exception {
        Order order = getOrderWithNegativeAddonQuantity();
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
            add(new Drink.Builder().setId(6).setQuantity(1).addAddons(getTestAddons()).build());
            add(new Drink.Builder().setId(10).setQuantity(0).addAddons(getTestAddons()).build());
            add(new Drink.Builder().setId(11).setQuantity(3).addAddons(getTestAddons()).build());
        }};
    }

    private List<Drink> getDrinksFromDatabaseByIds() {
        return new ArrayList<Drink>() {{
            add(WATER.getCopy());
            add(BORJOMI.getCopy());
            add(ESPRESSO.getCopy());
            add(AMERICANO.getCopy());
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
            add(MILK.getCopy());
            add(CREAM.getCopy());
            add(CINNAMON.getCopy());
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