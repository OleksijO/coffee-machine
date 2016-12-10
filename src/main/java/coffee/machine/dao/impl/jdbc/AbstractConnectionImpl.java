package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.logging.DaoErrorProcessing;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class repesents JDBC implementation of AbstractConnection.
 * It performs rollback if transaction began but was not committed before close method was called.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class AbstractConnectionImpl implements AbstractConnection, DaoErrorProcessing {
    private static final Logger logger = Logger.getLogger(AbstractConnectionImpl.class);

    private static final String CAN_NOT_BEGIN_TRANSACTION = "Can not begin transaction.";
    private static final String CAN_NOT_COMMIT_TRANSACTION = "Can not commit transaction";
    private static final String CAN_NOT_ROLLBACK_TRANSACTION = "Can not rollback transaction";
    private static final String CAN_NOT_CLOSE_CONNECTION = "Can not close connection";

    private Connection connection;

    private boolean transactionBegin = false;
    private boolean transactionCommitted = false;

    AbstractConnectionImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void beginTransaction() {
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            transactionBegin = true;
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, CAN_NOT_BEGIN_TRANSACTION, e);
        }

    }

    @Override
    public void commitTransaction() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            transactionCommitted = true;
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, CAN_NOT_COMMIT_TRANSACTION, e);
        }

    }

    @Override
    public void rollbackTransaction() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            transactionCommitted = true;
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, CAN_NOT_ROLLBACK_TRANSACTION, e);
        }
    }

    @Override
    public void close() {
        try {
            if (transactionBegin && !transactionCommitted) {
                rollbackTransaction();
            }
            connection.close();
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, CAN_NOT_CLOSE_CONNECTION, e);
        }
    }

    Connection getConnection() {
        return connection;
    }

    void setConnection(Connection connection) {
        this.connection = connection;
    }

}
