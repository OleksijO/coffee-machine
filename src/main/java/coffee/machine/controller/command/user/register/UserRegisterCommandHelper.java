package coffee.machine.controller.command.user.register;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.command.login.LoginCommandHelper;
import coffee.machine.controller.logging.ControllerErrorLogging;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.i18n.message.key.error.CommandErrorKey;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static coffee.machine.view.Parameters.FULL_NAME_PARAM;
import static coffee.machine.view.Parameters.PASSWORD_PARAM;

/**
 * This class represents register page main functionality helper.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserRegisterCommandHelper implements ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(UserRegisterCommandHelper.class);

    private static final Pattern PATTERN_FULL_NAME = Pattern.compile(RegExp.REGEXP_FULL_NAME);

    private LoginCommandHelper loginHelper = new LoginCommandHelper();

    public RegisterFormData processRegisterForm(HttpServletRequest request) {
        String email = request.getParameter(Parameters.LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        String fullName = request.getParameter(FULL_NAME_PARAM);

        RegisterFormData formData = new RegisterFormData(email, password, fullName);

        if (!loginHelper.checkLogin(email)) {
      //      request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN);
            return formData;
        }
        if (!loginHelper.checkPassword(password)) {
     //       request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN);
            return formData;
        }
        if (!checkFullName(fullName)) {
            request.setAttribute(Attributes.ERROR_MESSAGE, CommandErrorKey.ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN);
            return formData;
        }

     //   formData.setValid(true);
        return formData;
    }

    private boolean checkFullName(String fullName) {
        return loginHelper.checkToPattern(PATTERN_FULL_NAME, fullName);
    }

    public void setGeneralRegisterPageAttributes(HttpServletRequest request) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_REGISTER);
        request.setAttribute(Attributes.REGISTER_FORM_TITLE, GeneralKey.REGISTER_USER_FORM_TITLE);
        request.setAttribute(Attributes.REGISTER_FORM_ACTION, PagesPaths.USER_REGISTER_PATH);
    }

}
