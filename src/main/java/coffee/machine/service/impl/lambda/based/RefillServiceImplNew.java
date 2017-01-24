package coffee.machine.service.impl.lambda.based;

import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.value.object.ProductsReceipt;
import coffee.machine.service.RefillService;
import coffee.machine.service.impl.lambda.based.wrapper.GenericService;

import java.util.List;
import java.util.Objects;

/**
 * This class is an implementation of OrderPreparationService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RefillServiceImplNew extends GenericService implements RefillService {

    private RefillServiceImplNew(DaoFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final RefillService instance = new RefillServiceImplNew(DaoFactoryImpl.getInstance());
    }

    public static RefillService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public void refill(ProductsReceipt receipt) {

        Objects.requireNonNull(receipt);

        executeInSerializableTransactionalVoidWrapper(connection -> {

            refillDrinks(receipt, connection);
            refillAddons(receipt, connection);

        });
    }

    private void refillDrinks(ProductsReceipt receipt, DaoConnection connection) {
        if (receipt.getDrinks().isEmpty()) {
            return;
        }
        DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
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

    private void refillAddons(ProductsReceipt receipt, DaoConnection connection) {
        if (receipt.getAddons().isEmpty()) {
            return;
        }
        AddonDao addonDao = daoFactory.getAddonDao(connection);
        List<Product> actualAddons = addonDao.getAllFromList(receipt.getAddons());
        incrementQuantitiesInListByAnotherProductList(actualAddons, receipt.getAddons());
        addonDao.updateQuantityAllInList(actualAddons);
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
