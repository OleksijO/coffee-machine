package coffee.machine.controller.validation;

/**
 * This interface defines general behaviour for validators.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface Validator<T> {

    /**
     * @param object Object inctance to be validated
     * @return A notification object, which contains information about validation results
     */
    Notification validate(T object);
}
