package coffee.machine.controller.command.login;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.logging.ControllerErrorLogging;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
            "LOGIN TRY FAILED: no such combination of email and password. Entered e-mail: ";

    public static final String USER_LOGGED_IN = "USER id=%d LOGGED IN.";
    public static final String ADMIN_LOGGED_IN = "ADMIN id=%d LOGGED IN.";




    public boolean checkPassword(String password) {
        return checkToPattern(PATTERN_PASSWORD, password);
    }

    public boolean checkLogin(String email) {
        return checkToPattern(PATTERN_EMAIL, email);
    }

    public boolean checkToPattern(Pattern pattern, String stringToCheck) {
        return (stringToCheck != null) && (pattern.matcher(stringToCheck).matches());
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

}
