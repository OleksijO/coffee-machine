package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.Command;
import coffee_machine.controller.impl.command.abstracts.AbstractLoginCommand;
import coffee_machine.controller.security.PasswordEncryptor;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.model.entity.user.User;
import coffee_machine.service.UserService;
import coffee_machine.service.impl.UserServiceImpl;
import coffee_machine.view.Attributes;
import coffee_machine.view.PagesPaths;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.*;
import static coffee_machine.view.Parameters.LOGIN;
import static coffee_machine.view.Parameters.PASSWORD;
import static coffee_machine.i18n.message.key.error.CommandErrorKey.*;

public class UserLoginSubmitCommand extends AbstractLoginCommand implements Command {
    private static final Logger logger = Logger.getLogger(UserLoginSubmitCommand.class);

    UserService userService = UserServiceImpl.getInstance();

    public UserLoginSubmitCommand() {
        super(logger);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_LOGIN);
            request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_USER_FORM_TITLE);
            request.setAttribute(Attributes.LOGIN_FORM_ACTION, PagesPaths.USER_LOGIN_PATH);

            String email = request.getParameter(LOGIN);
            request.setAttribute(PREVIOUS_ENTERED_EMAIL, email);
            String password = request.getParameter(PASSWORD);

            if (!processLoginForm(request, email, password)) {
                return LOGIN_PAGE;
            }

            String encryptedPassword = PasswordEncryptor.encryptPassword(password);
            User user = userService.getUserByLogin(email);

            if ((user == null) || (!encryptedPassword.equals(user.getPassword()))) {
                logger.info(TRY_FAILED_WRONG_EMAIL_OR_PASSWORD);
                request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_NO_SUCH_COMBINATION);
            } else {
                logger.info(String.format(USER_LOGGED_IN, user.getId()));
                request.getSession().setAttribute(USER_ID, user.getId());
                response.sendRedirect(USER_HOME_PATH);
                return REDIRECTED;

            }
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
}


