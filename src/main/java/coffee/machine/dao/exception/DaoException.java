package coffee.machine.dao.exception;

import coffee.machine.exception.ApplicationException;
import coffee.machine.i18n.message.key.error.DaoErrorKey;

/**
 * This class represents custom exception for controller unit.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DaoException extends ApplicationException {

	public DaoException(){
		super(DaoErrorKey.DAO_ERROR);
	}

	/**
	 * @param cause throwable instance
	 */
	public DaoException(Throwable cause) {
		super(DaoErrorKey.DAO_ERROR, cause);
	}

}
