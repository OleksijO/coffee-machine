package coffee_machine.service.impl;

import coffee_machine.dao.*;
import coffee_machine.model.entity.Account;
import coffee_machine.model.entity.goods.Addon;
import coffee_machine.model.entity.goods.Drink;
import coffee_machine.service.CoffeeMachineService;
import coffee_machine.service.exception.ServiceException;
import data.entity.Accounts;
import data.entity.Addons;
import data.entity.Drinks;
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

import static coffee_machine.i18n.message.key.error.ServiceErrorKey.NOT_ENOUGH_MONEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by oleksij.onysymchuk@gmail on 28.11.2016.
 */
public class CoffeeMachineServiceImplTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private DrinkDao drinkDao;
    @Mock
    private AddonDao addonDao;
    @Mock
    private AccountDao accountDao;
    @Mock
    private HistoryRecordDao historyRecordDao;
    @Mock
    private AbstractConnection connection;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<List<Drink>> drinkListCaptor;
    @Captor
    private ArgumentCaptor<List<Addon>> addonListCaptor;

    private CoffeeMachineService service;

    Map<Integer, Integer> drinkQuantitiesById = Drinks.getQuantitiesByIds();
    Map<Integer, Integer>  addonQuantitiesById =Addons.getQuantitiesByIds();


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(daoFactory.getAccountDao(connection)).thenReturn(accountDao);
        when(daoFactory.getHistoryRecordDao(connection)).thenReturn(historyRecordDao);
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

        List<Addon> addonsToBuy = new ArrayList<>();
        addonsToBuy.add(Addons.MILK.addon);
        addonsToBuy.add(Addons.SUGAR.addon);
        when(addonDao.getAllFromList(any())).thenReturn(addonsToBuy);

        service = CoffeeMachineServiceImpl.getInstance();
        CoffeeMachineServiceImpl.daoFactory = daoFactory;

    }


    @Test
    public void testPrepareDrinksForUserWithoutAddons() throws Exception {
        int drinkQuantity = 2;
        int userId = 2;
        long userAccountInitialAmount = Accounts.USER_A.account.getAmount();
        long cmAccountAmount = Accounts.COFFEE_MACHINE.account.getAmount();

        List<Drink> drinksToBuy = getTestDrinksWithoutAddons();

        setDrinksQuantity(drinksToBuy, drinkQuantity);

        long sumAmount = getSumAmount(drinksToBuy);
        service.prepareDrinksForUser(drinksToBuy, userId);

        verify(drinkDao).updateQuantityAllInList(drinkListCaptor.capture());
        // verify(addonDao).updateQuantityAllInList(addonListCaptor.capture());
        verify(accountDao, times(2)).update(accountCaptor.capture());

        List<Account> capturedAccounts = accountCaptor.getAllValues();
        Account cmAccount = capturedAccounts.get(0);
        Account userAccount = capturedAccounts.get(1);
        List<Drink> updatedDrinks = drinkListCaptor.getValue();
        //List<Addon> updatedAddons = addonListCaptor.getValue();


        // assertNull("List of addons to update shuold be null", updatedAddons);
        assertNotNull("List of drinks to update should be not null", updatedDrinks);
        updatedDrinks.forEach(drink -> {
            assertEquals("Drink quantity should decrease on " + drinkQuantity + " drink id=" + drink.getId(),
                    drinkQuantitiesById.get(drink.getId()) - drinkQuantity,
                    drink.getQuantity());
        });

        assertEquals("CM balance", cmAccountAmount + sumAmount, cmAccount.getAmount());
        assertEquals("User balance", userAccountInitialAmount - sumAmount, userAccount.getAmount());
    }

    private List<Drink> getTestDrinksWithoutAddons(){
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
            sumAmount += drink.getTotalPrice()*drink.getQuantity();
        }
        return sumAmount;
    }

    @Test
    public void testPrepareDrinksForUserWithAddons() throws Exception {
        int drinkQuantity = 4;
        int addonQuantity = 1;
        int userId = 2;
        long userAccountInitialAmount = Accounts.USER_A.account.getAmount();
        long cmAccountAmount = Accounts.COFFEE_MACHINE.account.getAmount();


        List<Drink> drinksToBuy = getTestDrinksWithAddons(addonQuantity);
        setDrinksQuantity(drinksToBuy, drinkQuantity);

        long sumAmount = getSumAmount(drinksToBuy);
        System.out.println("sumAmount = "+sumAmount);
        service.prepareDrinksForUser(drinksToBuy, userId);

        verify(drinkDao).updateQuantityAllInList(drinkListCaptor.capture());
        verify(addonDao).updateQuantityAllInList(addonListCaptor.capture());
        verify(accountDao, times(2)).update(accountCaptor.capture());

        List<Account> capturedAccounts = accountCaptor.getAllValues();
        Account cmAccount = capturedAccounts.get(0);
        Account userAccount = capturedAccounts.get(1);
        List<Drink> updatedDrinks = drinkListCaptor.getValue();
        List<Addon> updatedAddons = addonListCaptor.getValue();


        assertNotNull("List of addons to update should be not null", updatedAddons);
        assertNotNull("List of drinks to update should be not null", updatedDrinks);
        updatedDrinks.forEach(drink1 -> {
            assertEquals("Drink quantity should decrease on " + drinkQuantity + " drink id=" + drink1.getId(),
                    drinkQuantitiesById.get(drink1.getId()) - drinkQuantity,
                    drink1.getQuantity());
        });

        updatedAddons.forEach(addon -> {
            assertEquals("Addon quantity should decrease on " + addonQuantity + " addon id=" + addon.getId(),
                    addonQuantitiesById.get(addon.getId()) - addonQuantity*drinkQuantity,
                    addon.getQuantity());
        });

        assertEquals("CM balance", cmAccountAmount + sumAmount, cmAccount.getAmount());
        assertEquals("User balance", userAccountInitialAmount - sumAmount, userAccount.getAmount());
    }

    private List<Drink> getTestDrinksWithAddons(int addonQuantity){
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

    }


    @Test
    public void testPrepareDrinksForUserNotEnoughMoney() throws Exception {
        int drinkQuantity = 4;
        int userId = 2;
        int addonQuantity = 1;

        long userAccountInitialAmount = Accounts.USER_A.account.getAmount();
        long cmAccountAmount = Accounts.COFFEE_MACHINE.account.getAmount();
        Accounts.USER_A.account.setAmount(0);

        List<Drink> drinksToBuy = new ArrayList<>();
        drinksToBuy.add(Drinks.BORJOMI.drink.getBaseDrink());
        Drink drink = Drinks.ESPRESSO.drink.getBaseDrink();
        drink.getAddons().iterator().next().setQuantity(2);
        drinksToBuy.add(drink);
        drink = Drinks.MOCACCINO.drink.getBaseDrink();
        drink.getAddons().iterator().next();
        drink.getAddons().iterator().next().setQuantity(1);
        drinksToBuy.add(drink);
        setDrinksQuantity(drinksToBuy, drinkQuantity);


        long sumAmount = getSumAmount(drinksToBuy);
        try {
            service.prepareDrinksForUser(drinksToBuy, userId);
        } catch (ServiceException e){
            assertEquals(NOT_ENOUGH_MONEY, e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            fail("Here should be application exception");
        } finally {
            Accounts.USER_A.account.setAmount(userAccountInitialAmount);
        }


        verify(drinkDao, times(0)).updateQuantityAllInList(drinkListCaptor.capture());
        verify(addonDao,times(0)).updateQuantityAllInList(addonListCaptor.capture());
        verify(accountDao, times(0)).update(accountCaptor.capture());





    }


    
    
   

}