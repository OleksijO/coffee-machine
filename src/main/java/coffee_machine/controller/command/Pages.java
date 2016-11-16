package coffee_machine.controller.command;

public interface Pages {
    String JSP = ".jsp";
    String SUCCESS = "_success";
    String FAILED = "_failed";
    String VIEW_CLASSPATH = "/WEB-INF/view";
    String USER = "/user";
    String ADMIN = "/admin";
    String LOGIN = "/login";
    String HOME_PATH = "/home";
    String USER_LOGIN_PATH = USER + LOGIN;
    String ADMIN_LOGIN_PATH = ADMIN + LOGIN;
    String HOME_PAGE = VIEW_CLASSPATH + "/homePage" + JSP;
    String USER_LOGIN_PAGE = VIEW_CLASSPATH + USER_LOGIN_PATH + JSP;
    String USER_LOGIN_SUCCESS_PAGE = VIEW_CLASSPATH + USER_LOGIN_PATH + SUCCESS + JSP;
    String USER_LOGIN_FAILED_PAGE = VIEW_CLASSPATH + USER_LOGIN_PATH + FAILED + JSP;
    String ADMIN_LOGIN_PAGE = VIEW_CLASSPATH + ADMIN_LOGIN_PATH + JSP;
    String ADMIN_LOGIN_SUCCESS_PAGE = VIEW_CLASSPATH + ADMIN_LOGIN_PATH + SUCCESS + JSP;
    String ADMIN_LOGIN_FAILED_PAGE = VIEW_CLASSPATH + ADMIN_LOGIN_PATH + FAILED + JSP;
    String REFILL = "/refill";
    String REFILLED = "/refilled";
    String REFILL_PATH = ADMIN + REFILL;
    String REFILL_PAGE = REFILL_PATH + JSP;
    String REFILLED_PAGE = ADMIN + REFILLED + JSP;

}
