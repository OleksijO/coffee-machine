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
import static coffee_machine.view.PagesPaths.HOME_PATH;
import static coffee_machine.view.PagesPaths.REDIRECTED;

/**
 * Servlet implementation class MainController
 */
// @WebServlet("/*")
public class MainController extends HttpServlet implements ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(MainController.class);
    private static final long serialVersionUID = 1L;
    private CommandHolder commandHolder;

    @Override
    public void init() throws ServletException {
        super.init();
        commandHolder = new CommandHolderImpl();
    }

    private void processRequest(Command command, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {

            if (command == null) {
                logger.debug("Command did not found. Redirecting to home page");
                response.sendRedirect(HOME_PATH);
                return;
            }
            String view = command.execute(request, response);
            /* redirected to reset uri */
            if (REDIRECTED.equals(view)) {
                return;
            }
            logger.debug("Forwarding to " + view);
            request.getRequestDispatcher(view).forward(request, response);

        } catch (ApplicationException e) {
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        String uri = getUri(request);
        processRequest(commandHolder.post(uri), request, response);
    }

}
