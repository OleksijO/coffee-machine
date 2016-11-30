package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.DrinkDao;
import coffee_machine.model.entity.Account;
import coffee_machine.model.entity.goods.Drink;
import coffee_machine.service.DrinkService;
import data.entity.Drinks;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by oleksij.onysymchuk@gmail on 28.11.2016.
 */
public class DrinkServiceImplTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private DrinkDao drinkDao;

    @Mock
    private AbstractConnection connection;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<List<Drink>> drinkListCaptor;

    private DrinkService service;

    List<Drink> drinksToUpdate;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(daoFactory.getDrinkDao(connection)).thenReturn(drinkDao);
        when(daoFactory.getConnection()).thenReturn(connection);

        when(drinkDao.getAllFromList(any())).thenReturn(drinksToUpdate);

        service = DrinkServiceImpl.getInstance();
        DrinkServiceImpl.daoFactory = daoFactory;

    }


    @Test
    public void testRefillEmpty() throws Exception {
        service.refill(null);
        verify(daoFactory, times(0)).getDrinkDao(connection);
        service.refill(new HashMap<>());
        verify(daoFactory, times(0)).getDrinkDao(connection);

    }

    @Test
    public void testRefillTwo() throws Exception {
        drinksToUpdate = new ArrayList<>();
        drinksToUpdate.add(Drinks.MOCACCINO.drink);
        drinksToUpdate.add(Drinks.BORJOMI.drink);
        int valueToAdd = 10;
        int memory1 = drinksToUpdate.get(0).getQuantity();
        int memory2 = drinksToUpdate.get(1).getQuantity();

        when(drinkDao.getAllByIds(new ArrayList<Integer>() {{
            add(2);
            add(11);
        }})).thenReturn(drinksToUpdate);
        doNothing().when(drinkDao).updateQuantityAllInList(drinkListCaptor.capture());
        Map<Integer, Integer> quantitiesToAdd = new HashMap<Integer, Integer>() {{
            put(11, valueToAdd);
            put(2, valueToAdd);
        }};

        service.refill(quantitiesToAdd);

        verify(daoFactory, times(1)).getDrinkDao(connection);
        verify(drinkDao, times(1)).getAllByIds(new ArrayList<Integer>() {{
            add(2);
            add(11);
        }});
        Assert.assertEquals(
                memory1 + valueToAdd,
                drinkListCaptor.getAllValues()
                        .get(drinkListCaptor.getAllValues().size() - 1).get(0).getQuantity());

        Assert.assertEquals(
                memory2 + valueToAdd,
                drinkListCaptor.getAllValues()
                        .get(drinkListCaptor.getAllValues().size() - 1).get(1).getQuantity());

    }


    @Test
    public void testRefillOne() throws Exception {
        drinksToUpdate = new ArrayList<>();
        drinksToUpdate.add(Drinks.MOCACCINO.drink);

        int valueToAdd = 10;
        int memory1 = drinksToUpdate.get(0).getQuantity();


        when(drinkDao.getAllByIds(new ArrayList<Integer>() {{
            add(11);
        }})).thenReturn(drinksToUpdate);
        doNothing().when(drinkDao).updateQuantityAllInList(drinkListCaptor.capture());
        Map<Integer, Integer> quantitiesToAdd = new HashMap<Integer, Integer>() {{
            put(11, valueToAdd);
        }};

        service.refill(quantitiesToAdd);

        verify(daoFactory, times(1)).getDrinkDao(connection);
        verify(drinkDao, times(1)).getAllByIds(new ArrayList<Integer>() {{
            add(11);
        }});
        Assert.assertEquals(
                memory1 + valueToAdd,
                drinkListCaptor.getAllValues()
                        .get(drinkListCaptor.getAllValues().size() - 1).get(0).getQuantity());

    }

  /*  @Test
    public void testPrepareDrinksForUserWithAddons() throws Exception {
        prepareDataForTest(4,2,1);

        service.prepareDrinksForUser(drinksToBuy, userId);

        varifyDaoAccesionTimes(1, 1, 2);
        verifyTestResultsWithoutAddons();
        verifyTestResultsWithAddons();
    }

    @Test
    public void testPrepareDrinksForUserNotEnoughMoney() throws Exception {

        prepareDataForTest(4,2,1);
        Accounts.USER_A.account.setAmount(0);

        try {
            service.prepareDrinksForUser(drinksToBuy, userId);
        } catch (ServiceException e) {
            e.printStackTrace();
            assertEquals(NOT_ENOUGH_MONEY, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Here should be application exception");
        } finally {
            Accounts.USER_A.account.setAmount(userAccountInitialAmount);
        }

        varifyDaoAccesionTimes(0, 0, 0);
    }

    @Test
    public void testPrepareDrinksForUserNotEnoughDrinks() throws Exception {

        prepareDataForTest(4,2,1);
        int memory = Drinks.ESPRESSO.drink.getQuantity();
        Drinks.ESPRESSO.drink.setQuantity(0);
        try {
            service.prepareDrinksForUser(drinksToBuy, userId);
        } catch (ServiceException e) {
            e.printStackTrace();
            assertEquals(GOODS_NO_LONGER_AVAILABLE, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Here should be application exception");
        } finally {
            Drinks.ESPRESSO.drink.setQuantity(memory);
        }

        varifyDaoAccesionTimes(0, 0, 0);
    }

    @Test
    public void testPrepareDrinksForUserEmptyDrinks() throws Exception {
        prepareDataForTest(4,2,1);
        drinksToBuy.clear();
        Drinks.ESPRESSO.drink.setQuantity(0);
        try {
            service.prepareDrinksForUser(drinksToBuy, userId);
        } catch (ServiceException e) {
            e.printStackTrace();
            assertEquals(YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Here should be application exception");
        }

        varifyDaoAccesionTimes(0, 0, 0);
    }





    private void prepareDataForTestWithoutAddons(int drinkQuantity, int userId) {

        this.drinkQuantity = drinkQuantity;
        this.userId = userId;
        drinksToBuy = getTestDrinksWithoutAddons();
        setDrinksQuantity(drinksToBuy, drinkQuantity);
        sumAmount = getSumAmount(drinksToBuy);
    }

    private void prepareDataForTest(int drinkQuantity, int userId, int addonQuantity) {
        prepareDataForTestWithoutAddons(drinkQuantity, userId);
        this.addonQuantity = addonQuantity;
        drinksToBuy = getTestDrinksWithAddons(addonQuantity);
        setDrinksQuantity(drinksToBuy, drinkQuantity);
        sumAmount = getSumAmount(drinksToBuy);
    }

    private void verifyTestResultsWithAddons() {
        List<Addon> updatedAddons = addonListCaptor.getValue();
        assertNotNull("List of addons to update should be not null", updatedAddons);
        if (addonQuantity > 0) {
            updatedAddons.forEach(addon -> {
                assertEquals("Addon quantity should decrease on " + addonQuantity + " addon id=" + addon.getId(),
                        addonQuantitiesById.get(addon.getId()) - addonQuantity * drinkQuantity,
                        addon.getQuantity());
            });
        }
    }

    private void verifyTestResultsWithoutAddons() {
        List<Account> capturedAccounts = accountCaptor.getAllValues();
        Account cmAccount = capturedAccounts.get(0);
        Account userAccount = capturedAccounts.get(1);
        List<Drink> updatedDrinks = drinkListCaptor.getValue();

        assertNotNull("List of drinks to update should be not null", updatedDrinks);
        updatedDrinks.forEach(drink1 -> {
            assertEquals("Drink quantity should decrease on " + drinkQuantity + " drink id=" + drink1.getId(),
                    drinkQuantitiesById.get(drink1.getId()) - drinkQuantity,
                    drink1.getQuantity());
        });

        assertEquals("CM balance", cmAccountAmount + sumAmount, cmAccount.getAmount());
        assertEquals("User balance", userAccountInitialAmount - sumAmount, userAccount.getAmount());
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
        Iterator<Addon> iterator = drink.getAddons().iterator();
        iterator.next();
        iterator.next().setQuantity(addonQuantity);
        drinks.add(drink);
        return drinks;

    }*/


}