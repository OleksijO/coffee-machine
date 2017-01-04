package coffee.machine.controller.command;

import coffee.machine.controller.logging.ControllerErrorLogging;
import coffee.machine.exception.ApplicationException;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.controller.Command;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.ERROR_ADDITIONAL_MESSAGE;
import static coffee.machine.view.Attributes.ERROR_MESSAGE;

/**
 * This class represents template for specific commands, which use services and could throw exception .
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public abstract class CommandWrapperTemplate implements Command, ControllerErrorLogging {
    static Logger logger = Logger.getLogger(CommandWrapperTemplate.class);
    private String pageAfterErrors;

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
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }

        placeNecessaryDataToRequest(request);

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
     * @return Same as method execute()
     * @throws IOException in case of troubles with redirect
     */
    protected abstract String performExecute(HttpServletRequest request, HttpServletResponse response)
            throws IOException;
}
