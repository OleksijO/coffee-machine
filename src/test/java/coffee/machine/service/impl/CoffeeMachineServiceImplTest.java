package coffee.machine.service.impl;

import coffee.machine.dao.*;
import coffee.machine.i18n.message.key.error.ServiceErrorKey;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.CoffeeMachineService;
import coffee.machine.service.exception.ServiceException;
import data.entity.Accounts;
import data.entity.Addons;
import data.entity.Drinks;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class CoffeeMachineServiceImplTest {
    private static final String LIST_OF_ADDONS_TO_UPDATE_SHOULD_BE_NOT_NULL =
            "List of addons to update should be not null";
    private static final String LIST_OF_DRINKS_TO_UPDATE_SHOULD_BE_NOT_NULL =
            "List of drinks to update should be not null";
    private static final String ADDON_QUANTITY_SHOULD_DECREASE_FORMAT =
            "Addon quantity should decrease on %d, addon id=%d";
    private static final String DRINK_QUANTITY_SHOULD_DECREASE_FORMAT =
            "Drink quantity should decrease on %d, drink id=%d";
    private static final String HERE_SHOULD_BE_APPLICATION_EXCEPTION = "Here should be application exception";
    private static final String COFFEE_MACHINE_BALANCE_MISMATCH = "CoffeeMachine balance mismatch";
    private static final String USER_BALANCE_MISMATCH = "User balance mismatch";

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
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<List<Drink>> drinkListCaptor;
    @Captor
    private ArgumentCaptor<List<Item>> addonListCaptor;

    private CoffeeMachineService service;

    Map<Integer, Integer> drinkQuantitiesById = Drinks.getQuantitiesByIds();
    Map<Integer, Integer> addonQuantitiesById = Addons.getQuantitiesByIds();

    List<Drink> drinksToBuy;
    long sumAmount;
    long userAccountInitialAmount;
    long cmAccountAmount;
    int drinkQuantity;
    int addonQuantity;
    int userId;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(daoFactory.getAccountDao(connection)).thenReturn(accountDao);
        when(daoFactory.getOrderDao(connection)).thenReturn(orderDao);
        when(daoFactory.getAddonDao(connection)).thenReturn(addonDao);
        when(daoFactory.getDrinkDao(connection)).thenReturn(drinkDao);
        when(daoFactory.getConnection()).thenReturn(connection);

        Account coffeeMachineAccount = Accounts.COFFEE_MACHINE.account;
        Account userAccount = Accounts.USER_A.account;
        when(accountDao.getByUserId(2)).thenReturn(userAccount);
        when(accountDao.getById(1)).thenReturn(coffeeMachineAccount);

        List<Drink> baseDrinksToBuy = new ArrayList<>();
        baseDrinksToBuy.add(Drinks.BORJOMI.drink);
        baseDrinksToBuy.add(Drinks.ESPRESSO.drink);
        baseDrinksToBuy.add(Drinks.MOCACCINO.drink);
        when(drinkDao.getAllFromList(any())).thenReturn(baseDrinksToBuy);

        List<Item> addonsToBuy = new ArrayList<>();
        addonsToBuy.add(Addons.MILK.addon);
        addonsToBuy.add(Addons.SUGAR.addon);
        when(addonDao.getAllFromList(any())).thenReturn(addonsToBuy);

        service = CoffeeMachineServiceImpl.getInstance();
        CoffeeMachineServiceImpl.daoFactory = daoFactory;

        userAccountInitialAmount = Accounts.USER_A.account.getAmount();
        cmAccountAmount = Accounts.COFFEE_MACHINE.account.getAmount();
    }




    @Test
    public void testPrepareDrinksForUserDrinksToBuyWithoutAddons() throws Exception {

        prepareDataForTestDrinksWithoutAddons(2, 2);

        service.prepareDrinksForUser(drinksToBuy, userId);

        varifyDaoAccesionTimes(1, 0, 2);
        verifyTestResultsDrinksWithoutAddons();

    }

    @Test
    public void testPrepareDrinksForUserDrinksToBuyWithAddons() throws Exception {
        prepareDataForTestDrinkWithAddons(4,2,1);

        service.prepareDrinksForUser(drinksToBuy, userId);

        varifyDaoAccesionTimes(1, 1, 2);
        verifyTestResultsDrinksWithoutAddons();
        verifyTestResultsDrinksWithAddons();
    }

    @Test
    public void testPrepareDrinksForUserNotEnoughMoney() throws Exception {

        prepareDataForTestDrinkWithAddons(4,2,1);
        Accounts.USER_A.account.setAmount(0);

        try {
            service.prepareDrinksForUser(drinksToBuy, userId);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertEquals(ServiceErrorKey.NOT_ENOUGH_MONEY, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail(HERE_SHOULD_BE_APPLICATION_EXCEPTION);
        } finally {
            Accounts.USER_A.account.setAmount(userAccountInitialAmount);
        }

        varifyDaoAccesionTimes(0, 0, 0);
    }

    @Test
    public void testPrepareDrinksForUserNotEnoughDrinks() throws Exception {

        prepareDataForTestDrinkWithAddons(4,2,1);
        int memory = Drinks.ESPRESSO.drink.getQuantity();
        Drinks.ESPRESSO.drink.setQuantity(0);
        try {
            service.prepareDrinksForUser(drinksToBuy, userId);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertEquals(ServiceErrorKey.ITEM_NO_LONGER_AVAILABLE, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail(HERE_SHOULD_BE_APPLICATION_EXCEPTION);
        } finally {
            Drinks.ESPRESSO.drink.setQuantity(memory);
        }

        varifyDaoAccesionTimes(0, 0, 0);
    }

    @Test
    public void testPrepareDrinksForUserEmptyDrinks() throws Exception {
        prepareDataForTestDrinkWithAddons(4,2,1);
        drinksToBuy.clear();
        Drinks.ESPRESSO.drink.setQuantity(0);
        try {
            service.prepareDrinksForUser(drinksToBuy, userId);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertEquals(ServiceErrorKey.YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail(HERE_SHOULD_BE_APPLICATION_EXCEPTION);
        }

        varifyDaoAccesionTimes(0, 0, 0);
    }

    private void prepareDataForTestDrinksWithoutAddons(int drinkQuantity, int userId) {

        this.drinkQuantity = drinkQuantity;
        this.userId = userId;
        drinksToBuy = getTestDrinksWithoutAddons();
        setDrinksQuantity(drinksToBuy, drinkQuantity);
        sumAmount = getSumAmount(drinksToBuy);
    }

    private void prepareDataForTestDrinkWithAddons(int drinkQuantity, int userId, int addonQuantity) {
        prepareDataForTestDrinksWithoutAddons(drinkQuantity, userId);
        this.addonQuantity = addonQuantity;
        drinksToBuy = getTestDrinksWithAddons(addonQuantity);
        setDrinksQuantity(drinksToBuy, drinkQuantity);
        sumAmount = getSumAmount(drinksToBuy);
    }

    private void verifyTestResultsDrinksWithAddons() {
        List<Item> updatedAddons = addonListCaptor.getValue();
        assertNotNull(LIST_OF_ADDONS_TO_UPDATE_SHOULD_BE_NOT_NULL, updatedAddons);
        if (addonQuantity > 0) {
            updatedAddons.forEach(addon -> {
                assertEquals(String.format(ADDON_QUANTITY_SHOULD_DECREASE_FORMAT,addonQuantity, addon.getId()),
                        addonQuantitiesById.get(addon.getId()) - addonQuantity * drinkQuantity,
                        addon.getQuantity());
            });
        }
    }

    private void verifyTestResultsDrinksWithoutAddons() {
        List<Account> capturedAccounts = accountCaptor.getAllValues();
        Account cmAccount = capturedAccounts.get(0);
        Account userAccount = capturedAccounts.get(1);
        List<Drink> updatedDrinks = drinkListCaptor.getValue();

        assertNotNull(LIST_OF_DRINKS_TO_UPDATE_SHOULD_BE_NOT_NULL, updatedDrinks);
        updatedDrinks.forEach(drink1 -> {
            assertEquals(String.format(DRINK_QUANTITY_SHOULD_DECREASE_FORMAT,drinkQuantity,drink1.getId()),
                    drinkQuantitiesById.get(drink1.getId()) - drinkQuantity,
                    drink1.getQuantity());
        });

        assertEquals(COFFEE_MACHINE_BALANCE_MISMATCH, cmAccountAmount + sumAmount, cmAccount.getAmount());
        assertEquals(USER_BALANCE_MISMATCH, userAccountInitialAmount - sumAmount, userAccount.getAmount());
    }

    private void varifyDaoAccesionTimes(int drinkTimes, int addonTimes, int accountTimes) {
        verify(drinkDao, times(drinkTimes)).updateQuantityAllInList(drinkListCaptor.capture());
        verify(addonDao, times(addonTimes)).updateQuantityAllInList(addonListCaptor.capture());
        verify(accountDao, times(accountTimes)).update(accountCaptor.capture());
    }

    private List<Drink> getTestDrinksWithoutAddons() {
        List<Drink> drinks = new ArrayList<>();
        drinks.add(Drinks.BORJOMI.getCopy().getBaseDrink());
        drinks.add(Drinks.ESPRESSO.getCopy().getBaseDrink());
        drinks.add(Drinks.MOCACCINO.getCopy().getBaseDrink());
        return drinks;
    }

    private void setDrinksQuantity(List<Drink> drinks, int quantity) {
        for (Drink drink : drinks) {
            drink.setQuantity(quantity);
        }
    }

    private long getSumAmount(List<Drink> drinks) {
        long sumAmount = 0;
        for (Drink drink : drinks) {
            sumAmount += drink.getTotalPrice() * drink.getQuantity();
        }
        return sumAmount;
    }

    private List<Drink> getTestDrinksWithAddons(int addonQuantity) {
        List<Drink> drinks = new ArrayList<>();
        drinks.add(Drinks.BORJOMI.drink.getBaseDrink());
        Drink drink = Drinks.ESPRESSO.drink.getBaseDrink();
        drink.getAddons().iterator().next().setQuantity(addonQuantity);
        drinks.add(drink);
        drink = Drinks.MOCACCINO.drink.getBaseDrink();
        Iterator<Item> iterator = drink.getAddons().iterator();
        iterator.next();
        iterator.next().setQuantity(addonQuantity);
        drinks.add(drink);
        return drinks;

    }




}