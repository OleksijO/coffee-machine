package coffee.machine.dao.logging;

import coffee.machine.dao.exception.DaoException;
import coffee.machine.logging.ApplicationErrorLogging;
import org.apache.log4j.Logger;

/**
 * @author oleksij.onysymchuk@gmail.com 28.11.2016.
 */
public interface DaoErrorProcessing extends ApplicationErrorLogging {

    default void logErrorAndThrowDaoException(Logger logger, String message, Exception e) {
        logger.error(message, e);
        throw new DaoException(e);
    }

    default void logErrorAndThrowDaoException(Logger logger, String message, Object entity, Exception e) {
        logApplicationError(logger, message, entity.toString(), e);
        throw new DaoException(e);
    }

    default void logErrorAndThrowDaoException(Logger logger, Exception e) {
        logger.error(e);
        throw new DaoException(e);
    }

    default void logErrorAndThrowDaoException(Logger logger, String message) {
        logger.error(message);
        throw new DaoException();
    }

}
