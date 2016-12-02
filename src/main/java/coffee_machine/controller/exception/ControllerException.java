package coffee_machine.controller.exception;

import coffee_machine.exception.ApplicationException;

/**
 * This class represents custom exception for controller unit.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ControllerException extends ApplicationException {

    /**
     * @param message message key from resource bundle, which corresponds needed message
     */
    public ControllerException(String message) {
        super(message);
    }

}
