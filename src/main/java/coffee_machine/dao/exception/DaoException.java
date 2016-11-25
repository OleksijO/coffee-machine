package coffee_machine.dao.exception;

import coffee_machine.exception.ApplicationException;

public class DaoException extends ApplicationException {


	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(String message, String additionalMessage) {
		super(message, additionalMessage);
	}

	public DaoException(String message, String additionalMessage, Throwable cause) {
		super(message, additionalMessage, cause);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}
}
