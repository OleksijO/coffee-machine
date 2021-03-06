package coffee.machine.controller.validation;

import coffee.machine.model.value.object.user.LoginData;

import java.util.Objects;
import java.util.regex.Pattern;

import static coffee.machine.controller.RegExp.REGEXP_EMAIL;
import static coffee.machine.controller.RegExp.REGEXP_PASSWORD;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN;

/**
 * This class represents validator for value object LoginData.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginDataValidator implements Validator<LoginData> {

    @Override
    public Notification validate(LoginData loginData) {
        Objects.requireNonNull(loginData);
        Notification notification = new Notification(loginData.getClass());
        if (!isLoginValid(loginData.getEmail())) {
            notification
                    .addMessageKey(ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN);
        }
        if (!isPasswordValid(loginData.getPassword())) {
            notification
                    .addMessageKey(ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN);
        }
        return notification;
    }

    private boolean isPasswordValid(String password) {
        return (password != null)
                && (
                Pattern.compile(REGEXP_PASSWORD)
                        .matcher(password)
                        .matches());
    }

    private boolean isLoginValid(String email) {
        return (email != null)
                && (
                Pattern.compile(REGEXP_EMAIL)
                        .matcher(email)
                        .matches());
    }
}
