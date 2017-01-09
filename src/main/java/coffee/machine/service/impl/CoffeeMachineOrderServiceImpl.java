package coffee.machine.service.impl;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.dao.*;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.CoffeeMachineOrderService;
import coffee.machine.service.logging.ServiceErrorProcessing;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

import static coffee.machine.i18n.message.key.error.ServiceErrorKey.*;

/**
 * This class is an implementation of CoffeeMachineOrderService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class CoffeeMachineOrderServiceImpl implements CoffeeMachineOrderService, ServiceErrorProcessing {
    private static final Logger logger = Logger.getLogger(CoffeeMachineOrderServiceImpl.class);

    DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private final int COFFEE_MACHINE_ACCOUNT_ID = CoffeeMachineConfig.ACCOUNT_ID;

    private CoffeeMachineOrderServiceImpl() {
    }

    private static class InstanceHolder {
        private static final CoffeeMachineOrderService instance = new CoffeeMachineOrderServiceImpl();
    }

    public static CoffeeMachineOrderService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Order prepareOrder(Order preOrder) {

        checkOrder(preOrder);

        try (AbstractConnection connection = daoFactory.getConnection()) {
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            AccountDao accountDao = daoFactory.getAccountDao(connection);
            OrderDao orderDao = daoFactory.getOrderDao(connection);

            connection.beginSerializableTransaction();

            List<Drink> actualDrinks = drinkDao.getAllByIds(preOrder.getDrinkIds());
            List<Item> actualAddons = addonDao.getAllByIds(preOrder.getAddonIds());
            Order order = fillOrderWithAbsentData(preOrder, actualDrinks, actualAddons);
            Account userAccount = accountDao.getByUserId(order.getUserId())
                    .orElseThrow(IllegalStateException::new);

            checkUserHaveEnoughMoney(order.getTotalCost(), userAccount.getAmount());
            deductActualQuantitiesByOrderQuantities(actualDrinks, actualAddons, order);

            saveUpdatedDrinkQuantities(drinkDao, actualDrinks);
            saveUpdatedAddonQuantities(addonDao, actualAddons);
            performMoneyExchange(accountDao, userAccount, order.getTotalCost());
            saveOrder(orderDao, order);

            connection.commitTransaction();
            return order;
        }

    }

    private void checkOrder(Order order) {
        Objects.requireNonNull(order);
        order.clearZeroItems();
        if (order.isEmpty()) {
            logErrorAndThrowNewServiceException(logger, YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY);
        }
        if (order.hasNegativeQuantity()) {
            logErrorAndThrowNewServiceException(logger, QUANTITY_SHOULD_BE_NON_NEGATIVE);
        }
    }



    private Order fillOrderWithAbsentData(Order preOrder, List<Drink> actualDrinks, List<Item> actualAddons) {
        return preOrder
                .fillAbsentDrinkData(actualDrinks)
                .fillAbsentAddonData(actualAddons)
                .setCurrentDate()
                .calculateTotalPrice();
    }


    private void checkUserHaveEnoughMoney(long drinksPrice, long userAccountAmount) {
        if (userAccountAmount < drinksPrice) {
            logErrorAndThrowNewServiceException(
                    logger, NOT_ENOUGH_MONEY,
                    String.format("%.2f", drinksPrice * CoffeeMachineConfig.DB_MONEY_COEFF));
        }
    }

    private void deductActualQuantitiesByOrderQuantities(List<Drink> actualDrinks,
                                                         List<Item> actualAddons,
                                                         Order order) {
        for (Drink orderedDrink : order.getDrinks()) {
            deductActualQuantityByItemQuantity(actualDrinks, orderedDrink);
            for (Item orderedAddon : orderedDrink.getAddons()) {
                deductActualQuantityByItemQuantity(actualAddons, orderedAddon, orderedDrink.getQuantity());
            }
        }
    }

    private void deductActualQuantityByItemQuantity(List<? extends Item> actualItems, Item orderedItem) {
        int quantityWithoutParent = 1;
        deductActualQuantityByItemQuantity(actualItems, orderedItem, quantityWithoutParent);
    }

    private void deductActualQuantityByItemQuantity(List<? extends Item> actualItems,
                                                    Item orderedItem,
                                                    int parentQuantity) {
        Item actualDrink = actualItems.stream()
                .filter(item -> item.getId() == orderedItem.getId())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        int actualQuantity = actualDrink.getQuantity();
        int quantityToBuy = orderedItem.getQuantity() * parentQuantity;

        if (actualQuantity >= quantityToBuy) {
            actualDrink.setQuantity(actualQuantity - quantityToBuy);
        } else {
            logErrorAndThrowNewServiceException(logger, ITEM_NO_LONGER_AVAILABLE, orderedItem.getName());
        }
    }

    private void saveUpdatedDrinkQuantities(DrinkDao drinkDao, List<Drink> actualDrinks) {
        drinkDao.updateQuantityAllInList(actualDrinks);
    }

    private void saveUpdatedAddonQuantities(AddonDao addonDao, List<Item> actualAddons) {
        addonDao.updateQuantityAllInList(actualAddons);
    }

    private void performMoneyExchange(AccountDao accountDao, Account userAccount, long drinksPrice) {
        Account coffeeMachineAccount = accountDao.getById(COFFEE_MACHINE_ACCOUNT_ID)
                .orElseThrow(IllegalStateException::new);
        userAccount.withdraw(drinksPrice);
        coffeeMachineAccount.add(drinksPrice);
        accountDao.update(coffeeMachineAccount);
        accountDao.update(userAccount);
    }

    private void saveOrder(OrderDao orderDao, Order order) {
        orderDao.insert(order);
    }


}
