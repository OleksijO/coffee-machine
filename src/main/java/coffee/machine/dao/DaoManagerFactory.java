package coffee.machine.dao;

/**
 * This class represents Factory of DaoManagers
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface DaoManagerFactory {

	/**
	 * @return Abstract Connection for getting DAO from Factory
	 */
	DaoManager createDaoManager();


}
