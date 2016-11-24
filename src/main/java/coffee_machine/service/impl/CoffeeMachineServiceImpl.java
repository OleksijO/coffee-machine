package coffee_machine.service.impl;

import coffee_machine.dao.*;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.Account;
import coffee_machine.model.entity.HistoryRecord;
import coffee_machine.model.entity.goods.AbstractGoods;
import coffee_machine.model.entity.goods.Addon;
import coffee_machine.model.entity.goods.Drink;
import coffee_machine.service.CoffeeMachineService;
import coffee_machine.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class CoffeeMachineServiceImpl implements CoffeeMachineService {
    private static final Logger logger = Logger.getLogger(CoffeeMachineServiceImpl.class);
    private static CoffeeMachineServiceImpl instance;
    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private static final int COFFEE_MACHINE_ACCOUNT_ID = 1;

    public static CoffeeMachineService getInstance() {
        CoffeeMachineServiceImpl localInstance = instance;
        if (instance == null) {
            synchronized (CoffeeMachineServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CoffeeMachineServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    public HistoryRecord prepareDrinksForUser(List<Drink> drinks, int userId) {
        HistoryRecord historyRecord=null;
        try (AbstractConnection connection = daoFactory.getConnection()) {
            List<Drink> users = null;
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            UserDao userDao = daoFactory.getUserDao(connection);
            AccountDao accountDao = daoFactory.getAccountDao(connection);
            HistoryRecordDao historyDao = daoFactory.getHistoryRecordDao(connection);
            List<Drink> baseDrinksToBuy = getBaseDrinksFromDrinks(drinks);
            new ArrayList<>(drinks.size());
            drinks.forEach(drink -> baseDrinksToBuy.add(drink.getBaseDrink()));
            List<Addon> addonsToBuy = getAddonsFromDrinks(drinks);
            long drinksPrice = getSummaryPrice(drinks);
            try {
                connection.beginTransaction();
                List<Drink> baseDrinksAvailable = drinkDao.getAllFromList(baseDrinksToBuy);
                List<Addon> addonsAvailable = addonDao.getAllFromList(addonsToBuy);
                Account userAccount = userDao.getById(userId).getAccount();
                if (userAccount.getAmount() < drinksPrice) {
                    connection.rollbackTransaction();
                    logErrorAndThrowNewServiceException(
                            "Not enough money to perform the operation. " +
                                    "Please, decrease quantity of drinks or select cheaper one.");
                }
                baseDrinksAvailable = deductGoodsToBuyFromAvailable(baseDrinksToBuy, baseDrinksAvailable);
                addonsAvailable = deductGoodsToBuyFromAvailable(addonsToBuy, addonsAvailable);
                Account coffeeMachineAccount = accountDao.getById(COFFEE_MACHINE_ACCOUNT_ID);
                userAccount.withdrow(drinksPrice);
                coffeeMachineAccount.add(drinksPrice);
                historyRecord = new HistoryRecord(new Date(), drinks.toString(), drinksPrice);
                historyDao.insert(historyRecord);
                drinkDao.updateQuantityAllInList(baseDrinksAvailable);
                addonDao.updateQuantityAllInList(addonsAvailable);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
            return historyRecord;
        }
    }

    private List<Drink> getBaseDrinksFromDrinks(List<Drink> drinks) {
        Set<Drink> baseDrinks = new HashSet<>();
        drinks.forEach(drink -> {
            Drink baseDrinkToBuy = drink.getBaseDrink();
            Optional<Drink> sameBaseDrink = baseDrinks.stream()
                    .filter(presentBaseDrink -> presentBaseDrink.equals(baseDrinkToBuy))
                    .findFirst();
            if (sameBaseDrink.isPresent()) {
                sameBaseDrink.get().setQuantity(sameBaseDrink.get().getQuantity() + drink.getQuantity());
            } else {
                baseDrinks.add(baseDrinkToBuy);
            }
        });
        return new ArrayList<>(baseDrinks);
    }

    private List<Addon> getAddonsFromDrinks(List<Drink> drinks) {
        Set<Addon> addons = new HashSet<>();
        drinks.forEach(drink -> {
            drink.getAddons().forEach(addon -> {
                Optional<Addon> sameAddon = addons.stream()
                        .filter(presentAddon -> presentAddon.equals(addon))
                        .findFirst();
                if (sameAddon.isPresent()) {
                    sameAddon.get().setQuantity(sameAddon.get().getQuantity() + addon.getQuantity());
                } else {
                    addons.add(addon);
                }
            });
        });
        return new ArrayList<>(addons);
    }

    private long getSummaryPrice(List<Drink> drinks) {
        final long price[] = new long[1];
        drinks.forEach(drink -> price[0] += drink.getTotalPrice());
        return price[0];
    }

    private <T extends AbstractGoods> List<T> deductGoodsToBuyFromAvailable(List<T> goodsToBuy, List<T> goodsAvailable) {
        goodsAvailable.forEach(goods -> {
            Optional<T> sameGoods = goodsAvailable.stream()
                    .filter(presentGoods -> presentGoods.equals(goods))
                    .findFirst();
            if (sameGoods.isPresent()) {
                sameGoods.get().setQuantity(sameGoods.get().getQuantity() - goods.getQuantity());
                if (sameGoods.get().getQuantity() < 0) {
                    logErrorAndThrowNewServiceException("Goods " + sameGoods.get() +
                            " quantity is not enough to perform operation. " +
                            "Please, decrease quantity or select another one.");
                }
            } else {
                logErrorAndThrowNewServiceException("Goods " + sameGoods.get() +
                        " is no longer availible. Please, select another one.");
            }
        });

        return goodsAvailable;
    }

    private void logErrorAndThrowNewServiceException(String message) {
        logger.error(message);
        throw new DaoException(message);
    }

    private void logErrorAndThrowWrapperServiceException(String message, Throwable e) {
        logger.error(message, e);
        throw new ServiceException(message, e);
    }


}
