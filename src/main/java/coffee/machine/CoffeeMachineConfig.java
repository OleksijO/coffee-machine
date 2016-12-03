package coffee.machine;

/**
 * This class is a constant holder for main application configuration constants
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CoffeeMachineConfig {
    /**
     * Coffee Machine account ID. Important: should be placed in DB manually.
     */
    int ACCOUNT_ID = 1;

    /**
     * Name of message resource bundle
     */
    String MESSAGES = "i18n.messages";

    /**
     * Relation coefficient of real money and the money value, stored in data base.
     * To get real amount one should multiply money value, stored in data base, on this coefficient
     * F.e.:
     *          (double) realMoney = (double) DB_MONEY_COEFF * (long)moneyFromDatabase;
     */
    double DB_MONEY_COEFF = 0.01;
}
