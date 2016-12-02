package coffee_machine.controller.impl.command;

import coffee_machine.controller.Command;
import coffee_machine.controller.RegExp;
import coffee_machine.controller.logging.ControllerErrorLogging;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static coffee_machine.i18n.message.key.error.CommandErrorKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN;
import static coffee_machine.i18n.message.key.error.CommandErrorKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN;
import static coffee_machine.view.Attributes.ERROR_MESSAGE;

/**
 * Created by oleksij.onysymchuk@gmail on 27.11.2016.
 */
public abstract class AbstractLoginCommand implements Command, ControllerErrorLogging {
    private static final Pattern PATTERN_EMAIL = Pattern.compile(RegExp.REGEXP_EMAIL);
    private static final Pattern PATTERN_PASSWORD = Pattern.compile(RegExp.REGEXP_PASSWORD);
    private final Logger logger;

    protected static final String TRY_FAILED_WRONG_EMAIL_OR_PASSWORD =
            "LOGIN TRY FAILED: no such combination of email and password.";

    protected static final String TRY_FAILED_WRONG_EMAIL = " LOGIN TRY FAILED: wrong email: ";
    protected static final String TRY_FAILED_WRONG_PASSWORD = " LOGIN TRY FAILED: password do not matches pattern.";
    protected static final String USER_LOGGED_IN = "USER id=%d LOGGED IN.";
    protected static final String ADMIN_LOGGED_IN = "ADMIN id=%d LOGGED IN.";

    public AbstractLoginCommand(Logger logger) {
        this.logger = logger;
    }

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
