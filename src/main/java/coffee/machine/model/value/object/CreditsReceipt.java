package coffee.machine.model.value.object;

import static coffee.machine.config.CoffeeMachineConfig.DB_MONEY_COEFF;

/**
 * This class represents value object for transfer data of adding credits and user
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class CreditsReceipt {
    private int userId;
    private long amount;

    private CreditsReceipt() {
    }

    public static class Builder {
        private CreditsReceipt receipt = new CreditsReceipt();

        public Builder setUserId(int userId) {
            receipt.userId = userId;
            return this;
        }

        public Builder setAmount(long amount) {
            receipt.amount = amount;
            return this;
        }

        public Builder setAmount(double realAmount) {
            receipt.amount = (long) (realAmount / DB_MONEY_COEFF);
            return this;
        }

        public CreditsReceipt build() {
            return receipt;
        }

    }

    public int getUserId() {
        return userId;
    }

    public long getAmount() {
        return amount;
    }

    public double getRealAmount() {
        return amount * DB_MONEY_COEFF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditsReceipt that = (CreditsReceipt) o;

        if (userId != that.userId) return false;
        return amount == that.amount;

    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "CreditsReceipt{" +
                "userId=" + userId +
                ", amount=" + amount +
                '}';
    }
}
