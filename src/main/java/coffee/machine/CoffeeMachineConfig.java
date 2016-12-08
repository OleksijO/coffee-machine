package coffee.machine;

import java.util.ResourceBundle;

/**
 * This class is a constant holder for main application configuration constants
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CoffeeMachineConfig {
    ResourceBundle config = ResourceBundle.getBundle("coffee_machine_config");
    String DATABASE_MONEY_COEFFICIENT = "database.money.coefficient";
    String COFFEE_MACHINE_ACCOUNT_ID = "account.id";
    String MESSAGES_BUNDLE = "messages.bundle";

    /**
     * Coffee Machine account ID. Important: should be placed in DB manually.
     */
    int ACCOUNT_ID = Integer.parseInt(config.getString(COFFEE_MACHINE_ACCOUNT_ID));

    /**
     * Name of message resource bundle
     */
    String MESSAGES = config.getString(MESSAGES_BUNDLE);

    /**
     * Relation coefficient of real money and the money value, stored in data base.
     * To get real amount one should multiply money value, stored in data base, on this coefficient
     * F.e.:
     *          (double) realMoney = (double) DB_MONEY_COEFF * (long)moneyFromDatabase;
     */
    double DB_MONEY_COEFF = Double.parseDouble(config.getString(DATABASE_MONEY_COEFFICIENT));
}
