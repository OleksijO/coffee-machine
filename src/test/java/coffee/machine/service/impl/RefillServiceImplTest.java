package coffee.machine.service.impl;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoManagerFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.value.object.ProductsReceipt;
import coffee.machine.service.RefillService;
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
 * @author oleksij.onysymchuk@gmail.com
 */
public class RefillServiceImplTest {

    @Mock
    private DaoManagerFactory daoFactory;
    @Mock
    private DrinkDao drinkDao;
    @Mock
    private AddonDao addonDao;

    @Mock
    private DaoManager daoManager;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<List<Drink>> drinkListCaptor;

    private RefillService service;

    private List<Drink> drinksToUpdate;
    private List<Product> addonsToUpdate;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(daoFactory.createDaoManager()).thenReturn(daoManager);
        when(daoManager.getDrinkDao()).thenReturn(drinkDao);
        when(daoManager.getAddonDao()).thenReturn(addonDao);

        service = new RefillServiceImpl();
        ((RefillServiceImpl) service).setDaoFactory(daoFactory);

        drinksToUpdate = new ArrayList<>();
        addonsToUpdate = new ArrayList<>();
    }


    @Test(expected = NullPointerException.class)
    public void testRefillThrowsExceptionOnNullReceipt()  {
        service.refill(null);
    }

    @Test
    public void testRefillUpdatesDrinksIfReceiptHasOnlyOneDrink() {
        Drink actualDrink = DrinksData.AMERICANO.getCopy();
        drinksToUpdate.add(actualDrink);
        when(drinkDao.getAllFromList(any())).thenReturn(drinksToUpdate);

        int quantityToAdd = 5;

        ProductsReceipt receipt = createCorrectReceipt(quantityToAdd, 0);

        service.refill(receipt);

        Drink updatedDrink = DrinksData.AMERICANO.getCopy();
        updatedDrink.setQuantity(updatedDrink.getQuantity() + quantityToAdd);
        List<Drink> updatedDrinks = new ArrayList<>();
        updatedDrinks.add(updatedDrink);

        verify(drinkDao).updateQuantityAllInList(updatedDrinks);
    }

    @Test
    public void testRefillUpdatesAddonsIfReceiptHasOnlyOneAddon() {
        Product actualAddon = AddonsData.CINNAMON.getCopy();
        addonsToUpdate.add(actualAddon);
        when(addonDao.getAllFromList(any())).thenReturn(addonsToUpdate);

        int quantityToAdd = 5;

        ProductsReceipt receipt = createCorrectReceipt(0, quantityToAdd);

        service.refill(receipt);

        Product updatedAddon = AddonsData.CINNAMON.getCopy();
        updatedAddon.setQuantity(updatedAddon.getQuantity() + quantityToAdd);
        List<Product> updatedAddons = new ArrayList<>();
        updatedAddons.add(updatedAddon);

        verify(addonDao).updateQuantityAllInList(updatedAddons);
    }


    @Test
    public void testRefillUpdatesDrinksAndAddonsIfReceiptHasSeveralDrinksAndAddons()  {
        drinksToUpdate.add(DrinksData.AMERICANO.getCopy());
        drinksToUpdate.add(DrinksData.TEA_WITH_SUGAR.getCopy());
        drinksToUpdate.add(DrinksData.LATTE.getCopy());
        when(drinkDao.getAllFromList(any())).thenReturn(drinksToUpdate);

        addonsToUpdate.add(AddonsData.CINNAMON.getCopy());
        addonsToUpdate.add(AddonsData.SUGAR.getCopy());
        when(addonDao.getAllFromList(any())).thenReturn(addonsToUpdate);

        int quantityToAdd = 5;
        ProductsReceipt receipt = createCorrectReceipt(quantityToAdd, quantityToAdd);
        service.refill(receipt);

        List<Drink> updatedDrinks = getCopyOfDrinksToBeUpdated();
        increaseEachProductQuantityInListBy(updatedDrinks, quantityToAdd);
        List<Product> updatedAddons = getCopyOfAddonsToBeUpdated();
        increaseEachProductQuantityInListBy(updatedAddons, quantityToAdd);

        verify(drinkDao).updateQuantityAllInList(updatedDrinks);
        verify(addonDao).updateQuantityAllInList(updatedAddons);

    }

    private ProductsReceipt createCorrectReceipt(int drinkQuantityToAdd, int addonQuantityToAdd) {
        ProductsReceipt receipt = new ProductsReceipt();
        for (Drink drink : drinksToUpdate) {
            Drink newDrinkToAdd = new Drink.Builder()
                    .setId(drink.getId())
                    .setQuantity(drinkQuantityToAdd)
                    .build();

            receipt.addDrink(newDrinkToAdd);
        }
        for (Product addon : addonsToUpdate) {
            Product newAddonToAdd = new Product.Builder()
                    .setId(addon.getId())
                    .setQuantity(addonQuantityToAdd)
                    .build();

            receipt.addAddon(newAddonToAdd);
        }
        return receipt;
    }

    private ArrayList<Drink> getCopyOfDrinksToBeUpdated() {
        return new ArrayList<Drink>() {{
            add(DrinksData.AMERICANO.getCopy());
            add(DrinksData.TEA_WITH_SUGAR.getCopy());
            add(DrinksData.LATTE.getCopy());
        }};
    }

    private ArrayList<Product> getCopyOfAddonsToBeUpdated() {
        return new ArrayList<Product>() {{
            add(AddonsData.CINNAMON.getCopy());
            add(AddonsData.SUGAR.getCopy());
        }};
    }


    private void increaseEachProductQuantityInListBy(List<? extends Product> products, int quantityToAdd) {
        products.forEach(product -> product.setQuantity(product.getQuantity() + quantityToAdd));
    }


}