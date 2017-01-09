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
    private static final String CONNECTION_IS_NOT_AN_ABSTRACT_CONNECTION_IMPL_FOR_JDBC = "Connection is not an AbstractConnectionImpl for JDBC.";

    private final DataSource dataSource = JdbcPooledDataSource.getInstance();

    private DaoFactoryImpl() {
    }

    private static class InstanceHolder {
        private static final DaoFactory instance = new DaoFactoryImpl();
    }

    public static DaoFactory getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public AbstractConnection getConnection() {
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

        return new AbstractConnectionImpl(connection);
    }

    @Override
    public UserDao getUserDao(AbstractConnection connection) {
        checkConnection(connection);
        Connection sqlConnection = getSqlConnection(connection);
        return new UserDaoImpl(sqlConnection, new AccountDaoImpl(sqlConnection));
    }

    private Connection getSqlConnection(AbstractConnection connection) {

        return ((AbstractConnectionImpl) connection).getConnection();
    }

    private void checkConnection(AbstractConnection connection) {

        if (connection == null) {
            throw new DaoException()
                    .addLogMessage(CONNECTION_CAN_NOT_BE_NULL);
        }
        if (!(connection instanceof AbstractConnectionImpl)) {
            throw new DaoException()
                    .addLogMessage(CONNECTION_IS_NOT_AN_ABSTRACT_CONNECTION_IMPL_FOR_JDBC);
        }

    }

    @Override
    public DrinkDao getDrinkDao(AbstractConnection connection) {
        checkConnection(connection);
        return new DrinkDaoImpl(getSqlConnection(connection));
    }

    @Override
    public AddonDao getAddonDao(AbstractConnection connection) {
        checkConnection(connection);
        return new AddonDaoImpl(getSqlConnection(connection));
    }

    @Override
    public AccountDao getAccountDao(AbstractConnection connection) {
        checkConnection(connection);
        return new AccountDaoImpl(getSqlConnection(connection));
    }

    @Override
    public OrderDao getOrderDao(AbstractConnection connection) {
        checkConnection(connection);
        return new OrderDaoImpl(getSqlConnection(connection));
    }
}
