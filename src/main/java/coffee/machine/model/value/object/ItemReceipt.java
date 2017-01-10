package coffee.machine.model.value.object;

import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemReceipt that = (ItemReceipt) o;

        if (addons != null ? !addons.equals(that.addons) : that.addons != null) return false;
        return drinks != null ? drinks.equals(that.drinks) : that.drinks == null;

    }

    @Override
    public int hashCode() {
        int result = addons != null ? addons.hashCode() : 0;
        result = 31 * result + (drinks != null ? drinks.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ItemReceipt{" +
                "addons=" + addons +
                ", drinks=" + drinks +
                '}';
    }
}
