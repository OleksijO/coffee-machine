package coffee.machine.controller.validation;

import coffee.machine.model.value.object.user.LoginData;
import coffee.machine.model.value.object.user.RegisterData;

import java.util.regex.Pattern;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN;

/**
 * Created by oleksij.onysymchuk@gmail on 16.01.2017.
 */
public class RegisterDataValidator implements Validator<RegisterData> {
    private Validator<LoginData> loginDataValidator = new LoginDataValidator();

    public Notification validate(RegisterData registerData) {

        Notification notification = loginDataValidator.validate(registerData);
        if (!isFullNameValid(registerData.getFullName())) {
            notification
                    .addMessageKey(ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN);
        }
        return notification;
    }

    private boolean isFullNameValid(String fullName) {
        return (fullName != null)
                && (
                Pattern.compile(coffee.machine.controller.RegExp.REGEXP_FULL_NAME)
                        .matcher(fullName)
                        .matches());
    }

    public void setLoginDataValidator(Validator<LoginData> loginDataValidator) {
        this.loginDataValidator = loginDataValidator;
    }
}
