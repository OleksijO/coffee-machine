package coffee.machine.controller.command.user.register;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.validation.Notification;
import coffee.machine.controller.validation.RegisterDataValidator;
import coffee.machine.controller.validation.Validator;
import coffee.machine.model.entity.User;
import coffee.machine.model.value.object.user.RegisterData;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.REGISTER_USER_FORM_TITLE;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.TITLE_USER_REGISTER;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.*;
import static coffee.machine.view.Parameters.FULL_NAME_PARAM;
import static coffee.machine.view.Parameters.PASSWORD_PARAM;

/**
 * Created by oleksij.onysymchuk@gmail
 */
public class UserRegisterSubmitCommand extends CommandWrapperTemplate {
    private static final Logger logger = Logger.getLogger(UserRegisterSubmitCommand.class);

    private static final String NEW_USER_HAS_BEEN_REGISTERED_FORMAT =
            "New user with email '%s' has been registered with id=%d.";

    private UserService userService = UserServiceImpl.getInstance();

    private Validator<RegisterData> registerDataValidator = new RegisterDataValidator();

    public UserRegisterSubmitCommand() {
        super(USER_REGISTER_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        RegisterData registerData = getRegisterDataFromRequest(request);
        saveFormData(request, registerData);

        Notification notification = registerDataValidator.validate(registerData);
        if (notification.hasMessages()){
            processValidationErrors(notification, request);
            return USER_REGISTER_PAGE;
        }

        User user = userService.createNewUser(registerData);
        processSuccessfulRegistration(request, user);
        removeFormData(request);

        return USER_REGISTER_SUCCESS_PAGE;
    }

    private RegisterData getRegisterDataFromRequest(HttpServletRequest request) {
        String email = request.getParameter(Parameters.LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        String fullName = request.getParameter(FULL_NAME_PARAM);
        return new RegisterData(email, password, fullName);
    }

    private void saveFormData(HttpServletRequest request, RegisterData formData) {
        request.setAttribute(PREVIOUS_ENTERED_EMAIL, formData.getEmail());
        request.setAttribute(PREVIOUS_ENTERED_FULL_NAME, formData.getFullName());
    }

    private void processSuccessfulRegistration(HttpServletRequest request, User user) {
        logger.info(String.format(NEW_USER_HAS_BEEN_REGISTERED_FORMAT, user.getEmail(), user.getId()));
        request.setAttribute(ADMIN_CONTACTS, CoffeeMachineConfig.ADMIN_CONTACT_INFO);
    }

    private void removeFormData(HttpServletRequest request) {
        request.removeAttribute(PREVIOUS_ENTERED_EMAIL);
        request.removeAttribute(PREVIOUS_ENTERED_FULL_NAME);
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_USER_REGISTER);
        request.setAttribute(REGISTER_FORM_TITLE, REGISTER_USER_FORM_TITLE);
        request.setAttribute(REGISTER_FORM_ACTION, USER_REGISTER_PATH);
    }

}
