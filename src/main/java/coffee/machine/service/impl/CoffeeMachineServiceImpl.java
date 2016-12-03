package coffee.machine.service.impl;

import coffee.machine.CoffeeMachineConfig;
import coffee.machine.dao.*;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.logging.ServiceErrorProcessing;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.HistoryRecord;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.service.CoffeeMachineService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static coffee.machine.i18n.message.key.error.ServiceErrorKey.GOODS_NO_LONGER_AVAILABLE;
import static coffee.machine.i18n.message.key.error.ServiceErrorKey.NOT_ENOUGH_MONEY;
import static coffee.machine.i18n.message.key.error.ServiceErrorKey.YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public class CoffeeMachineServiceImpl implements CoffeeMachineService, ServiceErrorProcessing {
    private static final Logger logger = Logger.getLogger(CoffeeMachineServiceImpl.class);

    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private static final int COFFEE_MACHINE_ACCOUNT_ID = CoffeeMachineConfig.ACCOUNT_ID;

    private static class InstanceHolder {
        private static final CoffeeMachineService instance = new CoffeeMachineServiceImpl();
    }

    public static CoffeeMachineService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public HistoryRecord prepareDrinksForUser(List<Drink> drinks, int userId) {

        try (AbstractConnection connection = daoFactory.getConnection()) {

            String orderDescription = drinks.toString();

            /* getting needed DAO */
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            AccountDao accountDao = daoFactory.getAccountDao(connection);
            HistoryRecordDao historyDao = daoFactory.getHistoryRecordDao(connection);
             /* calculationg order price */
            long drinksPrice = getSummaryPrice(drinks);

            /* getting separately drinks and addons */
            List<Drink> baseDrinksToBuy = getBaseDrinksFromDrinks(drinks);
            if (baseDrinksToBuy.size() == 0) {
                logErrorAndThrowNewServiceException(logger, YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY);
            }
            List<Item> addonsToBuy = getAddonsFromDrinks(drinks);

            connection.beginTransaction();

            /*  check if user has enough money to buy selected drinks */
            Account userAccount = accountDao.getByUserId(userId);
            if (userAccount.getAmount() < drinksPrice) {
                logErrorAndThrowNewServiceException(
                        logger, NOT_ENOUGH_MONEY, String.format("%.2f", drinksPrice * CoffeeMachineConfig.DB_MONEY_COEFF));
            }

            /*  getting available drinks and addons */
            List<Drink> baseDrinksAvailable = getBaseDrinksFromDrinks(drinkDao.getAllFromList(baseDrinksToBuy));
            List<Item> addonsAvailable = null;
            if (addonsToBuy.size() > 0) {
                addonsAvailable = addonDao.getAllFromList(addonsToBuy);
            }

            /* checking if there is enough available quantity of drinks and addons to prepare selected drinks
             * by deducting retrieved available goods quantities from the quantities of goods gotten in parameter */
            baseDrinksAvailable = deductGoodsToBuyFromAvailable(baseDrinksToBuy, baseDrinksAvailable);
            if (addonsToBuy.size() > 0) {
                addonsAvailable = deductGoodsToBuyFromAvailable(addonsToBuy, addonsAvailable);
            }
      
            /* performing money exchange */
            Account coffeeMachineAccount = accountDao.getById(COFFEE_MACHINE_ACCOUNT_ID);
            userAccount.withdrow(drinksPrice);
            coffeeMachineAccount.add(drinksPrice);
            accountDao.update(coffeeMachineAccount);
            accountDao.update(userAccount);

            /* updating quantities of goods in machine */
            if (baseDrinksAvailable.size() > 0) {
                drinkDao.updateQuantityAllInList(baseDrinksAvailable);
            }
            if (addonsToBuy.size() > 0) {
                addonDao.updateQuantityAllInList(addonsAvailable);
            }

            /* forming history record of purchase to return */
            HistoryRecord historyRecord = new HistoryRecord(userId, new Date(), orderDescription, drinksPrice);
            historyDao.insert(historyRecord);

            connection.commitTransaction();

            return historyRecord;
        }

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
        baseDrinks.keySet().forEach(drink -> {
            drink.setQuantity(baseDrinks.get(drink));
        });
        return new ArrayList<>(baseDrinks.keySet());
    }

    private List<Item> getAddonsFromDrinks(List<Drink> drinks) {
        HashMap<Item, Integer> addons = new HashMap<>();
        drinks.forEach(drink -> {
            drink.getAddons().forEach(addon -> {
                int quantity = addon.getQuantity();
                addon.setQuantity(0);
                if (!addons.containsKey(addon)) {
                    addons.put(addon, 0);
                }
                addons.put(addon, addons.get(addon) + quantity * drink.getQuantity());
            });
        });
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

    private long getSummaryPrice(List<Drink> drinks) {
        final long price[] = new long[1];
        drinks.forEach(drink -> price[0] += drink.getTotalPrice() * drink.getQuantity());
        return price[0];
    }

    private <T extends Item> List<T> deductGoodsToBuyFromAvailable(List<T> goodsToBuy, List<T> goodsAvailable) {
        HashMap<Integer, Integer> goodsAvailableQuantity = new HashMap<>();
        goodsAvailable.forEach(goods -> {
            int quantity = goods.getQuantity();
            goods.setQuantity(0);
            if (!goodsAvailableQuantity.containsKey(goods.getId())) {
                goodsAvailableQuantity.put(goods.getId(), 0);
            }
            goodsAvailableQuantity.put(goods.getId(), goodsAvailableQuantity.get(goods.getId()) + quantity);
        });

        goodsToBuy.forEach(goods1ToBuy -> {
            int availableQuantity = -1;
            if (goodsAvailableQuantity.containsKey(goods1ToBuy.getId())) {
                availableQuantity = goodsAvailableQuantity.get(goods1ToBuy.getId());
            }
            availableQuantity -= goods1ToBuy.getQuantity();
            if (availableQuantity < 0) {
                logErrorAndThrowNewServiceException(logger, GOODS_NO_LONGER_AVAILABLE, goods1ToBuy.getName());
            }
            goodsAvailableQuantity.put(goods1ToBuy.getId(), availableQuantity);
        });

        goodsAvailable.forEach(goods1Available -> {
            goods1Available.setQuantity(goodsAvailableQuantity.get(goods1Available.getId()));
        });

        return goodsAvailable;
    }
}
