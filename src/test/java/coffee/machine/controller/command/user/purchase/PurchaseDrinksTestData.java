package coffee.machine.controller.command.user.purchase;

import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.view.config.Parameters;

import java.util.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
enum PurchaseDrinksTestData {
    EMPTY_DATA(
            new HashMap<>(),
            new Order.Builder().setUserId(1).build()),

    PURCHASE_CORRECT_DATA(
            new TreeMap<String, String>() {{
                String drink = Parameters.DRINK_PARAMETER_STARTS_WITH;
                String addon = Parameters.ADDON_PARAMETER_STARTS_WITH;
                put(drink + 1, "0");
                put(drink + 1 + addon + 10, "0");
                put(drink + 1 + addon + 20, "1");
                put(drink + 2, "1");
                put(drink + 2 + addon + 10, "0");
                put(drink + 2 + addon + 20, "0");
                put(drink + 3, "2");
                put(drink + 3 + addon + 10, "1");
                put(drink + 3 + addon + 20, "0");
                put(drink + 4, "3");
                put(drink + 4 + addon + 10, "0");
                put(drink + 4 + addon + 20, "1");
                put(drink + 5, "0");
                put(drink + 6, "4");
                put(drink + 6 + addon + 10, "2");
                put(drink + 6 + addon + 20, "2");

            }},
            new Order.Builder()
                    .setDrinks(new ArrayList<Drink>() {
                        {
                            add(new Drink.Builder().setId(2).setQuantity(1).build());

                            List<Product> addons = new ArrayList<Product>() {{
                                add(new Product.Builder().setId(10).setQuantity(1).build());
                            }};
                            add(new Drink.Builder().setId(3).setQuantity(2).addAddons(addons).build());

                            addons = new ArrayList<Product>() {{
                                add(new Product.Builder().setId(20).setQuantity(1).build());
                            }};
                            add(new Drink.Builder().setId(4).setQuantity(3).addAddons(addons).build());

                            addons = new ArrayList<Product>() {{
                                add(new Product.Builder().setId(10).setQuantity(2).build());
                                add(new Product.Builder().setId(20).setQuantity(2).build());
                            }};
                            add(new Drink.Builder().setId(6).setQuantity(4).addAddons(addons).build());
                        }
                    })
                    .setUserId(1)
                    .build()
    ),

    PURCHASE_DATA_WITH_NEGATIVE_QUANTITIES(
            new TreeMap<String, String>() {{
                String drink = Parameters.DRINK_PARAMETER_STARTS_WITH;
                String addon = Parameters.ADDON_PARAMETER_STARTS_WITH;
                put(drink + 1, "0");
                put(drink + 1 + addon + 10, "0");
                put(drink + 1 + addon + 20, "1");
                put(drink + 2, "1");
                put(drink + 2 + addon + 10, "0");
                put(drink + 2 + addon + 20, "0");
                put(drink + 3, "2");
                put(drink + 3 + addon + 10, "1");
                put(drink + 3 + addon + 20, "0");
                put(drink + 4, "3");
                put(drink + 4 + addon + 10, "-1");
                put(drink + 4 + addon + 20, "1");
                put(drink + 5, "-1");
                put(drink + 6, "4");
                put(drink + 6 + addon + 10, "2");
                put(drink + 6 + addon + 20, "2");

            }},
            new Order.Builder()
                    .setDrinks(new ArrayList<Drink>() {
                        {
                            List<Product> addons = new ArrayList<Product>() {{
                                add(new Product.Builder().setId(10).setQuantity(0).build());
                                add(new Product.Builder().setId(20).setQuantity(1).build());
                            }};
                            add(new Drink.Builder().setId(1).setQuantity(0).addAddons(addons).build());

                            addons = new ArrayList<Product>() {{
                                add(new Product.Builder().setId(10).setQuantity(0).build());
                                add(new Product.Builder().setId(20).setQuantity(0).build());
                            }};
                            add(new Drink.Builder().setId(2).setQuantity(1).addAddons(addons).build());

                            addons = new ArrayList<Product>() {{
                                add(new Product.Builder().setId(10).setQuantity(1).build());
                                add(new Product.Builder().setId(20).setQuantity(0).build());
                            }};
                            add(new Drink.Builder().setId(3).setQuantity(2).addAddons(addons).build());

                            addons = new ArrayList<Product>() {{
                                add(new Product.Builder().setId(10).setQuantity(-1).build());
                                add(new Product.Builder().setId(20).setQuantity(1).build());
                            }};
                            add(new Drink.Builder().setId(4).setQuantity(3).addAddons(addons).build());

                            add(new Drink.Builder().setId(5).setQuantity(-1).build());

                            addons = new ArrayList<Product>() {{
                                add(new Product.Builder().setId(10).setQuantity(2).build());
                                add(new Product.Builder().setId(20).setQuantity(2).build());
                            }};
                            add(new Drink.Builder().setId(6).setQuantity(4).addAddons(addons).build());
                        }
                    })
                    .setUserId(1)
                    .build()
    );

    Map<String, String> requestParams;
    Order order;

    PurchaseDrinksTestData(Map<String, String> requestParams,
                           Order order) {
        this.requestParams = requestParams;

        this.order = order;
    }

}
