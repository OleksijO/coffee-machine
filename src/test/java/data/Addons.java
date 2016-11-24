package data;

import coffee_machine.model.entity.goods.Addon;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
public enum Addons {
    LEMON(5, "Лимон", 200, 20),
    SUGAR(7, "Дополнительный сахар", 100, 300),
    MILK(8, "Молоко", 200, 150),
    CREAM(9, "Сливки", 300, 150),
    CINNAMON(13, "Корица", 150, 75);


    public Addon addon;

    Addons(int id, String name, long price, int quantity) {
        addon = new Addon();
        addon.setId(id);
        addon.setName(name);
        addon.setPrice(price);
        addon.setQuantity(quantity);
    }
}


