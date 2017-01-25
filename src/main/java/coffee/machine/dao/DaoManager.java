package coffee.machine.dao;

/**
 * This class represents abstract connection.
 *
 * It should consider transaction's rollback necessary in close method.
 * It MUST call ROLLBACK IF TRANSACTION has been begun
 * but was NOT COMMITTED before close method was called.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface DaoManager extends AutoCloseable {

	/**
	 * Defines begin of transaction
	 */
	void beginTransaction();

	/**
	 * Defines begin of transaction with high isolation level
	 */
	void beginSerializableTransaction();

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
	 *
	 * IMPORTANT.
	 * It MUST call ROLLBACK IF TRANSACTION has been begun
	 * but was NOT COMMITTED before close method was called, f.e. if any exception was thrown
	 */
	@Override
	void close();

	/**
	 * @return user entity DAO
	 */
	UserDao getUserDao();

	/**
	 * @return drink entity DAO
	 */
	DrinkDao getDrinkDao();

	/**
	 * @return addon entity DAO
	 */
	AddonDao getAddonDao();

	/**
	 * @return account entity DAO
	 */
	AccountDao getAccountDao();

	/**
	 * @return order entity DAO
	 */
	OrderDao getOrderDao();


}
