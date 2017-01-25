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
public class DaoFactoryImpl implements DaoFactory {
    private static final String SQL_CONNECTION_CAN_NOT_BE_NULL =
            "SQL connection can not be null. Datasource returned no connection.";
    private static final String CONNECTION_CAN_NOT_BE_NULL = "Connection can not be null.";
    private static final String CONNECTION_IS_NOT_AN_ABSTRACT_CONNECTION_IMPL_FOR_JDBC = "Connection is not an DaoManagerImpl for JDBC.";

    private DataSource dataSource = JdbcPooledDataSource.getInstance();

    private DaoFactoryImpl() {
    }

    private static class InstanceHolder {
        private static final DaoFactory instance = new DaoFactoryImpl();
    }

    public static DaoFactory getInstance() {
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
                    .addLogMessage(SQL_CONNECTION_CAN_NOT_BE_NULL);
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
