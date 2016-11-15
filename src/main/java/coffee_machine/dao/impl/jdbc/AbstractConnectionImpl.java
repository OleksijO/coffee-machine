package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.exception.DaoException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class AbstractConnectionImpl implements AbstractConnection {

	private static final Logger logger = Logger.getLogger(AbstractConnectionImpl.class);
	private Connection connection;

	AbstractConnectionImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void beginTransaction() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			logErrorAndThrowDaoException("Can not set autocommit to false", e);
		}

	}

	private void logErrorAndThrowDaoException(String message, Throwable e) {
		logger.error(message, e);
		throw new DaoException(message, e);
	}

	@Override
	public void setTransactionIsolationLevelToReadComitted() {
		try {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		} catch (SQLException e) {
			logErrorAndThrowDaoException("Can not set transaction isolation level to READ COMITTED.", e);
		}

	}

	@Override
	public void setTransactionIsolationLevelToNone() {
		try {
			connection.setTransactionIsolation(Connection.TRANSACTION_NONE);
		} catch (SQLException e) {
			logErrorAndThrowDaoException("Can not set transaction isolation level to NONE.", e);
		}

	}

	@Override
	public void commitTransaction() {
		try {
			connection.commit();
		} catch (SQLException e) {
			logErrorAndThrowDaoException("Can not commit transaction.", e);
		}

	}

	@Override
	public void rollbackTransaction() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			logErrorAndThrowDaoException("Can not rollback transaction.", e);
		}
	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			logErrorAndThrowDaoException("Can not close SQL connection.", e);
		}
	}

	Connection getConnection() {
		return connection;
	}

	void setConnection(Connection connection) {
		this.connection = connection;
	}

}
