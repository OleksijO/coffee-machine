package coffee_machine.dao;

/**
 * This class represents abstract connection
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface AbstractConnection extends AutoCloseable {

	/**
	 * Defines begin of transaction
	 */
	void beginTransaction();

	/**
	 * Saves transaction.
	 */
	void commitTransaction();

	/**
	 * rolls back transaction
	 */
	void rollbackTransaction();

	/**
	 * Closes connection.
	 */
	@Override
	void close();

}
