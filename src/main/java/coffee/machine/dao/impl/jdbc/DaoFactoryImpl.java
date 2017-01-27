package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.*;
import coffee.machine.dao.exception.DaoException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class defines DAO Factory
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DaoFactoryImpl implements DaoManagerFactory {
    private static final String LOG_MESSAGE_SQL_CONNECTION_CAN_NOT_BE_NULL =
            "SQL connection can not be null. Datasource returned no connection.";

    private DataSource dataSource = JdbcPooledDataSource.getInstance();

    private DaoFactoryImpl() {
    }

    private static class InstanceHolder {
        private static final DaoManagerFactory instance = new DaoFactoryImpl();
    }

    public static DaoManagerFactory getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public DaoManager createDaoManager() {
        Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        if (connection == null) {
            throw new DaoException()
                    .addLogMessage(LOG_MESSAGE_SQL_CONNECTION_CAN_NOT_BE_NULL);
        }

        return new DaoManagerImpl(connection, this);
    }

    UserDao getUserDao(Connection connection) {
        return new UserDaoImpl(connection);
    }

    DrinkDao getDrinkDao(Connection connection) {
        return new DrinkDaoImpl(connection);
    }

    AddonDao getAddonDao(Connection connection) {
        return new AddonDaoImpl(connection);
    }

    AccountDao getAccountDao(Connection connection) {
        return new AccountDaoImpl(connection);
    }

    OrderDao getOrderDao(Connection connection) {
        return new OrderDaoImpl(connection);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
