package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.helper.UserRegisterCommandHelper;
import coffee.machine.controller.security.PasswordEncryptor;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.model.entity.user.User;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.PREVIOUS_ENTERED_EMAIL;
import static coffee.machine.view.Attributes.PREVIOUS_ENTERED_FULL_NAME;
import static coffee.machine.view.PagesPaths.USER_REGISTER_PAGE;
import static coffee.machine.view.Parameters.FULL_NAME_PARAM;
import static coffee.machine.view.Parameters.PASSWORD_PARAM;

/**
 * Created by oleksij.onysymchuk@gmail on 08.12.2016.
 */
public class UserRegisterSubmitCommand extends CommandExecuteWrapper {
    private static final Logger logger = Logger.getLogger(UserRegisterSubmitCommand.class);

    private static final String CANT_CREATE_USER = "Can't create user=";
    private static final String NEW_USER_HAS_BEEN_REGISTERED_FORMAT =
            "New user with email '%s' has been registered with id=%d.";

    private UserRegisterCommandHelper helper = new UserRegisterCommandHelper();
    private UserService userService = UserServiceImpl.getInstance();

    public UserRegisterSubmitCommand() {
        super(USER_REGISTER_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_REGISTER);
        request.setAttribute(Attributes.REGISTER_FORM_TITLE, GeneralKey.REGISTER_USER_FORM_TITLE);
        request.setAttribute(Attributes.REGISTER_FORM_ACTION, PagesPaths.USER_REGISTER_PATH);

        String email = request.getParameter(Parameters.LOGIN_PARAM);
        request.setAttribute(PREVIOUS_ENTERED_EMAIL, email);
        String password = request.getParameter(PASSWORD_PARAM);
        String fullName = request.getParameter(FULL_NAME_PARAM);
        request.setAttribute(PREVIOUS_ENTERED_FULL_NAME, fullName);

        if (!helper.processRegisterForm(request, email, password, fullName)) {
            return USER_REGISTER_PAGE;
        }

        String encryptedPassword = PasswordEncryptor.encryptPassword(password);
        User user = new User();
        user.setAdmin(false);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        try {
            userService.createNewUser(user);
        } catch (Exception e) {
            logger.error(CANT_CREATE_USER + user);
            throw e;
        }
        logger.info(String.format(NEW_USER_HAS_BEEN_REGISTERED_FORMAT, user.getEmail(), user.getId()));
        return PagesPaths.USER_REGISTER_SUCCESS_PAGE;
    }
}
