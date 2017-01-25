package coffee.machine.service.impl;

import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoManagerFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.value.object.ProductsReceipt;
import coffee.machine.service.RefillService;
import coffee.machine.service.impl.wrapper.GenericService;

import java.util.List;
import java.util.Objects;

/**
 * This class is an implementation of OrderPreparationService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RefillServiceImpl extends GenericService implements RefillService {

    private RefillServiceImpl(DaoManagerFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final RefillService instance = new RefillServiceImpl(DaoFactoryImpl.getInstance());
    }

    public static RefillService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public void refill(ProductsReceipt receipt) {

        Objects.requireNonNull(receipt);

        executeInSerializableTransactionalVoidWrapper(daoManager -> {

            refillDrinks(receipt, daoManager);
            refillAddons(receipt, daoManager);

        });
    }

    private void refillDrinks(ProductsReceipt receipt, DaoManager daoManager) {
        if (receipt.getDrinks().isEmpty()) {
            return;
        }
        DrinkDao drinkDao = daoManager.getDrinkDao();
        List<Drink> actualDrinks = drinkDao.getAllFromList(receipt.getDrinks());
        incrementQuantitiesInListByAnotherProductList(actualDrinks, receipt.getDrinks());
        drinkDao.updateQuantityAllInList(actualDrinks);
    }


    private void incrementQuantitiesInListByAnotherProductList(List<? extends Product> products, List<? extends Product> addingProducts) {
        for (Product addingProduct : addingProducts) {
            products.stream()
                    .filter(product -> product.getId() == addingProduct.getId())
                    .findFirst()
                    .orElseThrow(IllegalStateException::new)
                    .incrementQuantityBy(addingProduct.getQuantity());
        }
    }

    private void refillAddons(ProductsReceipt receipt, DaoManager daoManager) {
        if (receipt.getAddons().isEmpty()) {
            return;
        }
        AddonDao addonDao = daoManager.getAddonDao();
        List<Product> actualAddons = addonDao.getAllFromList(receipt.getAddons());
        incrementQuantitiesInListByAnotherProductList(actualAddons, receipt.getAddons());
        addonDao.updateQuantityAllInList(actualAddons);
    }

    public void setDaoFactory(DaoManagerFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
