package coffee.machine.controller.impl.command.request.data.extractor.impl;

import coffee.machine.view.Parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
enum TestData {
    EMPTY_TEST(
            new HashMap<String, String>(),
            new HashMap<Integer, Integer>(),
            new HashMap<Integer, Integer>(),
            new HashMap<Integer, Map<Integer, Integer>>()
    ),

    FULL_TEST(
            new HashMap<String, String>() {{
                int drinkId = 1;
                String drink = Parameters.DRINK_PARAMETER_STARTS_WITH;
                String addon = Parameters.ADDON_PARAMETER_STARTS_WITH;
                put(drink + drinkId, "0");
                put(drink + drinkId + addon + 1, "0");
                put(drink + drinkId++ + addon + 2, "1");
                put(drink + drinkId, "1");
                put(drink + drinkId + addon + 1, "0");
                put(drink + drinkId++ + addon + 2, "0");
                put(drink + drinkId, "2");
                put(drink + drinkId + addon + 1, "1");
                put(drink + drinkId++ + addon + 2, "0");
                put(drink + drinkId, "3");
                put(drink + drinkId + addon + 1, "0");
                put(drink + drinkId++ + addon + 2, "1");
                put(drink + drinkId, "4");
                put(drink + drinkId + addon + 1, "2");
                put(drink + drinkId + addon + 2, "2");
                int addonId = 1;
                put(addon + addonId++, "0");
                put(addon + addonId++, "2");
                put(addon + addonId++, "4");
                put(addon + addonId++, "6");
                put(addon + addonId++, "8");

            }},
            new HashMap<Integer, Integer>() {{
                put(2,1);
                put(3,2);
                put(4,3);
                put(5,4);
            }},
            new HashMap<Integer, Integer>(){{
                put(2,2);
                put(3,4);
                put(4,6);
                put(5,8);
            }},
            new HashMap<Integer, Map<Integer, Integer>>() {{
                put(1, new HashMap<Integer, Integer>() {{
                    put(2, 1);
                }});
                put(3, new HashMap<Integer, Integer>() {{
                    put(1, 1);
                }});
                put(4, new HashMap<Integer, Integer>() {{
                    put(2, 1);
                }});
                put(5, new HashMap<Integer, Integer>() {{
                    put(1, 2);
                    put(2, 2);
                }});
            }}
    );


    Map<String, String> requestParams;
    Map<Integer, Integer> drinksQuantity;
    Map<Integer, Integer> addonsQuantity;
    Map<Integer, Map<Integer, Integer>> addonsByDrinkIdQuantity;

    TestData(Map<String, String> requestParams,
             Map<Integer, Integer> drinksQuantity,
             Map<Integer, Integer> addonsQuantity,
             Map<Integer, Map<Integer, Integer>> addonsByDrinkIdQuantity) {
        this.requestParams = requestParams;
        this.drinksQuantity = drinksQuantity;
        this.addonsQuantity = addonsQuantity;
        this.addonsByDrinkIdQuantity = addonsByDrinkIdQuantity;
    }

}
