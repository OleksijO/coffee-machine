package coffee_machine.controller.impl.command;

import coffee_machine.controller.Command;
import coffee_machine.controller.RegExp;
import coffee_machine.controller.logging.ControllerErrorLogging;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.GeneralKey;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

import static coffee_machine.i18n.message.key.error.CommandErrorKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN;
import static coffee_machine.i18n.message.key.error.CommandErrorKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN;
import static coffee_machine.view.Attributes.ERROR_ADDITIONAL_MESSAGE;
import static coffee_machine.view.Attributes.ERROR_MESSAGE;
import static coffee_machine.view.PagesPaths.LOGIN_PAGE;

/**
 * This class represents login page main functionality handler command.
 * This is template for specific role user/admin login pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public abstract class AbstractLoginCommand implements Command, ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(AbstractLoginCommand.class);

    private static final Pattern PATTERN_EMAIL = Pattern.compile(RegExp.REGEXP_EMAIL);
    private static final Pattern PATTERN_PASSWORD = Pattern.compile(RegExp.REGEXP_PASSWORD);

    protected static final String TRY_FAILED_WRONG_EMAIL_OR_PASSWORD =
            "LOGIN TRY FAILED: no such combination of email and password.";

    protected static final String TRY_FAILED_WRONG_EMAIL = " LOGIN TRY FAILED: wrong email: ";
    protected static final String TRY_FAILED_WRONG_PASSWORD = " LOGIN TRY FAILED: password do not matches pattern.";
    protected static final String USER_LOGGED_IN = "USER id=%d LOGGED IN.";
    protected static final String ADMIN_LOGGED_IN = "ADMIN id=%d LOGGED IN.";

    /**
     *  This method determines common try-catch wrapper for child specific login commands
     * {@inheritDoc}
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /* common try-catch wrapper for specific login logic */
        try {

            return performExecute(request, response);

        } catch (ApplicationException e) {
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }

        return LOGIN_PAGE;
    }

    /**
     * This method should be overridden in childs and should perform specific logic of logging in
     *
     * @param request request instance
     * @param response responce instance
     * @return  Same as method execute()
     * @throws IOException in case of troubles with redirect
     */
    protected abstract String performExecute(HttpServletRequest request, HttpServletResponse response)
            throws IOException;


    protected boolean checkPassword(String password) {

        return checkToPattern(PATTERN_PASSWORD, password);
    }

    protected boolean checkLogin(String email) {
        return checkToPattern(PATTERN_EMAIL, email);
    }

    private boolean checkToPattern(Pattern pattern, String stringToCheck) {
        return (stringToCheck != null) && (pattern.matcher(stringToCheck).matches());
    }

    protected boolean processLoginForm(HttpServletRequest request, String email, String password){

        /* checking login and password field to match the patterns*/
        if (!checkLogin(email)) {
            request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN);
            logger.info(TRY_FAILED_WRONG_EMAIL + email);
            return false;
        }
        if (!checkPassword(password)) {
            request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN);
            logger.info(TRY_FAILED_WRONG_PASSWORD);
            return false;
        }
        return true;
    }


}
