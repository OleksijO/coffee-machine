package coffee.machine.controller.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 16.01.2017.
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
