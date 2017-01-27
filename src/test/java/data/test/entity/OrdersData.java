package data.test.entity;

import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;

import java.util.Arrays;
import java.util.Date;

import static data.test.entity.AddonsData.*;
import static data.test.entity.DrinksData.*;
import static data.test.entity.OrdersData.ConstHolder.*;

/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum OrdersData {
    A1(
            1,
            2,
            new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 5 * MIN),
            1000,
            getDrinkForOrderByDataAndQuantity(WATER, 1)
    ),
    A2(
            2,
            2,
            new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 15 * MIN),
            2000,
            getDrinkForOrderByDataAndQuantity(WATER, 2),
            getDrinkWithOneAddonForOrder(),
            getDrinkWithTwoAddonsForOrder()

    );

    public final Order order;

    static class ConstHolder {
        static final long YEAR = 365L * 24 * 3600 * 1000;
        static final long MONTH = 30L * 24 * 3600 * 1000;
        static final long DAY = 24L * 3600 * 1000;
        static final long HOUR = 3600L * 1000;
        static final long MIN = 60L * 1000;
        static final long YEAR2016 = YEAR * (2016 - 1970);
    }


    OrdersData(int id, int userId, Date date, long amount, Drink... drinks) {
        order = new Order.Builder()
                .setId(id)
                .setUserId(userId)
                .setDate(date)
                .setTotalCost(amount)
                .build();
        Arrays.stream(drinks)
                .forEach(order::addDrink);
    }

    private static Drink getDrinkWithOneAddonForOrder() {
        Product lemon = getAddonForOrderByDataAndQuantity(LEMON,1);
        Drink drink2 = getDrinkForOrderByDataAndQuantity(TEA_WITH_SUGAR, 1);
        drink2.getAddons().add(lemon);
        return drink2;
    }

    private static Drink getDrinkWithTwoAddonsForOrder() {
        Drink drink2 = getDrinkForOrderByDataAndQuantity(ESPRESSO, 3);
        Product sugar = getAddonForOrderByDataAndQuantity(SUGAR,2);
        Product milk = getAddonForOrderByDataAndQuantity(MILK, 1);
        drink2.getAddons().add(sugar);
        drink2.getAddons().add(milk);
        return drink2;
    }

    private static Product getAddonForOrderByDataAndQuantity(AddonsData addonData, int quantity) {
        Product lemon = addonData.getCopy();
        lemon.setQuantity(quantity);
        lemon.setPrice(0);
        return lemon;
    }

    private static Drink getDrinkForOrderByDataAndQuantity(DrinksData drinkData, int quantity) {
        Drink drink = drinkData.getCopy();
        drink.getAddons().clear();
        drink.setQuantity(quantity);
        drink.setPrice(0);
        return drink;
    }
}


