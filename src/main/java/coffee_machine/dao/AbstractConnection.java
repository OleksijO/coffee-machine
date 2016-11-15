package coffee_machine.dao;

public interface AbstractConnection extends AutoCloseable {

	void beginTransaction();

	void commitTransaction();

	void rollbackTransaction();

	@Override
	void close();

}
