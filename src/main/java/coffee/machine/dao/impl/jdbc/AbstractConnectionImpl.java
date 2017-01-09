package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class represents JDBC implementation of AbstractConnection.
 * It performs rollback if transaction began but was not committed before close method was called.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class AbstractConnectionImpl implements AbstractConnection {
    private static final String CAN_NOT_BEGIN_TRANSACTION = "Can not begin transaction.";
    private static final String CAN_NOT_COMMIT_TRANSACTION = "Can not commit transaction";
    private static final String CAN_NOT_ROLLBACK_TRANSACTION = "Can not rollback transaction";
    private static final String CAN_NOT_CLOSE_CONNECTION = "Can not close connection";

    private static final int DEFAULT_TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private static final int SERIALIZABLE_TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;

    private Connection connection;

    private boolean transactionActive = false;

    AbstractConnectionImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void beginTransaction() {
        beginTransactionWithIsolationLevel(DEFAULT_TRANSACTION_ISOLATION_LEVEL);
    }

    private void beginTransactionWithIsolationLevel(int transactionIsolationLevel) {
        try {
            connection.setTransactionIsolation(transactionIsolationLevel);
            connection.setAutoCommit(false);
            transactionActive = true;
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(CAN_NOT_BEGIN_TRANSACTION);
        }
    }


    @Override
    public void beginSerializableTransaction() {
        beginTransactionWithIsolationLevel(SERIALIZABLE_TRANSACTION_ISOLATION_LEVEL);
    }

    @Override
    public void commitTransaction() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            transactionActive = false;
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(CAN_NOT_COMMIT_TRANSACTION);
        }

    }

    @Override
    public void rollbackTransaction() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            transactionActive = false;
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(CAN_NOT_ROLLBACK_TRANSACTION);
        }
    }

    @Override
    public void close() {
        try {
            if (transactionActive) {
                rollbackTransaction();
            }
            connection.close();
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(CAN_NOT_CLOSE_CONNECTION);
        }
    }

    Connection getConnection() {
        return connection;
    }

    void setConnection(Connection connection) {
        this.connection = connection;
    }

}
