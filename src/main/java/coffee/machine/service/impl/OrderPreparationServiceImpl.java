package coffee.machine.service.impl;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.dao.*;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
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
    private static final String LOG_MESSAGE_NOT_ENOUGH_FORMAT =
            "There is not enough product id=%d (%s). Ordered = %d, available = %d";
    private static final String LOG_MESSAGE_NOT_ENOUGH_MONEY_FORMAT =
            "User has insufficient funds. Available amount = %.2f, order cost = %s";

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
        Objects.requireNonNull(preOrder);

        try (AbstractConnection connection = daoFactory.getConnection()) {
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            AccountDao accountDao = daoFactory.getAccountDao(connection);
            OrderDao orderDao = daoFactory.getOrderDao(connection);

            connection.beginSerializableTransaction();

            List<Drink> actualDrinks = drinkDao.getAllByIds(preOrder.getDrinkIds());
            List<Product> actualAddons = addonDao.getAllByIds(preOrder.getAddonIds());
            Order order = fillOrderWithAbsentData(preOrder, actualDrinks, actualAddons);
            Account userAccount = accountDao.getByUserId(order.getUserId())
                    .orElseThrow(IllegalStateException::new);

            checkUserHaveEnoughMoney(order.getTotalCost(), userAccount.getAmount());
            decreaseActualQuantitiesByOrderQuantities(actualDrinks, actualAddons, order);

            saveUpdatedDrinkQuantities(drinkDao, actualDrinks);
            saveUpdatedAddonQuantities(addonDao, actualAddons);
            performMoneyExchange(accountDao, userAccount, order.getTotalCost());
            saveOrder(orderDao, order);

            connection.commitTransaction();
            return order;
        }

    }

    private Order fillOrderWithAbsentData(Order preOrder, List<Drink> actualDrinks, List<Product> actualAddons) {
        return preOrder
                .fillAbsentDrinkData(actualDrinks)
                .fillAbsentAddonData(actualAddons)
                .setCurrentDate()
                .calculateTotalCost();
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

    private void decreaseActualQuantitiesByOrderQuantities(List<Drink> actualDrinks,
                                                           List<Product> actualAddons,
                                                           Order order) {
        for (Drink orderedDrink : order.getDrinks()) {
            deductActualQuantityByProductQuantity(actualDrinks, orderedDrink);
            for (Product orderedAddon : orderedDrink.getAddons()) {
                deductActualQuantityByProductQuantity(actualAddons, orderedAddon, orderedDrink.getQuantity());
            }
        }
    }

    private void deductActualQuantityByProductQuantity(List<? extends Product> actualProducts, Product orderedProduct) {
        int quantityWithoutParent = 1;
        deductActualQuantityByProductQuantity(actualProducts, orderedProduct, quantityWithoutParent);
    }

    private void deductActualQuantityByProductQuantity(List<? extends Product> actualProducts,
                                                    Product orderedProduct,
                                                    int parentQuantity) {
        Product actualDrink = actualProducts.stream()
                .filter(product -> product.getId() == orderedProduct.getId())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        int actualQuantity = actualDrink.getQuantity();
        int quantityToBuy = orderedProduct.getQuantity() * parentQuantity;

        if (actualQuantity >= quantityToBuy) {
            actualDrink.setQuantity(actualQuantity - quantityToBuy);
        } else {
            throw new ServiceException()
                    .addMessageKey(PRODUCT_NO_LONGER_AVAILABLE)
                    .addAdditionalMessage(orderedProduct.getName())
                    .addLogMessage(
                            String.format(LOG_MESSAGE_NOT_ENOUGH_FORMAT,
                                    orderedProduct.getId(),
                                    orderedProduct.getName(),
                                    quantityToBuy,
                                    actualQuantity));
        }
    }

    private void saveUpdatedDrinkQuantities(DrinkDao drinkDao, List<Drink> actualDrinks) {
        drinkDao.updateQuantityAllInList(actualDrinks);
    }

    private void saveUpdatedAddonQuantities(AddonDao addonDao, List<Product> actualAddons) {
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
