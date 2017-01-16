package coffee.machine.dao;

/**
 * This class represents Factory of implemented DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface DaoFactory {

	/**
	 * @return Abstract Connection for getting DAO from Factory
	 */
	AbstractConnection getConnection();

	/**
	 * @param connection
	 *            Abstract connection instance
	 * @return user entity DAO
	 */
	UserDao getUserDao(AbstractConnection connection);

	/**
	 * @param connection
	 *            Abstract connection instance
	 * @return drink entity DAO
	 */
	DrinkDao getDrinkDao(AbstractConnection connection);

	/**
	 * @param connection
	 *            Abstract connection instance
	 * @return addon entity DAO
	 */
	AddonDao getAddonDao(AbstractConnection connection);

	/**
	 * @param connection
	 *            Abstract connection instance
	 * @return account entity DAO
	 */
	AccountDao getAccountDao(AbstractConnection connection);

	/**
	 * @param connection
	 *            Abstract connection instance
	 * @return order entity DAO
	 */
	OrderDao getOrderDao(AbstractConnection connection);

}
