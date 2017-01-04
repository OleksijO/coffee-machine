package coffee.machine.model.entity;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.model.entity.item.Drink;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents Order entity.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Order {
    private int id;
    private int userId;
    private Date date;
    private List<Drink> drinks;
    private long amount;

    public Order() {
    }

    public double getRealAmount() {
        return CoffeeMachineConfig.DB_MONEY_COEFF * amount;
    }

    public void addDrink(Drink drink){
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
        return date;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order that = (Order) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (amount != that.amount) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return drinks != null ? drinks.equals(that.drinks) : that.drinks == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (drinks != null ? drinks.hashCode() : 0);
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", drinks='" + drinks + '\'' +
                ", amount=" + getRealAmount() +
                '}';
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

        public Builder setAmount(long amount) {
            order.amount = amount;
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
