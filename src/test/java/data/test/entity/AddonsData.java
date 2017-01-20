package data.test.entity;

import coffee.machine.model.entity.product.Product;
import coffee.machine.model.entity.product.ProductType;

/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum AddonsData {
    LEMON(5, "Лимон", 200, 20),
    SUGAR(7, "Дополнительный сахар", 100, 300),
    MILK(8, "Молоко", 200, 150),
    CREAM(9, "Сливки", 300, 150),
    CINNAMON(13, "Корица", 150, 75);


    public final Product addon;

    AddonsData(int id, String name, long price, int quantity) {
        addon = new Product.Builder(ProductType.ADDON)
                .setId(id)
                .setName(name)
                .setPrice(price)
                .setQuantity(quantity)
                .build();
    }

    public Product getCopy() {
        return new Product.Builder(addon.getType())
                .setId(addon.getId())
                .setQuantity(addon.getQuantity())
                .setName(addon.getName())
                .setPrice(addon.getPrice())
                .build();
    }
}


