package coffee.machine.model.value.object;

import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oleksij.onysymchuk@gmail on 05.01.2017.
 */
public class ProductsReceipt {
    private List<Product> addons = new ArrayList<>();
    private List<Drink> drinks = new ArrayList<>();

    public ProductsReceipt() {
    }

    public ProductsReceipt(List<Drink> drinks, List<Product> addons) {
        addAddons(addons);
        addDrinks(drinks);
    }

    public boolean isEmpty() {
        return isEmpty(drinks) && isEmpty(addons);
    }

    private boolean isEmpty(List<? extends Product> products) {
        return !products.stream()
                .filter(product -> product.getQuantity() != 0)
                .findAny()
                .isPresent();
    }

    public ProductsReceipt clearProductsWithZeroQuantity() {
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

    public void addAddon(Product addon) {
        addons.add(addon);
    }

    public void addAddons(List<Product> addons) {
        this.addons.addAll(addons);
    }

    public List<Product> getAddons() {
        return addons;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductsReceipt that = (ProductsReceipt) o;

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
        return "ProductsReceipt{" +
                "addons=" + addons +
                ", drinks=" + drinks +
                '}';
    }
}
