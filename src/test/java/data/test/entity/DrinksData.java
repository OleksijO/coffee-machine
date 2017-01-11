package data.test.entity;

import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;

import java.util.*;


/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum DrinksData {
    WATER(1, "Вода", 100, 20),
    BORJOMI(2, "Боржоми", 500, 20),
    TEA_WITHOUT_SUGAR(3, "Чай без сахара", 500, 10, AddonsData.LEMON.getCopy()),
    TEA_WITH_SUGAR(4, "Чай с сахаром", 600, 20, AddonsData.LEMON.getCopy(), AddonsData.SUGAR.getCopy()),
    ESPRESSO(6, "Эспрессо", 700, 50, AddonsData.SUGAR.getCopy(), AddonsData.MILK.getCopy(), AddonsData.CREAM.getCopy(), AddonsData.CINNAMON.getCopy()),
    AMERICANO(10, "Американо", 800, 150, AddonsData.SUGAR.getCopy(), AddonsData.MILK.getCopy(), AddonsData.CREAM.getCopy(), AddonsData.CINNAMON.getCopy()),
    MOCACCINO(11, "Мокачино", 1000, 50, AddonsData.SUGAR.getCopy(), AddonsData.MILK.getCopy(), AddonsData.CREAM.getCopy(), AddonsData.CINNAMON.getCopy()),
    LATTE(12, "Латте", 1200, 100, AddonsData.SUGAR.getCopy(), AddonsData.CINNAMON.getCopy());

    public final Drink drink;

    DrinksData(int id, String name, long price, int quantity, Item... addons) {
        drink = new Drink.Builder()
                .setId(id)
                .setName(name)
                .setPrice(price)
                .setQuantity(quantity)
                .build();
        if (addons != null) {
            drink.addAddons(Arrays.asList(addons));
        }

    }

    public Drink getCopy() {
        Drink newDrink = new Drink.Builder()
                .setId(drink.getId())
                .setQuantity(drink.getQuantity())
                .setName(drink.getName())
                .setPrice(drink.getPrice())
                .build();
        newDrink.getAddons().addAll(drink.getAddons());
        return newDrink;
    }

}


    
        



