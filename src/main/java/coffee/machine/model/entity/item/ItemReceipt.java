package coffee.machine.model.entity.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oleksij.onysymchuk@gmail on 05.01.2017.
 */
public class ItemReceipt {
    private List<Item> addons = new ArrayList<>();
    private List<Drink> drinks = new ArrayList<>();

    public ItemReceipt() {
    }

    public ItemReceipt(List<Drink> drinks, List<Item> addons) {
        addAddons(addons);
        addDrinks(drinks);
    }

    public boolean isEmpty() {
        return addons.size() + drinks.size() == 0;
    }

    public ItemReceipt clearZeroItems() {
        addons = addons
                .stream()
                .filter(addon -> addon.getQuantity() != 0)
                .collect(Collectors.toList());
        drinks = drinks
                .stream()
                .filter(drink -> drink.getQuantity() != 0)
                .collect(Collectors.toList());
        return this;
    }

    public boolean hasNegativeQuantity() {
        return addons.stream().filter(addon -> addon.getQuantity() < 0).findAny().isPresent()
                || drinks.stream().filter(drink -> drink.getQuantity() < 0).findAny().isPresent();
    }

    public void addDrink(Drink drink) {
        drinks.add(drink);
    }

    public void addDrinks(List<Drink> drinks) {
        this.drinks.addAll(drinks);
    }

    public void addAddon(Item addon) {
        addons.add(addon);
    }

    public void addAddons(List<Item> addons) {
        this.addons.addAll(addons);
    }

    public List<Item> getAddons() {
        return addons;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }
}
