package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.*;
import coffee.machine.dao.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class represents JDBC implementation of DaoManager.
 * It performs rollback if transaction began but was not committed or rolled back before close method was called.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class DaoManagerImpl implements DaoManager {
    private static final String LOG_MESSAGE_CAN_NOT_BEGIN_TRANSACTION = "Can not begin transaction.";
    private static final String LOG_MESSAGE_CAN_NOT_COMMIT_TRANSACTION = "Can not commit transaction";
    private static final String LOG_MESSAGE_CAN_NOT_ROLLBACK_TRANSACTION = "Can not rollback transaction";
    private static final String LOG_MESSAGE_CAN_NOT_CLOSE_CONNECTION = "Can not close connection";

    private static final int DEFAULT_TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private static final int SERIALIZABLE_TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;
    private final DaoFactoryImpl daoFactory;

    private Connection sqlConnection;

    private boolean transactionActive = false;

    DaoManagerImpl(Connection sqlConnection, DaoFactoryImpl daoFactory) {
        this.sqlConnection = sqlConnection;
        this.daoFactory = daoFactory;
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
                    .addLogMessage(LOG_MESSAGE_CAN_NOT_BEGIN_TRANSACTION);
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
                    .addLogMessage(LOG_MESSAGE_CAN_NOT_COMMIT_TRANSACTION);
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
                    .addLogMessage(LOG_MESSAGE_CAN_NOT_ROLLBACK_TRANSACTION);
        }
    }

    @Override
    public void close() {
        if (transactionActive) {
            rollbackTransaction();
        }
        try {
            sqlConnection.close();
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_CAN_NOT_CLOSE_CONNECTION);
        }
    }

    @Override
    public UserDao getUserDao() {
        return daoFactory.getUserDao(sqlConnection);
    }

    @Override
    public DrinkDao getDrinkDao() {
        return daoFactory.getDrinkDao(sqlConnection);
    }

    @Override
    public AddonDao getAddonDao() {
        return daoFactory.getAddonDao(sqlConnection);
    }

    @Override
    public AccountDao getAccountDao() {
        return daoFactory.getAccountDao(sqlConnection);
    }

    @Override
    public OrderDao getOrderDao() {
        return daoFactory.getOrderDao(sqlConnection);
    }

    Connection getSqlConnection() {
        return sqlConnection;
    }

}
