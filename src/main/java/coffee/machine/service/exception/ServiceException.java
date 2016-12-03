package coffee.machine.service.exception;

import coffee.machine.exception.ApplicationException;

/**
 * This class represents custom application exception for service layer.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ServiceException extends ApplicationException {

	public ServiceException(String messageKey) {
		super(messageKey);
	}

	public ServiceException(String messageKey, String additionalMessage) {
		super(messageKey, additionalMessage);
	}

}
