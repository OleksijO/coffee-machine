package coffee.machine.controller.command.login;

import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.model.entity.LoginData;
import coffee.machine.model.entity.User;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee.machine.controller.command.login.LoginCommandHelper.ADMIN_LOGGED_IN;
import static coffee.machine.controller.command.login.LoginCommandHelper.USER_LOGGED_IN;
import static coffee.machine.i18n.message.key.error.CommandErrorKey.ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.*;
import static coffee.machine.view.Parameters.PASSWORD_PARAM;

/**
 * This class represents user login post request page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginSubmitCommand extends CommandWrapperTemplate {
    private static final Logger logger = Logger.getLogger(LoginSubmitCommand.class);

    private UserService userService = UserServiceImpl.getInstance();

    public LoginSubmitCommand() {
        super(LOGIN_PAGE);
    }

    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        LoginData loginData = getLoginDataFromRequest(request);
        saveFormDataToRequest(request, loginData);

        if (isDoubleLoginAttempt(request.getSession())) {
            logAndPlaceErrorMessageToRequest(ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN, request);
            return LOGIN_PAGE;
        }

        User user = userService.getUserByLoginData(loginData);
        performActionsToLogIn(request, response, user);
        return REDIRECTED;
    }


    private LoginData getLoginDataFromRequest(HttpServletRequest request) {
        String email = request.getParameter(Parameters.LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        return new LoginData(email, password);
    }

    private void saveFormDataToRequest(HttpServletRequest request, LoginData loginData) {

        request.setAttribute(PREVIOUS_ENTERED_EMAIL, loginData.getEmail());
    }

    private boolean isDoubleLoginAttempt(HttpSession session) {
        return (session.getAttribute(USER_ID) != null)
                || (session.getAttribute(ADMIN_ID) != null);
    }

    private void logAndPlaceErrorMessageToRequest(String messageKey, HttpServletRequest request) {
        logApplicationError(logger, messageKey);
        request.setAttribute(ERROR_MESSAGE, messageKey);
    }


    private void performActionsToLogIn(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        if (user.isAdmin()) {
            request.getSession().setAttribute(ADMIN_ID, user.getId());
            logUserDetails(ADMIN_LOGGED_IN, user.getId());
            response.sendRedirect(ADMIN_HOME_PATH);
        } else {
            request.getSession().setAttribute(USER_ID, user.getId());
            logUserDetails(USER_LOGGED_IN, user.getId());
            response.sendRedirect(USER_HOME_PATH);
        }
    }


    private void logUserDetails(String logMessageFormat, int userId) {
        logger.info(String.format(logMessageFormat, userId));
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_LOGIN);
        request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_FORM_TITLE);
        request.setAttribute(Attributes.LOGIN_FORM_ACTION, LOGIN_PATH);
    }
}


