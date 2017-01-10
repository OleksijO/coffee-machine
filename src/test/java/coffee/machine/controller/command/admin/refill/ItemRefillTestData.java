package coffee.machine.controller.command.admin.refill;

import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.value.object.ItemReceipt;
import coffee.machine.view.Parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
enum ItemRefillTestData {
    EMPTY_DATA(
            new HashMap<>(),
            new ItemReceipt(new ArrayList<Drink>(), new ArrayList<Item>())),

    REFILL_FULL_DATA(
            new HashMap<String, String>() {{
                String drink = Parameters.DRINK_PARAMETER_STARTS_WITH;
                String addon = Parameters.ADDON_PARAMETER_STARTS_WITH;
                put(drink + 1, "0");
                put(drink + 2, "1");
                put(drink + 3, "-1");
                put(drink + 4, "2");

                put(addon + 1, "0");
                put(addon + 2, "2");
                put(addon + 3, "-1");
                put(addon + 4, "6");
                put(addon + 5, "8");

            }},
            new ItemReceipt(
                    new ArrayList<Drink>() {{
                        add(new Drink.Builder().setId(1).setQuantity(0).build());
                        add(new Drink.Builder().setId(2).setQuantity(1).build());
                        add(new Drink.Builder().setId(3).setQuantity(-1).build());
                        add(new Drink.Builder().setId(4).setQuantity(2).build());
                    }},
                    new ArrayList<Item>() {{
                        add(new Item.Builder().setId(1).setQuantity(0).build());
                        add(new Item.Builder().setId(2).setQuantity(2).build());
                        add(new Item.Builder().setId(3).setQuantity(-1).build());
                        add(new Item.Builder().setId(4).setQuantity(6).build());
                        add(new Item.Builder().setId(5).setQuantity(8).build());
                    }})
    );


    Map<String, String> requestParams;
    ItemReceipt itemReceipt;

    ItemRefillTestData(Map<String, String> requestParams, ItemReceipt itemReceipt) {
        this.requestParams = requestParams;
        this.itemReceipt = itemReceipt;
    }
}
