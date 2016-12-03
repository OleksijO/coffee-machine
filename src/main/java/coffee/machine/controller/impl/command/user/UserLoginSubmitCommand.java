package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.security.PasswordEncryptor;
import coffee.machine.view.PagesPaths;
import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.helper.LoginCommandHelper;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.model.entity.user.User;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.impl.command.helper.LoginCommandHelper.TRY_FAILED_WRONG_EMAIL_OR_PASSWORD;
import static coffee.machine.controller.impl.command.helper.LoginCommandHelper.USER_LOGGED_IN;
import static coffee.machine.i18n.message.key.error.CommandErrorKey.ERROR_LOGIN_NO_SUCH_COMBINATION;
import static coffee.machine.view.Attributes.ERROR_MESSAGE;
import static coffee.machine.view.Attributes.PREVIOUS_ENTERED_EMAIL;
import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.Parameters.PASSWORD;

/**
 * This class represents user login post request page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserLoginSubmitCommand extends CommandExecuteWrapper {
    private static final Logger logger = Logger.getLogger(UserLoginSubmitCommand.class);

    UserService userService = UserServiceImpl.getInstance();
    LoginCommandHelper helper=new LoginCommandHelper();

    public UserLoginSubmitCommand() {
        super(PagesPaths.LOGIN_PAGE);
    }

    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_LOGIN);
        request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_USER_FORM_TITLE);
        request.setAttribute(Attributes.LOGIN_FORM_ACTION, PagesPaths.USER_LOGIN_PATH);

        String email = request.getParameter(Parameters.LOGIN_PARAM);
        request.setAttribute(PREVIOUS_ENTERED_EMAIL, email);
        String password = request.getParameter(PASSWORD);

        if (!helper.processLoginForm(request, email, password)) {
            return PagesPaths.LOGIN_PAGE;
        }

        String encryptedPassword = PasswordEncryptor.encryptPassword(password);
        User user = userService.getUserByLogin(email);

        if ((user == null) || (!encryptedPassword.equals(user.getPassword())) || (user.isAdmin())) {
            logger.info(TRY_FAILED_WRONG_EMAIL_OR_PASSWORD);
            request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_NO_SUCH_COMBINATION);
        } else {
            logger.info(String.format(USER_LOGGED_IN, user.getId()));
            request.getSession().setAttribute(USER_ID, user.getId());
            response.sendRedirect(PagesPaths.USER_HOME_PATH);
            return PagesPaths.REDIRECTED;

        }

        return PagesPaths.LOGIN_PAGE;
    }
}


