package coffee_machine.controller.logging;

import coffee_machine.exception.ApplicationException;
import coffee_machine.logging.ApplicationErrorLogging;
import coffee_machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by oleksij.onysymchuk@gmail on 28.11.2016.
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
        logApplicationError(logger,
                e.getMessage(),
                ((e.getAdditionalMessage() == null) ? e.getAdditionalMessage() : null) + getRequestData(request),
                e);
    }

    default String getRequestData(HttpServletRequest request) {
        return new StringBuilder()
                .append("\nState:\t")
                .append("\tUser ID =").append(request.getSession().getAttribute(Attributes.USER_ID))
                .append("\tAdmin ID =").append(request.getSession().getAttribute(Attributes.ADMIN_ID))
                .append("\tRequest URI = ").append(request.getRequestURI())
                .append("\tRequest query = ").append(request.getQueryString()).toString();
    }

}
