package coffee_machine.controller;

public interface PagesPaths {
    String JSP = ".jsp";
    String VIEW_CLASSPATH = "/WEB-INF/view";
    String USER = "/user";
    String ADMIN = "/admin";
    String LOGIN = "/login";
    String LOGOUT = "/logout";
    String HOME_PATH = "/home";
    String SUBMIT = "/submit";
    String HISTORY = "/history";
    String REDIRECTED = "REDIRECTED";
    String USER_LOGIN_PATH = USER + LOGIN;
    String USER_LOGOUT_PATH = USER + LOGOUT;
    String USER_LOGIN_SUBMIT_PATH = USER + LOGIN + SUBMIT;
    String USER_HISTORY_PATH = USER + HISTORY;

    String ADMIN_LOGIN_PATH = ADMIN + LOGIN;
    String ADMIN_LOGOUT_PATH = ADMIN + LOGOUT;
    String ADMIN_LOGIN_SUBMIT_PATH = ADMIN + LOGIN + SUBMIT;
    String PAGE = "Page" + JSP;
    String HOME_PAGE = VIEW_CLASSPATH + HOME_PATH + PAGE;
    String USER_HOME_PATH = USER + HOME_PATH;
    String USER_HOME_PAGE = VIEW_CLASSPATH + USER_HOME_PATH + PAGE;
    String ADMIN_HOME_PATH = ADMIN + HOME_PATH;
    String ADMIN_HOME_PAGE = VIEW_CLASSPATH + ADMIN_HOME_PATH + PAGE;
    String USER_HISTORY_PAGE = VIEW_CLASSPATH + USER + HISTORY + PAGE;
    String USER_LOGIN_PAGE = VIEW_CLASSPATH + USER_LOGIN_PATH + PAGE;
    String ADMIN_LOGIN_PAGE = VIEW_CLASSPATH + ADMIN_LOGIN_PATH + PAGE;
    String REFILL = "/refill";
    String ADMIN_REFILL_PATH = ADMIN + REFILL;
    String ADMIN_REFILL_SUBMIT_PATH = ADMIN + SUBMIT + REFILL;
    String ADMIN_REFILL_PAGE = VIEW_CLASSPATH + ADMIN_REFILL_PATH + PAGE;
    String HEADER = VIEW_CLASSPATH + "/fragment/header" + JSP;
    String FOOTER = VIEW_CLASSPATH + "/fragment/footer" + JSP;

}

