package coffee.machine.model.entity;

import static coffee.machine.config.CoffeeMachineConfig.DB_MONEY_COEFF;

/**
 * This class represents Account entity.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Account {
    private int id;
    private long amount;

    public Account withdraw(long amount) {
        this.amount -= amount;
        return this;
    }

    public Account add(long amount) {
        this.amount += amount;
        return this;
    }

    public long getAmount() {
        return amount;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (amount ^ (amount >>> 32));
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (amount != other.amount)
            return false;
        return id == other.id;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", amount=" + amount + "]";
    }

    public static class Builder {
        private Account account = new Account();

        public Builder setId(int id) {
            account.id = id;
            return this;
        }

        public Builder setAmount(long amount) {
            account.amount = amount;
            return this;
        }

        public Account build() {
            return account;
        }

    }
}
