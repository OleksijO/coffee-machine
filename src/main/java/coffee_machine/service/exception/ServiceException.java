package coffee_machine.service.exception;

import coffee_machine.exception.ApplicationException;

public class ServiceException extends ApplicationException {

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, String additionalMessage) {
		super(message, additionalMessage);
	}

	public ServiceException(String message, String additionalMessage, Throwable cause) {
		super(message, additionalMessage, cause);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}
