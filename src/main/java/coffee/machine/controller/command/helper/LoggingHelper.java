package coffee.machine.controller.command.helper;

import coffee.machine.exception.ApplicationException;
import coffee.machine.i18n.SupportedLocale;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.ResourceBundle;

import static coffee.machine.config.CoffeeMachineConfig.MESSAGES;
import static coffee.machine.view.Attributes.*;

/**
 * This class represents functionality for build logging message
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoggingHelper {
    private static final String USER_LOCALIZED_MESSAGE = "User localized message: ";
    private static final String LOG_MESSAGE = "Log message: ";
    private static final String STATE = "State: ";

    private static final String LINE_SEPARATOR = "\n";
    private static final String SEPARATOR = "\t";
    private static final String MESSAGE_SEPARATOR = " : ";

    private static final String USER_ID_IS = " id=";
    private static final String REQUEST_URI_IS = "Request_URI=";
    private static final String USER_LOCALE_IS = "User_locale=";
    private static final String REQUEST_QUERY_IS = "Request_query=";
    private static final String REQUEST_METHOD = "Request_method=";


    /**
     * @param request Request instance
     * @return Log message with request contexts
     */
    public String buildLogMessage(HttpServletRequest request) {
        return getRequestData(request);
    }

    private String getRequestData(HttpServletRequest request) {
        StringBuilder messageBuilder = new StringBuilder()
                .append(LINE_SEPARATOR).append(SEPARATOR)
                .append(STATE);

        addUserRoleIfPreset(request, messageBuilder);
        addUserIdIfPresent(request, messageBuilder);
        messageBuilder.append(SEPARATOR).append(REQUEST_URI_IS).append(request.getRequestURI())
                .append(SEPARATOR).append(REQUEST_METHOD).append(request.getMethod().toUpperCase())
                .append(SEPARATOR).append(REQUEST_QUERY_IS).append(request.getQueryString())
                .append(SEPARATOR)
                .append(USER_LOCALE_IS).append(request.getSession().getAttribute(USER_LOCALE));

        return messageBuilder.toString();
    }

    private void addUserRoleIfPreset(HttpServletRequest request, StringBuilder messageBuilder) {
        if (request.getSession().getAttribute(USER_ROLE) != null) {
            messageBuilder.append(SEPARATOR)
                    .append(request.getSession().getAttribute(USER_ROLE));
        }
    }

    private void addUserIdIfPresent(HttpServletRequest request, StringBuilder messageBuilder) {
        if (request.getSession().getAttribute(USER_ID) != null) {
            messageBuilder
                    .append(USER_ID_IS)
                    .append(request.getSession().getAttribute(USER_ID));
        }
    }

    /**
     * @param e       Custom exception instance
     * @param request Request instance
     * @return Log message with exception and request contexts
     */
    public String buildLogMessage(ApplicationException e, HttpServletRequest request) {
        StringBuilder messageBuilder = new StringBuilder();
        addUserMessage(e, request, messageBuilder);
        addLogMessage(e, messageBuilder);
        addRequestData(request, messageBuilder);
        return messageBuilder.toString();
    }

    private void addUserMessage(ApplicationException e, HttpServletRequest request, StringBuilder messageBuilder) {
        if (e.getMessageKey() != null) {
            messageBuilder.append(LINE_SEPARATOR).append(SEPARATOR)
                    .append(USER_LOCALIZED_MESSAGE)
                    .append(getLocalizedMessageByKeyAndLocale(
                            e.getMessageKey(), (Locale) request.getSession().getAttribute(USER_LOCALE)));
            addAdditionalMessage(e, messageBuilder);
        }
    }

    private String getLocalizedMessageByKeyAndLocale(String messageKey, Locale locale) {
        Locale supportedLocale = SupportedLocale.getSupportedOrDefault(locale);
        return ResourceBundle.getBundle(MESSAGES, supportedLocale).getString(messageKey);
    }

    private void addAdditionalMessage(ApplicationException e, StringBuilder messageBuilder) {
        if (e.getAdditionalMessage() != null) {
            messageBuilder.append(MESSAGE_SEPARATOR)
                    .append(e.getAdditionalMessage());
        }
    }

    private StringBuilder addRequestData(HttpServletRequest request, StringBuilder messageBuilder) {
        return messageBuilder.append(getRequestData(request));
    }

    private void addLogMessage(ApplicationException e, StringBuilder messageBuilder) {
        if (e.getLogMessage() != null) {
            messageBuilder.append(LINE_SEPARATOR).append(SEPARATOR)
                    .append(LOG_MESSAGE).append(e.getLogMessage());
        }
    }


}
