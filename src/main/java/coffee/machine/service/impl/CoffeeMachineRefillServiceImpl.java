package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.i18n.message.key.error.ServiceErrorKey;
import coffee.machine.model.entity.item.Addons;
import coffee.machine.model.entity.item.Drinks;
import coffee.machine.model.entity.item.ItemReceipt;
import coffee.machine.service.CoffeeMachineRefillService;
import coffee.machine.service.logging.ServiceErrorProcessing;
import org.apache.log4j.Logger;

import java.util.Objects;

import static coffee.machine.i18n.message.key.error.ServiceErrorKey.ADMIN_REFILL_NOTHING_TO_ADD;

/**
 * This class is an implementation of CoffeeMachineOrderService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class CoffeeMachineRefillServiceImpl implements CoffeeMachineRefillService, ServiceErrorProcessing {
    private static final Logger logger = Logger.getLogger(CoffeeMachineRefillServiceImpl.class);

    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private CoffeeMachineRefillServiceImpl() {
    }

    private static class InstanceHolder {
        private static final CoffeeMachineRefillService instance = new CoffeeMachineRefillServiceImpl();
    }

    public static CoffeeMachineRefillService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public void refill(ItemReceipt receipt) {

        checkReceipt(receipt);
        receipt.clearZeroItems();

        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            AddonDao addonDao = daoFactory.getAddonDao(connection);

            connection.beginSerializableTransaction();

            refillDrinks(receipt, drinkDao);
            refillAddons(receipt, addonDao);

            connection.commitTransaction();
        }
    }

    private void checkReceipt(ItemReceipt receipt) {
        Objects.requireNonNull(receipt);
        if (receipt.isEmpty()) {
            logErrorAndThrowNewServiceException(logger, ADMIN_REFILL_NOTHING_TO_ADD);
        }
        if (receipt.hasNegativeQuantity()) {
            logErrorAndThrowNewServiceException(logger, ServiceErrorKey.QUANTITY_SHOULD_BE_NON_NEGATIVE);
        }
    }

    private void refillDrinks(ItemReceipt receipt, DrinkDao drinkDao) {
        if (receipt.getDrinks().isEmpty()){
            return;
        }
        Drinks actualDrinks = loadActualDrinks(receipt, drinkDao);
        actualDrinks.incrementQuantities(receipt.getDrinks());
        drinkDao.updateQuantityAllInList(actualDrinks.getDrinks());
    }

    private Drinks loadActualDrinks(ItemReceipt receipt, DrinkDao drinkDao) {
        return new Drinks()
                .add(drinkDao.getAllFromList(receipt.getDrinks()));
    }

    private void refillAddons(ItemReceipt receipt, AddonDao addonDao) {
        if (receipt.getAddons().isEmpty()){
            return;
        }
        Addons actualAddons = loadActualAddons(receipt, addonDao);
        actualAddons.incrementQuantities(receipt.getAddons());
        addonDao.updateQuantityAllInList(actualAddons.getAddons());
    }

    private Addons loadActualAddons(ItemReceipt receipt, AddonDao addonDao) {
        return new Addons()
                .add(addonDao.getAllFromList(receipt.getAddons()));
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
