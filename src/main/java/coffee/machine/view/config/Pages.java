package coffee.machine.view.config;

import static coffee.machine.view.config.Paths.*;

/**
 * This class is a constant holder for application uri paths and jsp pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public final class Pages {

    private static final String JSP = ".jsp";
    public static final String VIEW_JSP_CLASSPATH = "/WEB-INF/view/jsp";
    private static final String PAGE = "Page" + JSP;

    public static final String HOME_PAGE = VIEW_JSP_CLASSPATH + HOME_PATH + PAGE;
    public static final String LOGIN_PAGE = VIEW_JSP_CLASSPATH + LOGIN + PAGE;

    public static final String USER_REGISTER_PAGE = VIEW_JSP_CLASSPATH + USER + REGISTER + PAGE;
    public static final String USER_REGISTER_SUCCESS_PAGE = VIEW_JSP_CLASSPATH + USER + REGISTER + "Success" + PAGE;
    public static final String USER_PURCHASE_PAGE = VIEW_JSP_CLASSPATH + USER + PURCHASE + PAGE;
    public static final String USER_ORDER_HISTORY_PAGE = VIEW_JSP_CLASSPATH + USER + "/ordersHistory" + PAGE;

    public static final String ADMIN_REFILL_PAGE = VIEW_JSP_CLASSPATH + ADMIN_REFILL_PATH + PAGE;
    public static final String ADMIN_ADD_CREDITS_PAGE = VIEW_JSP_CLASSPATH + ADMIN + "/addCredits" + PAGE;

    private Pages() {
    }
}
