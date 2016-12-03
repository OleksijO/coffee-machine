package coffee.machine.dao.exception;

import coffee.machine.exception.ApplicationException;
import coffee.machine.i18n.message.key.error.DaoErrorKey;

/**
 * This class represents custom application exception for controller unit.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DaoException extends ApplicationException {

	/**
	 * Creates instance with default message key for view
	 */
	public DaoException(){
		super(DaoErrorKey.DAO_ERROR);
	}

	/**
	 * Creates instance with default message key for view
	 *
	 * @param cause throwable instance
	 */
	public DaoException(Throwable cause) {
		super(DaoErrorKey.DAO_ERROR, cause);
	}

}
