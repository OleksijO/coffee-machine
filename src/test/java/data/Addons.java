package data;

import coffee_machine.model.entity.goods.Addon;

import java.util.*;

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

    public static List<Addon> getAllAddons() {
        List<Addon> addons = new ArrayList<>();
        Arrays.stream(values()).forEach(record -> addons.add(record.addon));
        return addons;
    }

    public Addon getCopy() {
        Addon newAddon = new Addon();
        newAddon.setId(addon.getId());
        newAddon.setQuantity(addon.getQuantity());
        newAddon.setName(addon.getName());
        newAddon.setPrice(addon.getPrice());
        return newAddon;
    }

    public static Addon getCopyById(int id){
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


