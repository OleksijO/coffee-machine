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
	DaoManager createDaoManager();


}
