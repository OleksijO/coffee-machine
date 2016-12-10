package coffee.machine.controller.impl.command;

import coffee.machine.controller.impl.command.helper.LoginCommandHelper;
import coffee.machine.controller.impl.command.helper.LoginFormData;
import coffee.machine.controller.security.PasswordEncryptor;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.model.entity.user.User;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.impl.command.helper.LoginCommandHelper.*;
import static coffee.machine.i18n.message.key.error.CommandErrorKey.ERROR_LOGIN_NO_SUCH_COMBINATION;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.*;

/**
 * This class represents user login post request page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginSubmitCommand extends CommandExecuteWrapper {
    private static final Logger logger = Logger.getLogger(LoginSubmitCommand.class);
    private static final String USER_ROLE_IS_NOT_IDENTIFIED = "User role is not identified";

    UserService userService = UserServiceImpl.getInstance();
    LoginCommandHelper helper = new LoginCommandHelper();

    public LoginSubmitCommand() {
        super(LOGIN_PAGE);
    }

    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setGeneralLoginPageAttributes(request);
        LoginFormData formData = helper.processLoginForm(request);
        if (helper.isDoubleLoginAttempt(request)) {
            return LOGIN_PAGE;
        }

        if (!formData.isValid()) {
            return LOGIN_PAGE;
        }

        String encryptedPassword = PasswordEncryptor.encryptPassword(formData.getPassword());
        User user = userService.getUserByLogin(formData.getEmail());

        if ((user == null) || (!encryptedPassword.equals(user.getPassword()))) {
            logger.info(TRY_FAILED_WRONG_EMAIL_OR_PASSWORD);
            request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_NO_SUCH_COMBINATION);
        } else {
            performActionsToLogIn(request, response, user);
            return REDIRECTED;
        }

        return LOGIN_PAGE;
    }

    private void setGeneralLoginPageAttributes(HttpServletRequest request) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_LOGIN);
        request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_FORM_TITLE);
        request.setAttribute(Attributes.LOGIN_FORM_ACTION, LOGIN_PATH);
    }

    private void performActionsToLogIn(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        if (user.isAdmin()) {

            helper.performActionsToLogInRole(request, response, ADMIN_LOGGED_IN,
                    user.getId(), ADMIN_ID, ADMIN_HOME_PATH);
            return;
        } else if (!user.isAdmin()) {

            helper.performActionsToLogInRole(request, response, USER_LOGGED_IN,
                    user.getId(), USER_ID, USER_HOME_PATH);
            return;
        }
        throw new UnsupportedOperationException(USER_ROLE_IS_NOT_IDENTIFIED);
    }

}


