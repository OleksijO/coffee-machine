package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.value.object.ItemReceipt;
import coffee.machine.service.RefillService;
import coffee.machine.service.exception.ServiceException;

import java.util.List;
import java.util.Objects;

import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.ADMIN_REFILL_NOTHING_TO_ADD;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;

/**
 * This class is an implementation of OrderPreparationService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RefillServiceImpl implements RefillService {
    private static final String LOG_MESSAGE_ADMIN_REFILL_NOTHING_TO_ADD = "Nothing to add. Details: ";
    private static final String LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE = "Item quantity is negative. Details: ";

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
    public void refill(ItemReceipt receipt) {

        checkReceipt(receipt);

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
        receipt.clearZeroItems();
        if (receipt.isEmpty()) {

            throw new ServiceException()
                    .addMessageKey(ADMIN_REFILL_NOTHING_TO_ADD)
                    .addLogMessage(LOG_MESSAGE_ADMIN_REFILL_NOTHING_TO_ADD + receipt);

        }
        if (receipt.hasNegativeQuantity()) {
            throw new ServiceException()
                    .addMessageKey(QUANTITY_SHOULD_BE_NON_NEGATIVE)
                    .addLogMessage(LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE + receipt);
        }
    }

    private void refillDrinks(ItemReceipt receipt, DrinkDao drinkDao) {
        if (receipt.getDrinks().isEmpty()){
            return;
        }
        List<Drink> actualDrinks = drinkDao.getAllFromList(receipt.getDrinks());
        incrementQuantitiesInListByAnotherItemList(actualDrinks, receipt.getDrinks());
        drinkDao.updateQuantityAllInList(actualDrinks);
    }


    private void incrementQuantitiesInListByAnotherItemList(List<? extends Item> items, List<? extends Item> addingItems) {
        for (Item addingItem : addingItems) {
            items.stream()
                    .filter(item -> item.getId() == addingItem.getId())
                    .findFirst()
                    .orElseThrow(IllegalStateException::new)
                    .incrementQuantityBy(addingItem.getQuantity());
        }
    }

    private void refillAddons(ItemReceipt receipt, AddonDao addonDao) {
        if (receipt.getAddons().isEmpty()){
            return;
        }
        List<Item> actualAddons = addonDao.getAllFromList(receipt.getAddons());
        incrementQuantitiesInListByAnotherItemList(actualAddons, receipt.getAddons());
        addonDao.updateQuantityAllInList(actualAddons);
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
