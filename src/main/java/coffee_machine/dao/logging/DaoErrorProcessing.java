package coffee_machine.dao.logging;

import coffee_machine.dao.exception.DaoException;
import coffee_machine.logging.ApplicationErrorLogging;
import org.apache.log4j.Logger;

/**
 * Created by oleksij.onysymchuk@gmail on 28.11.2016.
 */
public interface DaoErrorProcessing extends ApplicationErrorLogging {

    default void logErrorAndThrowDaoException(Logger logger, String messageKey, Exception e) {
        logApplicationError(logger, messageKey, e);
        throw new DaoException(messageKey, e);
    }

    default void logErrorAndThrowDaoException(Logger logger, String messageKey, Object entity, Exception e) {
        logApplicationError(logger, messageKey, entity.toString(), e);
        throw new DaoException(messageKey, e);
    }

    default void logErrorAndThrowNewDaoException(Logger logger, String messageKey) {
        logApplicationError(logger, messageKey);
        throw new DaoException(messageKey);
    }

}
