package coffee_machine.service.impl;

import coffee_machine.i18n.SupportedLocale;
import coffee_machine.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

abstract class AbstractService  {
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n.messages",
			SupportedLocale.EN.getLocale());

	private final Logger logger;

	public AbstractService(Logger logger) {
		this.logger = logger;
	}

	protected void logErrorAndWrapException(Throwable e) {
		logger.error(RESOURCE_BUNDLE.getString(e.getMessage()), e);
		ServiceException exception = new ServiceException(e.getMessage(), e);
		if (e.getClass().equals(ServiceException.class)){
			exception.setAdditionalMessage(((ServiceException)e).getAdditionalMessage());
		}
		throw exception;
	}

	protected void logErrorAndThrowNewServiceException(String messageKey) {
		logger.error(RESOURCE_BUNDLE.getString(messageKey));
		throw new ServiceException(messageKey);
	}

	protected void logErrorAndThrowNewServiceException(String messageKey, String additionalMessage) {
		logger.error(RESOURCE_BUNDLE.getString(messageKey)+additionalMessage);
		throw new ServiceException(messageKey, additionalMessage);
	}
}
