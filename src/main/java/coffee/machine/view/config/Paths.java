package coffee.machine.view.config;

/**
 * This class is a constant holder for application uri paths and jsp pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public final class Paths {

    public static final String USER = "/user";
    public static final String ADMIN = "/admin";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REGISTER = "/register";
    public static final String HOME_PATH = "/home";
    public static final String ORDERS_HISTORY = "/orders/history";
    public static final String PURCHASE = "/purchase";
    public static final String REFILL = "/refill";
    public static final String ADD_CREDIT = "/add_credits";

    public static final String REDIRECTED = "REDIRECTED";

    public static final String LOGIN_PATH = LOGIN;
    public static final String LOGOUT_PATH = LOGOUT;

    public static final String USER_REGISTER_PATH = USER + REGISTER;
    public static final String USER_ORDER_HISTORY_PATH = USER + ORDERS_HISTORY;
    public static final String USER_PURCHASE_PATH = USER + PURCHASE;

    public static final String USER_HOME_PATH = USER_PURCHASE_PATH;

    public static final String ADMIN_REFILL_PATH = ADMIN + REFILL;
    public static final String ADMIN_ADD_CREDITS_PATH = ADMIN + ADD_CREDIT;

    public static final String ADMIN_HOME_PATH = ADMIN_REFILL_PATH;

    private Paths() {
    }
}
