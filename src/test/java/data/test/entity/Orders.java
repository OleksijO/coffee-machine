package data.test.entity;

import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Drink;

import java.util.Date;

import static data.test.entity.Orders.ConstHolder.*;

/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum Orders {
    A1(1, 2, new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 5 * MIN), 1000),
    A2(2, 2, new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 15 * MIN), 2000);

    public final Order order;

    static class ConstHolder {
        static final long YEAR = 365L * 24 * 3600 * 1000;
        static final long MONTH = 30L * 24 * 3600 * 1000;
        static final long DAY = 24L * 3600 * 1000;
        static final long HOUR = 3600L * 1000;
        static final long MIN = 60L * 1000;
        static final long YEAR2016 = YEAR * (2016 - 1970);
    }


    Orders(int id, int userId, Date date, long amount) {
        order = new Order.Builder()
                .setId(id)
                .setUserId(userId)
                .setDate(date)
                .setTotalCost(amount)
                .build();
        switch (id) {
            case 1:
                Drink drink = DrinksData.WATER.getCopy().getBaseDrink();
                drink.setQuantity(1);
                drink.setPrice(0);
                order.addDrink(drink);
                break;
            case 2:
                Drink drink2 = DrinksData.WATER.getCopy().getBaseDrink();
                drink2.setQuantity(2);
                drink2.setPrice(0);
                order.addDrink(drink2);
                drink2 = DrinksData.TEA_WITH_SUGAR.getCopy().getBaseDrink();
                drink2.setQuantity(1);
                drink2.setPrice(0);
                drink2.getAddons().forEach(addon -> {
                    if (addon.getId() == 5) {
                        addon.setQuantity(1);
                    }
                    addon.setPrice(0);
                });
                order.addDrink(drink2);
                drink2 = DrinksData.ESPRESSO.getCopy().getBaseDrink();
                drink2.setQuantity(3);
                drink2.setPrice(0);
                drink2.getAddons().forEach(addon -> {
                    if (addon.getId() == 7) {
                        addon.setQuantity(2);
                    }
                    if (addon.getId() == 8) {
                        addon.setQuantity(1);
                    }
                    addon.setPrice(0);
                });
                order.addDrink(drink2);
                break;

        }


    }
}


