package coffee_machine.dao.logging;

import coffee_machine.dao.exception.DaoException;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import coffee_machine.logging.ApplicationErrorLogging;
import org.apache.log4j.Logger;

/**
 * Created by oleksij.onysymchuk@gmail on 28.11.2016.
 */
public interface DaoErrorProcessing extends ApplicationErrorLogging {

    default void logErrorAndThrowDaoException(Logger logger, String message, Exception e) {
        logApplicationError(logger, message, e);
        throw new DaoException(DaoErrorKey.DAO_ERROR, e);
    }

    default void logErrorAndThrowDaoException(Logger logger, String message, Object entity, Exception e) {
        logApplicationError(logger, message, entity.toString(), e);
        throw new DaoException(DaoErrorKey.DAO_ERROR, e);
    }

    default void logErrorAndThrowNewDaoException(Logger logger, String message) {
        logApplicationError(logger, message);
        throw new DaoException(DaoErrorKey.DAO_ERROR);
    }

    default void logErrorAndThrowDaoException(Logger logger, Exception e) {
        logError(logger, e);
        throw new DaoException(DaoErrorKey.DAO_ERROR, e);
    }

    default void logErrorAndThrowDaoException(Logger logger, String message) {
        logError(logger, message);
        throw new DaoException(DaoErrorKey.DAO_ERROR);
    }

}
