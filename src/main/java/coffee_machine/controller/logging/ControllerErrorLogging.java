package coffee_machine.controller.logging;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import coffee_machine.exception.ApplicationException;
import coffee_machine.logging.ApplicationErrorLogging;
import coffee_machine.view.Attributes;

/**
 * @author oleksij.onysymchuk@gmail.com 28.11.2016.
 */
public interface ControllerErrorLogging extends ApplicationErrorLogging {
	String MESSAGE_IN_CASE_OF_EMPTY = "";

	default void logError(Logger logger, String message, HttpServletRequest request, Exception e) {
		logger.error(message + getRequestData(request), e);
	}

	default void logError(Logger logger, HttpServletRequest request, Exception e) {
		logError(logger, MESSAGE_IN_CASE_OF_EMPTY, request, e);
	}

	default void logApplicationError(Logger logger, HttpServletRequest request, ApplicationException e) {
		logApplicationError(logger, e.getMessage(),
				((e.getAdditionalMessage() == null) ? e.getAdditionalMessage() : null) + getRequestData(request), e);
	}

	default String getRequestData(HttpServletRequest request) {
		StringBuilder messageBuilder = new StringBuilder().append("\nState:\t");

		if (request.getSession().getAttribute(Attributes.USER_ID) != null) {
			int userId = (int) request.getSession().getAttribute(Attributes.USER_ID);
			if (userId > 0) {
				messageBuilder.append("\tUser_ID=").append(userId);
			}
		}
		if (request.getSession().getAttribute(Attributes.ADMIN_ID) != null) {
			int adminId = (int) request.getSession().getAttribute(Attributes.ADMIN_ID);
			if (adminId > 0) {
				messageBuilder.append("\tAdmin_ID=").append(adminId);
			}
		}
		messageBuilder.append("\tRequest_URI=").append(request.getRequestURI()).append("\tRequest_query=")
				.append(request.getQueryString()).append("\tUser_locale=")
				.append(request.getSession().getAttribute(Attributes.USER_LOCALE));

		return messageBuilder.toString();
	}

}
