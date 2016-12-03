package coffee.machine.controller.impl.command.admin;

import coffee.machine.controller.security.PasswordEncryptor;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.PagesPaths;
import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.helper.LoginCommandHelper;
import coffee.machine.model.entity.user.User;
import coffee.machine.service.UserService;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.impl.command.helper.LoginCommandHelper.ADMIN_LOGGED_IN;
import static coffee.machine.controller.impl.command.helper.LoginCommandHelper.TRY_FAILED_WRONG_EMAIL_OR_PASSWORD;
import static coffee.machine.i18n.message.key.error.CommandErrorKey.ERROR_LOGIN_NO_SUCH_COMBINATION;
import static coffee.machine.view.Attributes.ADMIN_ID;
import static coffee.machine.view.Attributes.ERROR_MESSAGE;
import static coffee.machine.view.Attributes.PREVIOUS_ENTERED_EMAIL;
import static coffee.machine.view.PagesPaths.REDIRECTED;
import static coffee.machine.view.Parameters.LOGIN_PARAM;
import static coffee.machine.view.Parameters.PASSWORD;

/**
 * This class represents admin login post request page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminLoginSubmitCommand extends CommandExecuteWrapper {
    private static final Logger logger = Logger.getLogger(AdminLoginSubmitCommand.class);
    UserService adminService = UserServiceImpl.getInstance();
    LoginCommandHelper helper = new LoginCommandHelper();

    public AdminLoginSubmitCommand() {
        super(PagesPaths.LOGIN_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_LOGIN);
        request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_ADMIN_FORM_TITLE);
        request.setAttribute(Attributes.LOGIN_FORM_ACTION, PagesPaths.ADMIN_LOGIN_PATH);

        String email = request.getParameter(LOGIN_PARAM);
        String password = request.getParameter(PASSWORD);
        request.setAttribute(PREVIOUS_ENTERED_EMAIL, email);

        if (!helper.processLoginForm(request, email, password)) {
            return PagesPaths.LOGIN_PAGE;
        }

        String encryptedPassword = PasswordEncryptor.encryptPassword(password);
        User admin = adminService.getUserByLogin(email);

        if ((admin == null) || (!encryptedPassword.equals(admin.getPassword())) || (!admin.isAdmin())) {

            logger.info(TRY_FAILED_WRONG_EMAIL_OR_PASSWORD);
            request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_NO_SUCH_COMBINATION);
            return PagesPaths.LOGIN_PAGE;

        } else {

            logger.info(String.format(ADMIN_LOGGED_IN, admin.getId()));
            request.getSession().setAttribute(ADMIN_ID, admin.getId());
            response.sendRedirect(PagesPaths.ADMIN_HOME_PATH);
            return REDIRECTED;
        }
    }

}
