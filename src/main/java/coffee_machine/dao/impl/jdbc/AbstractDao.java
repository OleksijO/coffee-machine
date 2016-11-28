package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.GenericDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.logging.DaoErrorProcessing;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class AbstractDao<T> implements GenericDao<T>, DaoErrorProcessing {
    static final String FIELD_ID = "id";

    private final Logger logger;

    public AbstractDao(Logger logger) {
        this.logger = logger;
    }


    protected void checkSingleResult(List list) {
        if ((list != null) && (list.size() > 1)) {
            logErrorAndThrowNewDaoException(DaoErrorKey.DB_ERROR_UNEXPECTED_MULTIPLE_RESULT_WHILE_GETTING_BY_ID);
        }
    }

    protected int executeInsertStatement(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    protected void logErrorAndThrowDaoException(String messageKey, Exception e) {
        logApplicationError(logger, messageKey, e);
        throw new DaoException(messageKey, e);
    }

    protected void logErrorAndThrowDaoException(String messageKey, Object entity, Exception e) {
        logApplicationError(logger, messageKey, entity.toString(), e);
        throw new DaoException(messageKey, e);
    }

    protected void logErrorAndThrowNewDaoException(String messageKey) {
        logApplicationError(logger, messageKey);
        throw new DaoException(messageKey);
    }

}
