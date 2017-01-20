package coffee.machine.controller.command.login;

import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.helper.LoggingHelper;
import coffee.machine.controller.validation.LoginDataValidator;
import coffee.machine.controller.validation.Notification;
import coffee.machine.controller.validation.Validator;
import coffee.machine.model.entity.user.User;
import coffee.machine.model.entity.user.UserRole;
import coffee.machine.model.value.object.user.LoginData;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_FOR_LOGIN_FORM;
import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_LOGIN;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN;
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
    private LoggingHelper loggingHelper = new LoggingHelper();

    private static final String LOG_MESSAGE_FORMAT_USER_LOGGED_IN_DETAILS = "%s id=%d LOGGED IN.";
    private static final String LOG_MESSAGE_DOUBLE_LOGIN_ATTEMPT_DETAILS = "Double login attempt. Details: ";

    private Map<UserRole, String> afterLoginPathByRole = new HashMap<>() ;
    {
        afterLoginPathByRole.put(UserRole.USER, USER_HOME_PATH);
        afterLoginPathByRole.put(UserRole.ADMIN, ADMIN_HOME_PATH);
    }

    private UserService userService = UserServiceImpl.getInstance();

    private Validator<LoginData> loginDataValidator = new LoginDataValidator();

    public LoginSubmitCommand() {
        super(LOGIN_PAGE);
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_LOGIN);
        request.setAttribute(LOGIN_FORM_TITLE, TITLE_FOR_LOGIN_FORM);
        request.setAttribute(LOGIN_FORM_ACTION, LOGIN_PATH);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginData loginData = getLoginDataFromRequest(request);
        saveFormData(loginData, request);

        Notification notification = loginDataValidator.validate(loginData);
        if (notification.hasErrorMessages()) {
            processValidationErrors(notification, request);
            return LOGIN_PAGE;
        }

        if (isDoubleLoginAttempt(request.getSession())) {
            logDetails(request, loginData);
            placeErrorMessageToRequest(request);
            return LOGIN_PAGE;
        }

        User user = userService.getUserByLoginData(loginData);
        performActionsToLogIn(request, response, user);

        clearFormData(request);
        return REDIRECTED;
    }

    private LoginData getLoginDataFromRequest(HttpServletRequest request) {
        String email = request.getParameter(Parameters.LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        return new LoginData(email, password);
    }

    private void saveFormData(LoginData loginData, HttpServletRequest request) {
        request.setAttribute(PREVIOUS_ENTERED_EMAIL, loginData.getEmail());
    }

    private boolean isDoubleLoginAttempt(HttpSession session) {
        return session.getAttribute(USER_ID) != null;
    }

    private void logDetails(HttpServletRequest request, LoginData loginData) {
        logger.error(LOG_MESSAGE_DOUBLE_LOGIN_ATTEMPT_DETAILS +
                loginData +
                loggingHelper.buildLogMessage(request));
    }

    private void placeErrorMessageToRequest(HttpServletRequest request) {
        request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN);
    }


    private void performActionsToLogIn(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        request.getSession().setAttribute(USER_ID, user.getId());
        request.getSession().setAttribute(USER_ROLE, user.getRole());
        response.sendRedirect(request.getContextPath() + afterLoginPathByRole.get(user.getRole()));
        logUserDetails(user);
    }


    private void logUserDetails(User user) {
        logger.info(
                String.format(
                        LOG_MESSAGE_FORMAT_USER_LOGGED_IN_DETAILS,
                        user.getRole(), user.getId()));
    }

    private void clearFormData(HttpServletRequest request) {
        request.removeAttribute(PREVIOUS_ENTERED_EMAIL);
    }
}


