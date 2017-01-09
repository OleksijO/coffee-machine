package coffee.machine.controller.exception;

import coffee.machine.exception.ApplicationException;

/**
 * This class represents custom exception for controller unit.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ControllerException extends ApplicationException {

    @Override
    public ControllerException addLogMessage(String logMessage) {
        super.addLogMessage(logMessage);
        return this;
    }

    @Override
    public ControllerException addMessageKey(String messageKey) {
        super.addMessageKey(messageKey);
        return this;
    }

    @Override
    public ControllerException addAdditionalMessage(String additionalMessage) {
        super.addAdditionalMessage(additionalMessage);
        return this;
    }

}
