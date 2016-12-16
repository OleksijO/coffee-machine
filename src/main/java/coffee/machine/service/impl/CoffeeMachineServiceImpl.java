package coffee.machine.service.impl;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.dao.*;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.logging.ServiceErrorProcessing;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.service.CoffeeMachineService;
import org.apache.log4j.Logger;

import java.util.*;

import static coffee.machine.i18n.message.key.error.ServiceErrorKey.ITEM_NO_LONGER_AVAILABLE;
import static coffee.machine.i18n.message.key.error.ServiceErrorKey.NOT_ENOUGH_MONEY;
import static coffee.machine.i18n.message.key.error.ServiceErrorKey.YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY;

/**
 * This class is an implementation of CoffeeMachineService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class CoffeeMachineServiceImpl implements CoffeeMachineService, ServiceErrorProcessing {
    private static final Logger logger = Logger.getLogger(CoffeeMachineServiceImpl.class);

    DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private final int COFFEE_MACHINE_ACCOUNT_ID = CoffeeMachineConfig.ACCOUNT_ID;

    private CoffeeMachineServiceImpl() {
    }

    private static class InstanceHolder {
        private static final CoffeeMachineService instance = new CoffeeMachineServiceImpl();
    }

    public static CoffeeMachineService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Order prepareDrinksForUser(List<Drink> drinks, int userId) {

        checkArgs(drinks);

        long drinksPrice = getSummaryPrice(drinks);

        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            AccountDao accountDao = daoFactory.getAccountDao(connection);
            OrderDao orderDao = daoFactory.getOrderDao(connection);

            connection.beginTransaction();

            Account userAccount = accountDao.getByUserId(userId);
            checkUserHaveEnoughMoney(drinksPrice, userAccount.getAmount());
            decreaseItemQuantitiesInDatabaseByItemQuantitiesInDrinksToBuy(drinks, drinkDao, addonDao);
            performMoneyExchange(accountDao, userAccount, drinksPrice);

            Order order = new Order(userId, new Date(), drinks, drinksPrice);
            orderDao.insert(order);

            connection.commitTransaction();

            return order;
        }

    }

    private void checkArgs(List<Drink> drinks) {
        Objects.requireNonNull(drinks);
        if (drinks.size() == 0) {
            logErrorAndThrowNewServiceException(logger, YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY);
        }
    }

    private long getSummaryPrice(List<Drink> drinks) {
        final long price[] = new long[1];
        drinks.forEach(drink -> price[0] += drink.getTotalPrice() * drink.getQuantity());
        return price[0];
    }

    private void checkUserHaveEnoughMoney(long drinksPrice, long userAccountAmount) {
        if (userAccountAmount < drinksPrice) {
            logErrorAndThrowNewServiceException(
                    logger, NOT_ENOUGH_MONEY,
                    String.format("%.2f", drinksPrice * CoffeeMachineConfig.DB_MONEY_COEFF));
        }
    }



    private void decreaseItemQuantitiesInDatabaseByItemQuantitiesInDrinksToBuy(List<Drink> drinks,
                                                                               DrinkDao drinkDao,
                                                                               AddonDao addonDao) {
        List<Drink> baseDrinksToBuy = getBaseDrinksFromDrinks(drinks);
        List<Item> addonsToBuy = getAddonsFromDrinks(drinks);
        List<Drink> baseDrinksAvailable = getBaseDrinksFromDrinks(drinkDao.getAllFromList(baseDrinksToBuy));
        baseDrinksAvailable = deductItemsToBuyFromAvailable(baseDrinksToBuy, baseDrinksAvailable);

        if (baseDrinksAvailable.size() > 0) {
            drinkDao.updateQuantityAllInList(baseDrinksAvailable);
        }
        if (addonsToBuy.size() > 0) {
            List<Item> addonsAvailable = addonDao.getAllFromList(addonsToBuy);
            addonsAvailable = deductItemsToBuyFromAvailable(addonsToBuy, addonsAvailable);
            addonDao.updateQuantityAllInList(addonsAvailable);
        }
    }

    private void performMoneyExchange(AccountDao accountDao, Account userAccount, long drinksPrice) {
        Account coffeeMachineAccount = accountDao.getById(COFFEE_MACHINE_ACCOUNT_ID);
        userAccount.withdraw(drinksPrice);
        coffeeMachineAccount.add(drinksPrice);
        accountDao.update(coffeeMachineAccount);
        accountDao.update(userAccount);
    }

    private List<Drink> getBaseDrinksFromDrinks(List<Drink> drinks) {
        HashMap<Drink, Integer> baseDrinks = new HashMap<>();

        drinks.forEach(drink -> {
            int quantity = drink.getQuantity();
            Drink baseDrink = drink.getBaseDrink();
            baseDrink.setQuantity(0);
            if (!baseDrinks.containsKey(baseDrink)) {
                baseDrinks.put(baseDrink, 0);
            }
            baseDrinks.put(baseDrink, baseDrinks.get(baseDrink) + quantity);
        });

        baseDrinks.keySet().forEach(drink -> drink.setQuantity(baseDrinks.get(drink)));

        return new ArrayList<>(baseDrinks.keySet());
    }

    private List<Item> getAddonsFromDrinks(List<Drink> drinks) {
        HashMap<Item, Integer> addons = new HashMap<>();
        drinks.forEach(drink -> drink.getAddons().forEach(addonToBy -> {
            int quantity = addonToBy.getQuantity();
            Item addon = addonToBy.getCopy();
            addon.setQuantity(0);
            if (!addons.containsKey(addon)) {
                addons.put(addon, 0);
            }
            addons.put(addon, addons.get(addon) + quantity * drink.getQuantity());
        }));

        List<Item> addonsWithQuantity = new ArrayList<>();

        addons.keySet().forEach(addon -> {
            int quantity = addons.get(addon);
            if (quantity > 0) {
                addon.setQuantity(quantity);
                addonsWithQuantity.add(addon);
            }
        });

        return addonsWithQuantity;
    }



    private <T extends Item> List<T> deductItemsToBuyFromAvailable(List<T> itemsToBuy, List<T> itemsAvailable) {
        HashMap<Integer, Integer> itemsAvailableQuantity = new HashMap<>();

        itemsAvailable.forEach(itemAvailable -> {
            int quantity = itemAvailable.getQuantity();
            itemAvailable.setQuantity(0);
            if (!itemsAvailableQuantity.containsKey(itemAvailable.getId())) {
                itemsAvailableQuantity.put(itemAvailable.getId(), 0);
            }
            itemsAvailableQuantity.put(itemAvailable.getId(), itemsAvailableQuantity.get(itemAvailable.getId()) + quantity);
        });

        itemsToBuy.forEach(itemToBuy -> {
            int availableQuantity = -1;
            if (itemsAvailableQuantity.containsKey(itemToBuy.getId())) {
                availableQuantity = itemsAvailableQuantity.get(itemToBuy.getId());
            }
            availableQuantity -= itemToBuy.getQuantity();
            if (availableQuantity < 0) {
                logErrorAndThrowNewServiceException(logger, ITEM_NO_LONGER_AVAILABLE, itemToBuy.getName());
            }
            itemsAvailableQuantity.put(itemToBuy.getId(), availableQuantity);
        });

        itemsAvailable.forEach(
                itemAvailable -> itemAvailable.setQuantity(itemsAvailableQuantity.get(itemAvailable.getId())));

        return itemsAvailable;
    }
}
