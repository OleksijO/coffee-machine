package data.test.entity;

import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemType;

import java.util.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public enum Addons {
    LEMON(5, "Лимон", 200, 20),
    SUGAR(7, "Дополнительный сахар", 100, 300),
    MILK(8, "Молоко", 200, 150),
    CREAM(9, "Сливки", 300, 150),
    CINNAMON(13, "Корица", 150, 75);


    public final Item addon;

    Addons(int id, String name, long price, int quantity) {
        addon = new Item();
        addon.setId(id);
        addon.setName(name);
        addon.setType(ItemType.ADDON);
        addon.setPrice(price);
        addon.setQuantity(quantity);
    }

    public static List<Item> getAllAddons() {
        List<Item> addons = new ArrayList<>();
        Arrays.stream(values()).forEach(record -> addons.add(record.addon));
        return addons;
    }

    public Item getCopy() {
        Item newAddon = new Item();
        newAddon.setId(addon.getId());
        newAddon.setQuantity(addon.getQuantity());
        newAddon.setName(addon.getName());
        newAddon.setPrice(addon.getPrice());
        return newAddon;
    }

    public static Item getCopyById(int id){
        for (Addons rec:values()){
            if (rec.addon.getId()==id){
                return rec.getCopy();
            }
        }
        return null;
    }

    public static Map<Integer,Integer> getQuantitiesByIds() {
        Map<Integer, Integer> initialQuantitiesById = new HashMap<>();
        Arrays.stream(values()).forEach(record -> initialQuantitiesById.put(record.addon.getId(),record.addon.getQuantity()));
        return initialQuantitiesById;
    }
}


