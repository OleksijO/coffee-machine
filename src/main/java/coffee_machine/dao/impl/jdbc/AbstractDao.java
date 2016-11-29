package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.GenericDao;
import coffee_machine.dao.logging.DaoErrorProcessing;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class AbstractDao<T> implements GenericDao<T>, DaoErrorProcessing {
    static final String DB_ERROR_UNEXPECTED_MULTIPLE_RESULT_WHILE_GETTING_BY_ID =
            "Unexpected multiple result while getting by id";
    static final String CAN_NOT_CREATE_EMPTY = "Can not insert null entity.";
    static final String CAN_NOT_CREATE_ALREADY_SAVED = "Can not insert already saved entity (id!=0)";
    static final String DB_ERROR_WHILE_INSERTING = "Database error while inserting entity";
    static final String CAN_NOT_UPDATE_EMPTY = "Can not update null entity";
    static final String CAN_NOT_UPDATE_UNSAVED = "Can not update unsaved entity (id==0)";
    static final String DB_ERROR_WHILE_UPDATING ="Database error while updating entity" ;
    static final String DB_ERROR_WHILE_GETTING_ALL = "Database error while getting all";
    static final String DB_ERROR_WHILE_GETTING_BY_ID = "Database error while getting by id";
    static final String DB_ERROR_WHILE_DELETING_BY_ID = "Database error while deleting entity";

    static final String FIELD_ID = "id";
    
    private final Logger logger;

    public AbstractDao(Logger logger) {
        this.logger = logger;
    }


    protected void checkSingleResult(List list) {
        if ((list != null) && (list.size() > 1)) {
            logErrorAndThrowNewDaoException(DB_ERROR_UNEXPECTED_MULTIPLE_RESULT_WHILE_GETTING_BY_ID);
        }
    }

    protected int executeInsertStatement(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    protected void logErrorAndThrowDaoException(String message, Exception e) {
        logErrorAndThrowDaoException(logger, message, e);
    }

    protected void logErrorAndThrowDaoException(String message, Object entity, Exception e) {
        logErrorAndThrowDaoException(logger, message, entity.toString(), e);
    }

    protected void logErrorAndThrowNewDaoException(String message) {
        logErrorAndThrowNewDaoException(logger, message);
    }

}
