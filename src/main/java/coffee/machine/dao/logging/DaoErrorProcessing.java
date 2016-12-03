package coffee.machine.dao.logging;

import coffee.machine.dao.exception.DaoException;
import coffee.machine.logging.ApplicationErrorLogging;
import org.apache.log4j.Logger;

/**
 * This interface represents utility methods for processing errors in service layer.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface DaoErrorProcessing extends ApplicationErrorLogging {

    /**
     * Logs error and throws DaoException with default message for view
     *
     * @param logger logger instance of class
     * @param message error text message for log
     * @param e throwable instance
     */
    default void logErrorAndThrowDaoException(Logger logger, String message, Exception e) {
        logger.error(message, e);
        throw new DaoException(e);
    }

    /**
     * Logs error and throws DaoException with default message for view
     *
     * @param logger logger instance of class
     * @param message error text message for log
     * @param entity entity instance, which caused the error, to be logged
     * @param e throwable instance
     */
    default void logErrorAndThrowDaoException(Logger logger, String message, Object entity, Exception e) {
        logApplicationError(logger, message, entity.toString(), e);
        throw new DaoException(e);
    }

    /**
     * Logs error and throws DaoException with default message for view
     *
     * @param logger logger instance of class
     * @param e throwable instance
     */
    default void logErrorAndThrowDaoException(Logger logger, Exception e) {
        logger.error(e);
        throw new DaoException(e);
    }

    /**
     * Logs error and throws DaoException with default message for view
     *
     * @param logger logger instance of class
     * @param message error text message for log
     */
    default void logErrorAndThrowDaoException(Logger logger, String message) {
        logger.error(message);
        throw new DaoException();
    }

}
