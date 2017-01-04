package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.GenericDao;
import coffee.machine.dao.logging.DaoErrorProcessing;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This class represents common methods for all DAOs, defines logging methods
 *
 * @author oleksij.onysymchuk@gmail.com
 * @param <T> Entity class
 */
abstract class AbstractDao<T> implements GenericDao<T>, DaoErrorProcessing {
    private final Logger logger = Logger.getLogger(AbstractDao.class);

    static final String DB_ERROR_UNEXPECTED_MULTIPLE_RESULT_WHILE_GETTING_BY_ID =
            "Unexpected multiple result while getting by id ";
    static final String CAN_NOT_CREATE_EMPTY = "Can not insert null entity.";
    static final String CAN_NOT_CREATE_ALREADY_SAVED = "Can not insert already saved entity (id!=0)";
    static final String DB_ERROR_WHILE_INSERTING = "Database error while inserting entity ";
    static final String CAN_NOT_UPDATE_EMPTY = "Can not update null entity ";
    static final String CAN_NOT_UPDATE_UNSAVED = "Can not update unsaved entity (id==0)";
    static final String DB_ERROR_WHILE_UPDATING = "Database error while updating entity ";
    static final String DB_ERROR_WHILE_GETTING_ALL = "Database error while getting all ";
    static final String DB_ERROR_WHILE_GETTING_BY_ID = "Database error while getting by id ";
    static final String DB_ERROR_WHILE_DELETING_BY_ID = "Database error while deleting entity ";

    static final String FIELD_ID = "id";
    static final String ORDER_BY_ID = " ORDER BY id ";



    /**
     * Provides check if the list has only one element
     *
     * @param list list to be checked on single element
     */
    protected void checkSingleResult(List list) {
        if ((list != null) && (list.size() > 1)) {
            logErrorAndThrowDaoException(logger, DB_ERROR_UNEXPECTED_MULTIPLE_RESULT_WHILE_GETTING_BY_ID);
        }
    }

    /**
     *
     * @param statement statement to be executed
     * @return  Generated entity id.
     * @throws SQLException in case of problems with executing statement
     */
    protected int executeInsertStatement(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
        try (ResultSet keys = statement.getGeneratedKeys()) {
            keys.next();
            return keys.getInt(1);
        }
    }



}
