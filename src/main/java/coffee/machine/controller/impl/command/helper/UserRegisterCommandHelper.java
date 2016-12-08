package coffee.machine.controller.impl.command.helper;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.logging.ControllerErrorLogging;
import coffee.machine.i18n.message.key.error.CommandErrorKey;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * This class represents register page main functionality helper.
 * This is template for specific role user/admin login pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserRegisterCommandHelper implements ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(UserRegisterCommandHelper.class);

    private static final Pattern PATTERN_FULL_NAME = Pattern.compile(RegExp.REGEXP_FULL_NAME);

    private LoginCommandHelper loginHelper = new LoginCommandHelper();


    public boolean processRegisterForm(HttpServletRequest request, String email, String password, String fullName) {

        // checking login, password and full name fields to match the patterns
        if (!loginHelper.checkLogin(email)) {
            request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN);
            return false;
        }
        if (!loginHelper.checkPassword(password)) {
            request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN);
            return false;
        }
        if (!checkFullName(fullName)) {
            request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN);
            return false;
        }
        return true;
    }

    private boolean checkFullName(String fullName) {
        return loginHelper.checkToPattern(PATTERN_FULL_NAME, fullName);
    }


}
