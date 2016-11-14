package coffee_machine.dao.impl.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import coffee_machine.dao.AbstractConnection;

public class AbstractConnectionImpl implements AbstractConnection {
	private Connection connection;

	AbstractConnectionImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void beginTransaction() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setTransactionIsolationLevelToReadComitted() {
		try {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setTransactionIsolationLevelToNone() {
		try {
			connection.setTransactionIsolation(Connection.TRANSACTION_NONE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void commitTransaction() {
		try {
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void rollbackTransaction() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Connection getConnection() {
		return connection;
	}

	void setConnection(Connection connection) {
		this.connection = connection;
	}

}
