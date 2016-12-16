package coffee.machine.controller;

import coffee.machine.controller.impl.CommandHolderImpl;
import coffee.machine.controller.logging.ControllerErrorLogging;
import coffee.machine.exception.ApplicationException;
import coffee.machine.view.PagesPaths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.i18n.message.key.GeneralKey.ERROR_UNKNOWN;
import static coffee.machine.view.Attributes.ERROR_ADDITIONAL_MESSAGE;
import static coffee.machine.view.Attributes.ERROR_MESSAGE;
import static coffee.machine.view.PagesPaths.HOME_PAGE;

/**
 * This class represents request dispatcher. It calls commands for correspondent request uri
 * and forwards request to the appropriate view page.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class MainController extends HttpServlet implements ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(MainController.class);
    private static final String URI_IS = " : uri = ";
    private static final String REQUESTED_PATH_IS_NOT_SUPPORTED_REDIRECTING_TO_HOME_PAGE_FORMAT =
            "Requested path '%s' is not supported. Redirecting to home page.";

    /**
     * Command holder instance
     */
    CommandHolder commandHolder;


    @Override
    public void init() throws ServletException {
        super.init();
        commandHolder = new CommandHolderImpl();
    }

    /**
     * The main method, which redirects request to an appropriate page depends on commands results.
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
            if (command == null) {
                logger.info(String.format(
                        REQUESTED_PATH_IS_NOT_SUPPORTED_REDIRECTING_TO_HOME_PAGE_FORMAT,request.getRequestURI()));
                response.sendRedirect(PagesPaths.HOME_PATH);
                return;
            }

            String view = command.execute(request, response);

            if (PagesPaths.REDIRECTED.equals(view)) {
                return;     // redirected to reset uri
            }

            request.getRequestDispatcher(view).forward(request, response);

            return;

        } catch (ApplicationException e) {
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, ERROR_UNKNOWN);
        }

        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = getUri(request);
        processRequest(commandHolder.get(uri), request, response);
    }

    private String getUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        logger.debug(request.getMethod().toUpperCase() + URI_IS + uri);
        return uri;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = getUri(request);
        processRequest(commandHolder.post(uri), request, response);
    }

}
