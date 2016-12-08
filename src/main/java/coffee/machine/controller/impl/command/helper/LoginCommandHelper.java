package coffee.machine.controller.impl.command.helper;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.logging.ControllerErrorLogging;
import coffee.machine.i18n.message.key.error.CommandErrorKey;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

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
            "LOGIN_PARAM TRY FAILED: no such combination of email and password.";

    private static final String TRY_FAILED_WRONG_EMAIL = " LOGIN_PARAM TRY FAILED: wrong email: ";
    private static final String TRY_FAILED_WRONG_PASSWORD = " LOGIN_PARAM TRY FAILED: password do not matches pattern.";
    public static final String USER_LOGGED_IN = "USER id=%d LOGGED IN.";
    public static final String ADMIN_LOGGED_IN = "ADMIN id=%d LOGGED IN.";


    public boolean processLoginForm(HttpServletRequest request, String email, String password) {

        // checking login and password field to match the patterns
        if (!checkLogin(email)) {
            request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN);
            logger.info(TRY_FAILED_WRONG_EMAIL + email);
            return false;
        }
        if (!checkPassword(password)) {
            request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN);
            logger.info(TRY_FAILED_WRONG_PASSWORD);
            return false;
        }
        return true;
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


}
