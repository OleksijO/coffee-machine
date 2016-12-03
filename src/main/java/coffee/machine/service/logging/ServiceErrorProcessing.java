package coffee.machine.service.logging;

import coffee.machine.logging.ApplicationErrorLogging;
import coffee.machine.service.exception.ServiceException;
import org.apache.log4j.Logger;

/**
 * @author oleksij.onysymchuk@gmail.com 28.11.2016.
 */
public interface ServiceErrorProcessing extends ApplicationErrorLogging {

    default void logErrorAndThrowNewServiceException(Logger logger, String messageKey) {
        logApplicationError(logger, messageKey);
        throw new ServiceException(messageKey);
    }

    default void logErrorAndThrowNewServiceException(Logger logger, String messageKey, String additionalMessage) {
        logApplicationError(logger, messageKey, additionalMessage);
        throw new ServiceException(messageKey, additionalMessage);
    }

}
