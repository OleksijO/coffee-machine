package coffee.machine.service.exception;

import coffee.machine.exception.ApplicationException;

/**
 * This class represents custom application exception for service layer.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ServiceException extends ApplicationException {

	public ServiceException() {
	}

	@Override
	public ServiceException addLogMessage(String logMessage) {
		super.addLogMessage(logMessage);
		return this;
	}

	@Override
	public ServiceException addMessageKey(String messageKey) {
		super.addMessageKey(messageKey);
		return this;
	}

	@Override
	public ServiceException addAdditionalMessage(String additionalMessage) {
		super.addAdditionalMessage(additionalMessage);
		return this;
	}

}
