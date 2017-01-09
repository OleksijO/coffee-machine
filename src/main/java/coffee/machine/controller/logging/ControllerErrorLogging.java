package coffee.machine.controller.logging;

import coffee.machine.exception.ApplicationException;
import coffee.machine.logging.ApplicationErrorLogging;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static coffee.machine.view.Attributes.*;

/**
 * This interface represents utility methods for logging errors in controller unit.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface ControllerErrorLogging extends ApplicationErrorLogging {
    String MESSAGE_IN_CASE_OF_EMPTY = "";
    String SEPARATOR = "\t";
    String STATE = "\nState:";
    String USER_ID_IS = "User_ID=";
    String ADMIN_ID_IS = "Admin_ID=";
    String REQUEST_URI_IS = "Request_URI=";
    String USER_LOCALE_IS = "User_locale=";
    String REQUEST_QUERY_IS = "Request_query=";
    String REQUEST_METHOD = "Request_method=";

    /**
     * @param logger  logger instance of class
     * @param message text message
     * @param request request instance
     * @param e       throwable instance
     */
    default void logError(Logger logger, String message, HttpServletRequest request, Exception e) {
        logger.error(message + getRequestData(request), e);
    }

    /**
     * @param logger  logger instance of class
     * @param request request instance
     * @param e       throwable instance
     */
    default void logError(Logger logger, HttpServletRequest request, Exception e) {
        logError(logger, MESSAGE_IN_CASE_OF_EMPTY, request, e);
    }

    /**
     * @param logger  logger instance of class
     * @param request request instance
     * @param e       throwable instance
     */
    default void logApplicationError(Logger logger, HttpServletRequest request, ApplicationException e) {
        logApplicationError(
                logger,
                e.getMessageKey(),
                ((e.getLogMessage() != null) ? e.getLogMessage() : MESSAGE_IN_CASE_OF_EMPTY) + SEPARATOR +
                        ((e.getAdditionalMessage() != null) ? e.getAdditionalMessage() : MESSAGE_IN_CASE_OF_EMPTY) +
                        getRequestData(request),
                e);
    }

    /**
     * @param request request instance
     */
    default String getRequestData(HttpServletRequest request) {
        StringBuilder messageBuilder = new StringBuilder().append(STATE);

        if (request.getSession().getAttribute(USER_ID) != null) {
            int userId = (int) request.getSession().getAttribute(USER_ID);
            if (userId > 0) {
                messageBuilder.append(SEPARATOR).append(USER_ID_IS).append(userId);
            }
        }
        if (request.getSession().getAttribute(ADMIN_ID) != null) {
            int adminId = (int) request.getSession().getAttribute(ADMIN_ID);
            if (adminId > 0) {
                messageBuilder.append(SEPARATOR).append(ADMIN_ID_IS).append(adminId);
            }
        }
        messageBuilder.append(SEPARATOR).append(REQUEST_URI_IS).append(request.getRequestURI())
                .append(SEPARATOR).append(REQUEST_METHOD).append(request.getMethod().toUpperCase())
                .append(SEPARATOR).append(REQUEST_QUERY_IS).append(request.getQueryString())
                .append(SEPARATOR)
                .append(USER_LOCALE_IS).append(request.getSession().getAttribute(USER_LOCALE));

        return messageBuilder.toString();
    }

}
