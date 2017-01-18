package coffee.machine.model.entity;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents Order entity.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Order implements Identified{
    private int id;
    private int userId;
    private Date date;
    private List<Drink> drinks;
    private long totalCost;

    private Order() {
    }

    public double getRealTotalCost() {
        return CoffeeMachineConfig.DB_MONEY_COEFF * totalCost;
    }

    public void clearProductsWithZeroQuantity() {
        drinks = drinks.stream()
                .filter(drink -> drink.getQuantity() != 0)
                .collect(Collectors.toList());
        drinks.forEach(drink -> drink.setAddons(drink.getAddons().stream()
                .filter(addon -> addon.getQuantity() != 0)
                .collect(Collectors.toList())))
        ;
    }

    public boolean isEmpty() {
        return (drinks == null) || (drinks.isEmpty());
    }

    public boolean hasNegativeQuantity() {
        return drinks.stream()
                .filter(drink ->
                        (drink.getQuantity() < 0) ||
                                (drink.getAddons().stream()
                                        .filter(addon -> addon.getQuantity() < 0)
                                        .findAny()
                                        .isPresent()))
                .findAny()
                .isPresent();
    }

    public Order fillAbsentDrinkData(List<? extends Product> actualDrinks) {
        drinks.forEach(
                drink -> drink.fillAbsentData(
                        actualDrinks.stream()
                                .filter(d -> d.getId() == drink.getId())
                                .findFirst()
                                .orElseThrow(IllegalArgumentException::new)));
        return this;
    }

    public Order fillAbsentAddonData(List<Product> actualAddons) {
        drinks.stream()
                .flatMap(drink -> drink.getAddons().stream())
                .forEach(
                        addon -> addon.fillAbsentData(
                                actualAddons.stream()
                                        .filter(a -> a.getId() == addon.getId())
                                        .findFirst()
                                        .orElseThrow(IllegalArgumentException::new)));
        return this;
    }

    public Order setCurrentDate() {
        date = new Date();
        return this;
    }

    public Order calculateTotalCost() {
        totalCost = drinks.stream().mapToLong(drink -> drink.getTotalPrice()).sum();
        return this;
    }


    public void addDrink(Drink drink) {
        drinks.add(drink);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public long getTotalCost() {
        return totalCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order that = (Order) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (totalCost != that.totalCost) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return drinks != null ? drinks.equals(that.drinks) : that.drinks == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (drinks != null ? drinks.hashCode() : 0);
        result = 31 * result + (int) (totalCost ^ (totalCost >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", drinks='" + drinks + '\'' +
                ", amount=" + getRealTotalCost() +
                '}';
    }

    public Set<Integer> getDrinkIds() {
        return drinks.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

    public Set<Integer> getAddonIds() {
        return drinks.stream()
                .flatMap(drink ->
                        drink.getAddons().stream()
                                .map(Product::getId))
                .collect(Collectors.toSet());
    }


    public static class Builder {
        private Order order = new Order();

        public Builder setId(int id) {
            order.id = id;
            return this;
        }

        public Builder setUserId(int userId) {
            order.userId = userId;
            return this;
        }

        public Builder setDate(Date date) {
            order.date = date;
            return this;
        }

        public Builder setDrinks(List<Drink> drinks) {
            order.drinks = drinks;
            return this;
        }

        public Builder setTotalCost(long totalCost) {
            order.totalCost = totalCost;
            return this;
        }

        public Order build() {
            if (order.drinks == null) {
                order.drinks = new ArrayList<>();
            }
            return order;
        }
    }
}
