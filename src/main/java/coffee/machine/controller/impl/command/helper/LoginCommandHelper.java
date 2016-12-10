package coffee.machine.controller.impl.command.helper;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.logging.ControllerErrorLogging;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.i18n.message.key.error.CommandErrorKey;
import coffee.machine.view.Attributes;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.LOGIN_PATH;
import static coffee.machine.view.Parameters.PASSWORD_PARAM;

/**
 * This class represents login page main functionality helper.
 * This is template for specific role user/admin login pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginCommandHelper implements ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(LoginCommandHelper.class);

    private static final Pattern PATTERN_EMAIL = Pattern.compile(RegExp.REGEXP_EMAIL);
    private static final Pattern PATTERN_PASSWORD = Pattern.compile(RegExp.REGEXP_PASSWORD);

    public static final String TRY_FAILED_WRONG_EMAIL_OR_PASSWORD =
            "LOGIN TRY FAILED: no such combination of email and password. Entered e-mail: ";

    public static final String USER_LOGGED_IN = "USER id=%d LOGGED IN.";
    public static final String ADMIN_LOGGED_IN = "ADMIN id=%d LOGGED IN.";


    public LoginFormData processLoginForm(HttpServletRequest request) {
        String email = request.getParameter(Parameters.LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        LoginFormData formData = new LoginFormData(email, password);

        if (!checkLogin(email)) {
            request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN);
            return formData;
        }

        if (!checkPassword(password)) {
            request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN);
            return formData;
        }

        formData.setValid(true);
        return formData;
    }

    boolean checkPassword(String password) {
        return checkToPattern(PATTERN_PASSWORD, password);
    }

    boolean checkLogin(String email) {
        return checkToPattern(PATTERN_EMAIL, email);
    }

    boolean checkToPattern(Pattern pattern, String stringToCheck) {
        return (stringToCheck != null) && (pattern.matcher(stringToCheck).matches());
    }

    public boolean isDoubleLoginAttempt(HttpServletRequest request) {
        if ((request.getSession().getAttribute(USER_ID) != null)
                || (request.getSession().getAttribute(ADMIN_ID) != null)) {
            request.setAttribute(ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN);
            return true;
        }
        return false;
    }

    public void performActionsToLogInRole(HttpServletRequest request,
                                          HttpServletResponse response,
                                          String logMessageFormat, int userId,
                                          String sessionAttribute,
                                          String redirectPath) throws IOException {
        logger.info(String.format(logMessageFormat, userId));
        request.getSession().setAttribute(sessionAttribute, userId);
        response.sendRedirect(redirectPath);
    }

    public void setGeneralLoginPageAttributes(HttpServletRequest request) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_LOGIN);
        request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_FORM_TITLE);
        request.setAttribute(Attributes.LOGIN_FORM_ACTION, LOGIN_PATH);
    }
}
