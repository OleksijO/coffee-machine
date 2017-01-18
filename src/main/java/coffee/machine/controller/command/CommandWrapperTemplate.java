package coffee.machine.controller.command;

import coffee.machine.controller.Command;
import coffee.machine.controller.command.helper.LoggingHelper;
import coffee.machine.controller.validation.Notification;
import coffee.machine.exception.ApplicationException;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_UNKNOWN;
import static coffee.machine.view.Attributes.ERROR_ADDITIONAL_MESSAGE;
import static coffee.machine.view.Attributes.ERROR_MESSAGE;

/**
 * This class represents template for specific commands, which use services and could throw exception .
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public abstract class CommandWrapperTemplate implements Command {
    private static final String LOG_MESSAGE_FORMAT_ERROR_WHILE_VALIDATING_OBJECT_OF_CLASS =
            "Error while validating object of class %s. Details: \n\t";
    private static final String LOG_MESSAGES_DELIMITER = "\n\t";

    private static Logger logger = Logger.getLogger(CommandWrapperTemplate.class);
    private static LoggingHelper loggingHelper = new LoggingHelper();

    private final String pageAfterErrors;

    public CommandWrapperTemplate(String pageAfterErrors) {
        this.pageAfterErrors = pageAfterErrors;
    }

    /**
     * This method determines common try-catch wrapper for child specific login commands
     * {@inheritDoc}
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // common try-catch wrapper for command logic, which could throw application exception or any other
        try {

            String view = performExecute(request, response);
            placeNecessaryDataToRequest(request);
            return view;

        } catch (ApplicationException e) {
            processApplicationException(e, request);
        } catch (Exception e) {
            processException(request, e);
        }

        try {

            placeNecessaryDataToRequest(request);

        } catch (ApplicationException e) {
            processApplicationException(e, request);
        } catch (Exception e) {
            processException(request, e);
        }

        return pageAfterErrors;
    }


    /**
     * This method should be overridden in child classes and should place all needed for view data to request
     */
    protected abstract void placeNecessaryDataToRequest(HttpServletRequest request);

    /**
     * This method should be overridden in child classes and should perform specific logic
     *
     * @param request  request instance
     * @param response response instance
     * @return Same as method execute() - view identifier
     * @throws IOException in case of troubles with redirect
     */
    protected abstract String performExecute(HttpServletRequest request, HttpServletResponse response)
            throws IOException;

    private void processException(HttpServletRequest request, Exception e) {
        logger.error(e.getMessage() + loggingHelper.buildLogMessage(request), e);
        request.setAttribute(ERROR_MESSAGE, ERROR_UNKNOWN);
    }

    private void processApplicationException(ApplicationException e, HttpServletRequest request) {
        logger.error(loggingHelper.buildLogMessage(e, request), e);
        request.setAttribute(ERROR_MESSAGE, e.getMessageKey());
        request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
    }

    protected void processValidationErrors(Notification notification, HttpServletRequest request) {
        placeValidationErrorsToRequest(notification, request);
        logValidationErrors(notification, request);
    }

    private void placeValidationErrorsToRequest(Notification notification, HttpServletRequest request) {
        request.setAttribute(Attributes.VALIDATION_ERRORS, notification.getMessageKeys());
    }

    private void logValidationErrors(Notification notification, HttpServletRequest request) {
        if (notification.getLogMessages().size()==0){
            return;
        }
        String unitedMessages = notification.getLogMessages().stream()
                .collect(Collectors.joining(LOG_MESSAGES_DELIMITER));
        logger.error(
                String.format(LOG_MESSAGE_FORMAT_ERROR_WHILE_VALIDATING_OBJECT_OF_CLASS,
                        notification.getValidationObjectClass().getSimpleName()) +
                        unitedMessages +
                        loggingHelper.buildLogMessage(request)

        );
    }
}
