package coffee.machine.controller.impl.command.user;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.helper.RegisterFormData;
import coffee.machine.controller.impl.command.helper.UserRegisterCommandHelper;
import coffee.machine.controller.security.PasswordEncryptor;
import coffee.machine.model.entity.user.User;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.PREVIOUS_ENTERED_EMAIL;
import static coffee.machine.view.Attributes.PREVIOUS_ENTERED_FULL_NAME;
import static coffee.machine.view.PagesPaths.USER_REGISTER_PAGE;
import static coffee.machine.view.PagesPaths.USER_REGISTER_SUCCESS_PAGE;

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
        helper.setGeneralRegisterPageAttributes(request);
        RegisterFormData formData = helper.processRegisterForm(request);

        request.setAttribute(PREVIOUS_ENTERED_EMAIL, formData.getEmail());
        request.setAttribute(PREVIOUS_ENTERED_FULL_NAME, formData.getFullName());

        if (!formData.isValid()) {
            return USER_REGISTER_PAGE;
        }

        User user = getUserFromFormData(formData);

        userService.createNewUser(user);

        logger.info(String.format(NEW_USER_HAS_BEEN_REGISTERED_FORMAT, user.getEmail(), user.getId()));

        request.setAttribute(Attributes.ADMIN_CONTACTS, CoffeeMachineConfig.ADMIN_CONTACT_INFO);

        return USER_REGISTER_SUCCESS_PAGE;
    }

    private User getUserFromFormData(RegisterFormData formData) {
        User user = new User();
        user.setAdmin(false);
        user.setFullName(formData.getFullName());
        user.setEmail(formData.getEmail());
        user.setPassword(PasswordEncryptor.encryptPassword(formData.getPassword()));
        return user;
    }
}
