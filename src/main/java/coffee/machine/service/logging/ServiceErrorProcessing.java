package coffee.machine.service.logging;

import coffee.machine.logging.ApplicationErrorLogging;
import coffee.machine.service.exception.ServiceException;
import org.apache.log4j.Logger;

/**
 * This interface represents general utility methods for logging errors with
 * user error messages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface ServiceErrorProcessing extends ApplicationErrorLogging {

    /**
     * Logs error and throws ServiceException with default message for view
     *
     * @param logger logger instance of class
     * @param messageKey error text message bundle key for view
     */
    default void logErrorAndThrowNewServiceException(Logger logger, String messageKey) {
        logApplicationError(logger, messageKey);
        throw new ServiceException(messageKey);
    }

    /**
     * Logs error and throws ServiceException with default message for view
     *
     * @param logger logger instance of class
     * @param messageKey error text message bundle key for view
     * @param additionalMessage additional text message
     */
    default void logErrorAndThrowNewServiceException(Logger logger, String messageKey, String additionalMessage) {
        logApplicationError(logger, messageKey, additionalMessage);
        throw new ServiceException(messageKey, additionalMessage);
    }

}
