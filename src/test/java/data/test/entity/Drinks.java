package data.test.entity;

import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;

import java.util.*;


/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum Drinks {
    WATER(1, "Вода", 100, 20),
    BORJOMI(2, "Боржоми", 500, 20),
    TEA_WITHOUT_SUGAR(3, "Чай без сахара", 500, 10, Addons.LEMON.getCopy()),
    TEA_WITH_SUGAR(4, "Чай с сахаром", 600, 20, Addons.LEMON.getCopy(), Addons.SUGAR.getCopy()),
    ESPRESSO(6, "Эспрессо", 700, 50, Addons.SUGAR.getCopy(), Addons.MILK.getCopy(), Addons.CREAM.getCopy(), Addons.CINNAMON.getCopy()),
    AMERICANO(10, "Американо", 800, 150, Addons.SUGAR.getCopy(), Addons.MILK.getCopy(), Addons.CREAM.getCopy(), Addons.CINNAMON.getCopy()),
    MOCACCINO(11, "Мокачино", 1000, 50, Addons.SUGAR.getCopy(), Addons.MILK.getCopy(), Addons.CREAM.getCopy(), Addons.CINNAMON.getCopy()),
    LATTE(12, "Латте", 1200, 100, Addons.SUGAR.getCopy(), Addons.CINNAMON.getCopy());

    public final Drink drink;

    Drinks(int id, String name, long price, int quantity, Item... addons) {
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

    public static List<Drink> getAllDrinks() {
        List<Drink> drinks = new ArrayList<>();
        Arrays.stream(values()).forEach(record -> drinks.add(record.drink));
        return drinks;
    }

    public static Map<Integer, Integer> getQuantitiesByIds() {
        Map<Integer, Integer> initialQuantitiesById = new HashMap<>();
        Arrays.stream(values()).forEach(record -> initialQuantitiesById.put(record.drink.getId(), record.drink.getQuantity()));
        return initialQuantitiesById;
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


    
        



