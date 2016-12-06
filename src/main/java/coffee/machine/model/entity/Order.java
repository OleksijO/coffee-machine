package coffee.machine.model.entity;

import coffee.machine.CoffeeMachineConfig;
import coffee.machine.model.entity.item.Drink;

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

    public Order(int userId, Date date, List<Drink> drinks, long amount) {
        this.userId = userId;
        this.date = date;
        this.drinks = drinks;
        this.amount = amount;
    }

    public double getRealAmount() {
        return CoffeeMachineConfig.DB_MONEY_COEFF * amount;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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
                ", amount=" + amount +
                '}';
    }
}
