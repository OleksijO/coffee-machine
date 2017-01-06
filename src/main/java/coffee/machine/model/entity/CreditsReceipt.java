package coffee.machine.model.entity;

import static coffee.machine.config.CoffeeMachineConfig.DB_MONEY_COEFF;

/**
 * Created by oleksij.onysymchuk@gmail on 06.01.2017.
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
}
