package coffee.machine.dao.exception;

import coffee.machine.exception.ApplicationException;
import coffee.machine.dao.i18n.message.key.error.DaoErrorMessageKey;

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
		super(DaoErrorMessageKey.DAO_ERROR);
	}

	/**
	 * Creates instance with default message key for view
	 *
	 * @param cause throwable instance
	 */
	public DaoException(Throwable cause) {
		super(DaoErrorMessageKey.DAO_ERROR, cause);
	}

	@Override
	public DaoException addLogMessage(String logMessage) {
		super.addLogMessage(logMessage);
		return this;
	}

	@Override
	public DaoException addMessageKey(String messageKey) {
		super.addMessageKey(messageKey);
		return this;
	}

	@Override
	public DaoException addAdditionalMessage(String additionalMessage) {
		super.addAdditionalMessage(additionalMessage);
		return this;
	}
}
