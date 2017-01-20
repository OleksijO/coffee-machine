package coffee.machine.service.impl;

import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.value.object.ProductsReceipt;
import coffee.machine.service.RefillService;

import java.util.List;
import java.util.Objects;

/**
 * This class is an implementation of OrderPreparationService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RefillServiceImpl implements RefillService {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private RefillServiceImpl() {
    }

    private static class InstanceHolder {
        private static final RefillService instance = new RefillServiceImpl();
    }

    public static RefillService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public void refill(ProductsReceipt receipt) {

        Objects.requireNonNull(receipt);

        try (DaoConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            AddonDao addonDao = daoFactory.getAddonDao(connection);

            connection.beginSerializableTransaction();

            refillDrinks(receipt, drinkDao);
            refillAddons(receipt, addonDao);

            connection.commitTransaction();
        }
    }

    private void refillDrinks(ProductsReceipt receipt, DrinkDao drinkDao) {
        if (receipt.getDrinks().isEmpty()){
            return;
        }
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

    private void refillAddons(ProductsReceipt receipt, AddonDao addonDao) {
        if (receipt.getAddons().isEmpty()){
            return;
        }
        List<Product> actualAddons = addonDao.getAllFromList(receipt.getAddons());
        incrementQuantitiesInListByAnotherProductList(actualAddons, receipt.getAddons());
        addonDao.updateQuantityAllInList(actualAddons);
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
