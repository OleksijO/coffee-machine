package data.entity;

import coffee_machine.model.entity.goods.Addon;
import coffee_machine.model.entity.goods.Drink;

import java.util.*;


/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
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

    Drinks(int id, String name, long price, int quantity, Addon... addons) {
        drink = new Drink();
        drink.setId(id);
        drink.setName(name);
        drink.setPrice(price);
        drink.setQuantity(quantity);
        Set<Addon> drinkAddons = new HashSet<>();
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
        Set<Addon> addonSet=new HashSet<>();
        drink.getAddons().forEach(addon->addonSet.add(Addons.getCopyById(addon.getId())));
        newDrink.setAddons(addonSet);
        return newDrink;
    }

}


    
        



