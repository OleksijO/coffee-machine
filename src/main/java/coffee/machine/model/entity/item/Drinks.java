package coffee.machine.model.entity.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 06.01.2017.
 */
public class Drinks {
    private List<Drink> drinks = new ArrayList<>();

    public Drinks add(List<Drink> drinks) {
        this.drinks.addAll(drinks);
        return this;
    }

    public void incrementQuantities(List<Drink> drinksToAdd) {
        for (Drink drink : drinksToAdd) {
            drinks.stream()
                    .filter(item -> item.getId() == drink.getId())
                    .findFirst()
                    .orElseThrow(IllegalStateException::new)
                    .incrementQuantityBy(drink.getQuantity());
        }
    }


    public List<Drink> getDrinks() {
        return drinks;
    }
}
