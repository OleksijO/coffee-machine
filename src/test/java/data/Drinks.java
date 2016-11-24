package data;

import coffee_machine.model.entity.goods.Addon;
import coffee_machine.model.entity.goods.Drink;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static data.Addons.*;


/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
public enum Drinks {
    WATER(1,"Вода", 100, 20),
    BORJOMI(2,"Боржоми", 500, 20),
    TEA_WITHOUT_SUGAR(3,"Чай без сахара", 500, 10,LEMON.addon),
    TEA_WITH_SUGAR(4,"Чай с сахаром", 600, 20,LEMON.addon, SUGAR.addon),
    ESPRESSO(6,"Эспрессо", 700, 50,SUGAR.addon,MILK.addon,CREAM.addon,CINNAMON.addon),
    AMERICANO(10,"Американо", 800, 150,SUGAR.addon,MILK.addon,CREAM.addon,CINNAMON.addon),
    MOCACCINO(11,"Мокачино", 1000, 50,SUGAR.addon,MILK.addon,CREAM.addon,CINNAMON.addon),
    LATTE(12,"Латте", 1200, 100,SUGAR.addon,CINNAMON.addon);

    public Drink drink;

    Drinks(int id, String name, long price, int quantity, Addon...addons) {
        drink=new Drink();
        drink.setId(id);
        drink.setName(name);
        drink.setPrice(price);
        drink.setQuantity(quantity);
        Set<Addon> drinkAddons = new HashSet<>();
        if (addons!=null){
            Collections.addAll(drinkAddons, addons);
        }
        drink.setAddons(drinkAddons);
    }
}

    
        



