package coffee_machine.model.entity;

import java.util.Date;

import static coffee_machine.CoffeeMachineConfig.DB_MONEY_COEFF;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public class HistoryRecord {
    private int id;
    private int userId;
    private Date date;
    private String orderDescription;
    private long amount;

    public HistoryRecord() {
    }

    public HistoryRecord(int userId, Date date, String orderDescription, long amount) {
        this.userId = userId;
        this.date = date;
        this.orderDescription = orderDescription;
        this.amount = amount;
    }

    public double getRealAmount() {
        return DB_MONEY_COEFF * amount;
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

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
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

        HistoryRecord that = (HistoryRecord) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (amount != that.amount) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return orderDescription != null ? orderDescription.equals(that.orderDescription) : that.orderDescription == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (orderDescription != null ? orderDescription.hashCode() : 0);
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "HistoryRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", orderDescription='" + orderDescription + '\'' +
                ", amount=" + amount +
                '}';
    }
}
