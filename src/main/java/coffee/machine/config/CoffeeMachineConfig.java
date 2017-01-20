package coffee.machine.config;

import java.util.ResourceBundle;

/**
 * This class is a constant holder for main application configuration constants
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class CoffeeMachineConfig {
    private static ResourceBundle config = ResourceBundle.getBundle("coffee_machine_config");
    private static final String PROP_KEY_DATABASE_MONEY_COEFFICIENT = "database.money.coefficient";
    private static final String PROP_KEY_COFFEE_MACHINE_ACCOUNT_ID = "account.id";
    private static final String PROP_KEY_MESSAGES_BUNDLE = "messages.bundle";
    private static final String PROP_KEY_ADMIN_CONTACT_INFORMATION = "admin.contact.info";
    private static final String PROP_KEY_BALANCE_WARN_LIMIT =  "balance.warn.lower.limit";

    /**
     * Coffee Machine account ID. Important: should be placed in DB manually.
     */
    public static final int ACCOUNT_ID =
            Integer.parseInt(config.getString(PROP_KEY_COFFEE_MACHINE_ACCOUNT_ID));

    /**
     * Name of message resource bundle
     */
    public static final String MESSAGES =
            config.getString(PROP_KEY_MESSAGES_BUNDLE);

    /**
     * Relation coefficient of real money and the money value, stored in data base.
     * To get real amount one should multiply money value, stored in data base, on this coefficient
     * F.e.:
     *          (double) realMoney = (double) DB_MONEY_COEFF * (long)moneyFromDatabase;
     */
    public static final double DB_MONEY_COEFF =
            Double.parseDouble(config.getString(PROP_KEY_DATABASE_MONEY_COEFFICIENT));

    /**
     * Administrator contact information to be shown on view pages
     */
    public static final String ADMIN_CONTACT_INFO =
            config.getString(PROP_KEY_ADMIN_CONTACT_INFORMATION);

    /**
     * If user account amount is lower than this value one will be warn and offered to add credits.
     */
    public static final int BALANCE_WARN_LIMIT =
            Integer.parseInt(config.getString(PROP_KEY_BALANCE_WARN_LIMIT));

    private CoffeeMachineConfig() {
    }
}
