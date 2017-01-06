package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemReceipt;
import coffee.machine.service.CoffeeMachineRefillService;
import coffee.machine.service.exception.ServiceException;
import data.test.entity.AddonsData;
import data.test.entity.DrinksData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by oleksij.onysymchuk@gmail on 06.01.2017.
 */
public class CoffeeMachineRefillServiceImplTest {

    @Mock
    private DaoFactory daoFactory;
    @Mock
    private DrinkDao drinkDao;
    @Mock
    private AddonDao addonDao;

    @Mock
    private AbstractConnection connection;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<List<Drink>> drinkListCaptor;

    private CoffeeMachineRefillService service;

    private List<Drink> drinksToUpdate;
    private List<Item> addonsToUpdate;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(daoFactory.getDrinkDao(connection)).thenReturn(drinkDao);
        when(daoFactory.getAddonDao(connection)).thenReturn(addonDao);
        when(daoFactory.getConnection()).thenReturn(connection);


        service = CoffeeMachineRefillServiceImpl.getInstance();
        ((CoffeeMachineRefillServiceImpl) service).setDaoFactory(daoFactory);

        drinksToUpdate = new ArrayList<>();
        addonsToUpdate = new ArrayList<>();

    }


    @Test(expected = NullPointerException.class)
    public void testRefillWithNull() throws Exception {
        service.refill(null);
    }

    @Test(expected = ServiceException.class)
    public void testRefillWithEmpty() throws Exception {
        service.refill(new ItemReceipt(new ArrayList<>(), new ArrayList<>()));
    }

    @Test(expected = ServiceException.class)
    public void testRefillWithNegativeQuantity() throws Exception {
        ItemReceipt receipt = new ItemReceipt();
        int quantity = 3;
        for (int i = 1; i <= 4; i++) {
            Drink drink = new Drink.Builder()
                    .setId(i)
                    .setQuantity(quantity)
                    .build();
            receipt.addDrink(drink);
            quantity--;
        }
        service.refill(new ItemReceipt(new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void testRefillWithOneDrink() {
        Drink actualDrink = DrinksData.AMERICANO.getCopy();
        drinksToUpdate.add(actualDrink);
        when(drinkDao.getAllFromList(any())).thenReturn(drinksToUpdate);

        int quantityToAdd = 5;

        Drink drink = new Drink.Builder()
                .setId(actualDrink.getId())
                .setQuantity(quantityToAdd)
                .build();
        ItemReceipt receipt = new ItemReceipt();
        receipt.addDrink(drink);

        service.refill(receipt);

        Drink updatedDrink = DrinksData.AMERICANO.getCopy();
        updatedDrink.setQuantity(actualDrink.getQuantity() + quantityToAdd);
        List<Drink> updatedDrinks = new ArrayList<>();
        updatedDrinks.add(updatedDrink);

        verify(drinkDao).updateQuantityAllInList(updatedDrinks);
    }

    @Test
    public void testRefillWithOneAddon() {
        Item actualAddon = AddonsData.CINNAMON.getCopy();
        addonsToUpdate.add(actualAddon);
        when(addonDao.getAllFromList(any())).thenReturn(addonsToUpdate);

        int quantityToAdd = 5;

        Item addon = new Item.Builder()
                .setId(actualAddon.getId())
                .setQuantity(quantityToAdd)
                .build();
        ItemReceipt receipt = new ItemReceipt();
        receipt.addAddon(addon);

        service.refill(receipt);

        Item updatedAddon = AddonsData.CINNAMON.getCopy();
        updatedAddon.setQuantity(actualAddon.getQuantity() + quantityToAdd);
        List<Item> updatedAddons = new ArrayList<>();
        updatedAddons.add(updatedAddon);

        verify(addonDao).updateQuantityAllInList(updatedAddons);
    }


    @Test
    public void testRefillSeveralDrinksAndAddons() throws Exception {
        drinksToUpdate.add(DrinksData.AMERICANO.getCopy());
        drinksToUpdate.add(DrinksData.TEA_WITH_SUGAR.getCopy());
        drinksToUpdate.add(DrinksData.LATTE.getCopy());
        when(drinkDao.getAllFromList(any())).thenReturn(drinksToUpdate);

        addonsToUpdate.add(AddonsData.CINNAMON.getCopy());
        addonsToUpdate.add(AddonsData.SUGAR.getCopy());
        when(addonDao.getAllFromList(any())).thenReturn(addonsToUpdate);

        int quantityToAdd = 5;

        ItemReceipt receipt = createReceipt(quantityToAdd);


        service.refill(receipt);

        List<Drink> updatedDrinks = new ArrayList<Drink>() {{
            add(DrinksData.AMERICANO.getCopy());
            add(DrinksData.TEA_WITH_SUGAR.getCopy());
            add(DrinksData.LATTE.getCopy());
        }};
        increaseEachItemQuantityInListBy(updatedDrinks, quantityToAdd);
        List<Item> updatedAddons = new ArrayList<Item>() {{
            add(AddonsData.CINNAMON.getCopy());
            add(AddonsData.SUGAR.getCopy());
        }};
        increaseEachItemQuantityInListBy(updatedAddons, quantityToAdd);


        verify(drinkDao).updateQuantityAllInList(updatedDrinks);
        verify(addonDao).updateQuantityAllInList(updatedAddons);

    }

    private ItemReceipt createReceipt(int quantityToAdd) {
        ItemReceipt receipt = new ItemReceipt();
        for (Drink drink : drinksToUpdate) {
            Drink newDrinkToAdd = new Drink.Builder()
                    .setId(drink.getId())
                    .setQuantity(quantityToAdd)
                    .build();

            receipt.addDrink(newDrinkToAdd);
        }
        for (Item addon : addonsToUpdate) {
            Item newAddonToAdd = new Item.Builder()
                    .setId(addon.getId())
                    .setQuantity(quantityToAdd)
                    .build();

            receipt.addAddon(newAddonToAdd);
        }
        return receipt;
    }

    private void increaseEachItemQuantityInListBy(List<? extends Item> items, int quantityToAdd) {
        items.forEach(item -> item.setQuantity(item.getQuantity() + quantityToAdd));
    }


}