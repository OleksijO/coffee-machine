package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.*;
import coffee_machine.dao.exception.DaoException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class DaoFactoryImpl implements coffee_machine.dao.DaoFactory {
	private static volatile DaoFactoryImpl instance;
	private DataSource dataSource = initDataSource();

	public static DaoFactory getInstance() {
		DaoFactoryImpl localInstance = instance;
		if (instance == null) {
			synchronized (DaoFactoryImpl.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new DaoFactoryImpl();
				}
			}
		}
		return localInstance;
	}

	private DataSource initDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			// TODO log
			throw new Error(e);
		}
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/coffee_machine");
		cpds.setUser("root");
		cpds.setPassword("root");
		cpds.setMaxPoolSize(20);
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		return cpds;
	}

	@Override
	public AbstractConnection getConnection() {
		Connection connection;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO log
			e.printStackTrace();
			throw new DaoException(e);
		}

		if (connection == null) {
			// TODO log
			throw new DaoException("SQL connection can not be null.");
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
			throw new DaoException("Connection can not be null.");
		}
		if (!(connection instanceof AbstractConnectionImpl)) {
			throw new DaoException("Connection is not a AbstractConnectionIml for JDBC.");
		}

	}

	@Override
	public AdminDao getAdminDao(AbstractConnection connection) {
		checkConnection(connection);
		return new AdminDaoImpl(getSqlConnection(connection));
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
	public CoffeeMachineDao getCoffeeMachineDao(AbstractConnection connection) {
		checkConnection(connection);
		return new CoffeeMachineDaoImpl(getSqlConnection(connection));
	}

	@Override
	public AccountDao getAccountDao(AbstractConnection connection) {
		checkConnection(connection);
		return new AccountDaoImpl(getSqlConnection(connection));
	}

}
