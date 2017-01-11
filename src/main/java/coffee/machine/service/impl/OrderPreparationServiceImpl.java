package coffee.machine.service.impl;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.dao.*;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.OrderPreparationService;
import coffee.machine.service.exception.ServiceException;

import java.util.List;
import java.util.Objects;

import static coffee.machine.config.CoffeeMachineConfig.DB_MONEY_COEFF;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.*;

/**
 * This class is an implementation of OrderPreparationService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class OrderPreparationServiceImpl implements OrderPreparationService {
    private static final String LOG_MESSAGE_NOT_ENOUGH_FORMAT = "There is not enough item id=%d (%s). Ordered = %d, available = %d";
    private static final String LOG_MESSAGE_NOT_ENOUGH_MONEY_FORMAT = "User has insufficient funds. Available amount = %.2f, order cost = %s";
    private static final String LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE = "Item quantity is negative. Order details: ";
    private static final String LOG_MESSAGE_YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY = "Order is empty. Order details: ";

    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private final int COFFEE_MACHINE_ACCOUNT_ID = CoffeeMachineConfig.ACCOUNT_ID;

    private OrderPreparationServiceImpl() {
    }

    private static class InstanceHolder {
        private static final OrderPreparationService instance = new OrderPreparationServiceImpl();
    }

    public static OrderPreparationService getInstance() {
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
            throw new ServiceException()
                    .addMessageKey(YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY)
                    .addLogMessage(LOG_MESSAGE_YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY +order);
        }
        if (order.hasNegativeQuantity()) {
            throw new ServiceException()
                    .addMessageKey(QUANTITY_SHOULD_BE_NON_NEGATIVE)
                    .addLogMessage(LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE + order);
        }
    }


    private Order fillOrderWithAbsentData(Order preOrder, List<Drink> actualDrinks, List<Item> actualAddons) {
        return preOrder
                .fillAbsentDrinkData(actualDrinks)
                .fillAbsentAddonData(actualAddons)
                .setCurrentDate()
                .calculateTotalPrice();
    }


    private void checkUserHaveEnoughMoney(long orderCost, long userAccountAmount) {
        if (userAccountAmount < orderCost) {
            String realOrderCost = String.format("%.2f", orderCost * DB_MONEY_COEFF);
            throw new ServiceException()
                    .addMessageKey(NOT_ENOUGH_MONEY)
                    .addAdditionalMessage(realOrderCost)
                    .addLogMessage(
                            String.format(LOG_MESSAGE_NOT_ENOUGH_MONEY_FORMAT,
                                    userAccountAmount*DB_MONEY_COEFF,
                                    realOrderCost));
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
            throw new ServiceException()
                    .addMessageKey(ITEM_NO_LONGER_AVAILABLE)
                    .addAdditionalMessage(orderedItem.getName())
                    .addLogMessage(
                            String.format(LOG_MESSAGE_NOT_ENOUGH_FORMAT,
                                    orderedItem.getId(),
                                    orderedItem.getName(),
                                    quantityToBuy,
                                    actualQuantity));
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

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
