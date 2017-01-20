package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class represents JDBC implementation of DaoConnection.
 * It performs rollback if transaction began but was not committed before close method was called.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class DaoConnectionImpl implements DaoConnection {
    private static final String CAN_NOT_BEGIN_TRANSACTION = "Can not begin transaction.";
    private static final String CAN_NOT_COMMIT_TRANSACTION = "Can not commit transaction";
    private static final String CAN_NOT_ROLLBACK_TRANSACTION = "Can not rollback transaction";
    private static final String CAN_NOT_CLOSE_CONNECTION = "Can not close connection";

    private static final int DEFAULT_TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private static final int SERIALIZABLE_TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;

    private Connection sqlConnection;

    private boolean transactionActive = false;

    DaoConnectionImpl(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    @Override
    public void beginTransaction() {
        beginTransactionWithIsolationLevel(DEFAULT_TRANSACTION_ISOLATION_LEVEL);
    }

    private void beginTransactionWithIsolationLevel(int transactionIsolationLevel) {
        try {
            sqlConnection.setTransactionIsolation(transactionIsolationLevel);
            sqlConnection.setAutoCommit(false);
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
            sqlConnection.commit();
            sqlConnection.setAutoCommit(true);
            transactionActive = false;
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(CAN_NOT_COMMIT_TRANSACTION);
        }

    }

    @Override
    public void rollbackTransaction() {
        try {
            sqlConnection.rollback();
            sqlConnection.setAutoCommit(true);
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
            sqlConnection.close();
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(CAN_NOT_CLOSE_CONNECTION);
        }
    }

    Connection getSqlConnection() {
        return sqlConnection;
    }

}
