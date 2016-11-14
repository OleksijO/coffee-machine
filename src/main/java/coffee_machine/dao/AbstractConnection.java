package coffee_machine.dao;

public interface AbstractConnection extends AutoCloseable {

	void beginTransaction();

	void setTransactionIsolationLevelToReadComitted();

	void setTransactionIsolationLevelToNone();

	void commitTransaction();

	void rollbackTransaction();

	@Override
	void close();

}
