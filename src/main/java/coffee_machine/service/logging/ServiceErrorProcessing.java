package coffee_machine.service.logging;

import coffee_machine.logging.ApplicationErrorLogging;
import coffee_machine.service.exception.ServiceException;
import org.apache.log4j.Logger;

/**
 * Created by oleksij.onysymchuk@gmail on 28.11.2016.
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
