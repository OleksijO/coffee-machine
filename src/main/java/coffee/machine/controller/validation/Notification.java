package coffee.machine.controller.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a part of a notification pattern for validation.
 * It represents a notification object, which contains information about validation errors.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Notification {
    private Class validationObjectClass;
    private List<String> errorMessageKeys = new ArrayList<>();
    private List<String> logMessages = new ArrayList<>();

    public Notification(Class validationObjectClass) {
        this.validationObjectClass = validationObjectClass;
    }

    public boolean hasErrorMessages() {
        return !errorMessageKeys.isEmpty();
    }

    public List<String> getErrorMessageKeys() {
        return errorMessageKeys;
    }

    public List<String> getLogMessages() {
        return logMessages;
    }

    public Class getValidationObjectClass() {
        return validationObjectClass;
    }

    public Notification addMessageKey(String errorMessageKey) {
        errorMessageKeys.add(errorMessageKey);
        return this;
    }

    public Notification addLogMessage(String logMessage) {
        logMessages.add(logMessage);
        return this;
    }
}
