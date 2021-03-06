package coffee.machine.view.config;

/**
 * This class is a constant holder for attribute names, used in java classes and jsp view pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public final class Attributes {
    public static final String PAGE_TITLE = "page_title";
    public static final String USER_ID = "user_id";
    public static final String USER_ROLE = "user_role";
    public static final String USER_LOCALE = "javax.servlet.jsp.jstl.fmt.locale.session";
    public static final String DRINKS = "drinks";
    public static final String ADDONS = "addons";

    public static final String PREVIOUS_ENTERED_EMAIL = "previous_entered_email";
    public static final String PREVIOUS_ENTERED_FULL_NAME = "previous_entered_full_name";

    public static final String USUAL_MESSAGE = "usual_message";
    public static final String ERROR_MESSAGE = "error_message";
    public static final String VALIDATION_ERRORS = "validation_errors";

    public static final String USUAL_ADDITIONAL_MESSAGE = "usual_additional_message";
    public static final String ERROR_ADDITIONAL_MESSAGE = "error_additional_message";

    public static final String USER_BALANCE = "user_balance";

    public static final String ORDER = "order";

    public static final String COFFEE_MACHINE_BALANCE = "coffee_machine_balance";
    public static final String USER_ORDERS = "USER_ORDERS";

    public static final String LOGIN_FORM_TITLE = "login_form_title";
    public static final String LOGIN_FORM_ACTION = "login_form_action";

    public static final String REGISTER_FORM_TITLE = "register_form_title";
    public static final String REGISTER_FORM_ACTION = "login_from_action";

    public static final String BUNDLE_FILE = "bundle_file";
    public static final String USER_LIST = "add_credit_user_list";

    public static final String PREVIOUS_VALUES_TABLE = "previous_values_table";

    public static final String ADMIN_CONTACTS = "admin_contacts";

    public static final String BALANCE_LOW_WARN_LIMIT = "balance_warn_limit";
    public static final String CURRENT_PAGE = "current_page";
    public static final String LAST_PAGE = "last_page";

    private Attributes() {
    }
}
