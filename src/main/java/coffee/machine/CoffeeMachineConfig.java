package coffee.machine;

import java.util.ResourceBundle;

/**
 * This class is a constant holder for main application configuration constants
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CoffeeMachineConfig {
    ResourceBundle config = ResourceBundle.getBundle("coffee_machine_config");
    String PROP_KEY_DATABASE_MONEY_COEFFICIENT = "database.money.coefficient";
    String PROP_KEY_COFFEE_MACHINE_ACCOUNT_ID = "account.id";
    String PROP_KEY_MESSAGES_BUNDLE = "messages.bundle";
    String PROP_KEY_ADMIN_CONTACT_INFORMATION = "admin.contact.info";
    String PROP_KEY_BALANCE_WARN_LIMIT =  "balance.warn.lower.limit";

    /**
     * Coffee Machine account ID. Important: should be placed in DB manually.
     */
    int ACCOUNT_ID = Integer.parseInt(config.getString(PROP_KEY_COFFEE_MACHINE_ACCOUNT_ID));

    /**
     * Name of message resource bundle
     */
    String MESSAGES = config.getString(PROP_KEY_MESSAGES_BUNDLE);

    /**
     * Relation coefficient of real money and the money value, stored in data base.
     * To get real amount one should multiply money value, stored in data base, on this coefficient
     * F.e.:
     *          (double) realMoney = (double) DB_MONEY_COEFF * (long)moneyFromDatabase;
     */
    double DB_MONEY_COEFF = Double.parseDouble(config.getString(PROP_KEY_DATABASE_MONEY_COEFFICIENT));

    /**
     * Administrator contact information to be shown on view pages
     */
    String ADMIN_CONTACT_INFO = config.getString(PROP_KEY_ADMIN_CONTACT_INFORMATION);

    /**
     * If user account amount is lower than this value one will be warn and offered to add credits.
     */
    int BALANCE_WARN_LIMIT = Integer.parseInt(config.getString(PROP_KEY_BALANCE_WARN_LIMIT));
}
