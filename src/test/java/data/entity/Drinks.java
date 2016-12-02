package data.entity;

import coffee_machine.model.entity.item.Item;
import coffee_machine.model.entity.item.Drink;
import coffee_machine.model.entity.item.ItemType;

import java.util.*;


/**
 * @author oleksij.onysymchuk@gmail.com 24.11.2016.
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
        drink = new Drink();
        drink.setId(id);
        drink.setName(name);
        drink.setPrice(price);
        drink.setType(ItemType.DRINK);
        drink.setQuantity(quantity);
        Set<Item> drinkAddons = new HashSet<>();
        if (addons != null) {
            Collections.addAll(drinkAddons, addons);
        }
        drink.setAddons(drinkAddons);
    }

    public static List<Drink> getAllDrinks() {
        List<Drink> drinks = new ArrayList<>();
        Arrays.stream(values()).forEach(record -> drinks.add(record.drink));
        return drinks;
    }

    public static Map<Integer, Integer> getQuantitiesByIds() {
        Map<Integer, Integer> initialQuantitiesById = new HashMap<>();
        Arrays.stream(values()).forEach(record -> initialQuantitiesById.put(record.drink.getId(),record.drink.getQuantity()));
        return initialQuantitiesById;
    }

    public Drink getCopy() {
        Drink newDrink = new Drink();
        newDrink.setId(drink.getId());
        newDrink.setQuantity(drink.getQuantity());
        newDrink.setName(drink.getName());
        newDrink.setPrice(drink.getPrice());
        Set<Item> addonSet=new HashSet<>();
        drink.getAddons().forEach(addon->addonSet.add(Addons.getCopyById(addon.getId())));
        newDrink.setAddons(addonSet);
        return newDrink;
    }

}


    
        



