package coffee_machine.controller.command;

public interface Pages {
	String JSP = ".jsp";
	String VIEW_CLASSPATH = "/WEB-INF/view";
	String USER = "/user";
	String ADMIN = "/admin";
	String LOGIN = "/login";
	String HOME_PATH = "/home";
	String USER_LOGIN_PATH = USER + LOGIN;
	String ADMIN_LOGIN_PATH = ADMIN + LOGIN;
	String HOME_PAGE = VIEW_CLASSPATH + "/homePage" + JSP;
	String USER_LOGIN_PAGE = VIEW_CLASSPATH + USER_LOGIN_PATH + JSP;
	String ADMIN_LOGIN_PAGE = VIEW_CLASSPATH + ADMIN_LOGIN_PATH + JSP;

}
