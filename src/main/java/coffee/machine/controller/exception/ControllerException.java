package coffee.machine.controller.exception;

import coffee.machine.exception.ApplicationException;

/**
 * This class represents custom exception for controller unit.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ControllerException extends ApplicationException {

    /**
     * @param messageKey message key from resource bundle, which corresponds needed message
     */
    public ControllerException(String messageKey) {
        super(messageKey);
    }

}
