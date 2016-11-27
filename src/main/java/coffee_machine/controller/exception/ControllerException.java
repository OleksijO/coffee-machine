package coffee_machine.controller.exception;

import coffee_machine.exception.ApplicationException;

/**
 * Created by oleksij.onysymchuk@gmail on 27.11.2016.
 */
public class ControllerException extends ApplicationException {
    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, String additionalMessage) {
        super(message, additionalMessage);
    }

    public ControllerException(String message, String additionalMessage, Throwable cause) {
        super(message, additionalMessage, cause);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }
}
