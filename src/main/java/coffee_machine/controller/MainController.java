package coffee_machine.controller;

import coffee_machine.controller.impl.CommandHolderImpl;
import coffee_machine.controller.logging.ControllerErrorLogging;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.GeneralKey;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.view.Attributes.ERROR_ADDITIONAL_MESSAGE;
import static coffee_machine.view.Attributes.ERROR_MESSAGE;
import static coffee_machine.view.PagesPaths.HOME_PAGE;
import static coffee_machine.view.PagesPaths.HOME_PATH;
import static coffee_machine.view.PagesPaths.REDIRECTED;

/**
 * This class represents main request controller. It calls commands for correspondent request uri
 * and forwards request to the appropriate view page.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class MainController extends HttpServlet implements ControllerErrorLogging {
    static final Logger logger = Logger.getLogger(MainController.class);

    /**
     * Command holder instance
     */
    CommandHolder commandHolder;

    /* initializing holder for commands by uri */
    @Override
    public void init() throws ServletException {
        super.init();
        commandHolder = new CommandHolderImpl();
    }

    /**
     * The main method, which redirects request to an approprient page depends on commands results.
     *
     * @param command  command instance, which corresponds request uri
     * @param request  request instance
     * @param response response instance
     * @throws IOException      in case of troubles with redirecting
     * @throws ServletException in case of internal servlet troubles. Do not used directly in application.
     */
    void processRequest(Command command, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {

            /* in case of unsupported uri redirecting to home page */
            if (command == null) {
                response.sendRedirect(HOME_PATH);
                return;
            }

            String view = command.execute(request, response);

            /* redirected to reset uri */
            if (REDIRECTED.equals(view)) {
                return;
            }

            request.getRequestDispatcher(view).forward(request, response);

            return;
        } catch (ApplicationException e) {
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* getting command for GET requests */

        String uri = getUri(request);
        processRequest(commandHolder.get(uri), request, response);
    }

    private String getUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        logger.debug(request.getMethod().toUpperCase() + " : uri = " + uri);
        return uri;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* getting command for POST requests */

        String uri = getUri(request);
        processRequest(commandHolder.post(uri), request, response);
    }

}
