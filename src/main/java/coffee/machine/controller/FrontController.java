package coffee.machine.controller;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.config.Paths.REDIRECTED;

/**
 * This class represents request dispatcher. It calls commands for correspondent request uri
 * and forwards request to the appropriate view page.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class FrontController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(FrontController.class);
    private static final String URI_IS = " : uri = ";

    private static final String DELIMITER = CommandHolder.DELIMITER;

    /**
     * Command's holder instance
     */
    CommandHolder commandHolder;


    @Override
    public void init() throws ServletException {
        super.init();
        commandHolder = new CommandHolder(getServletContext().getContextPath());
    }

    /**
     * The main method, which redirects request to an appropriate page depends on commands results.
     *
     * @param request  request instance
     * @param response response instance
     * @throws IOException      in case of troubles with redirecting
     * @throws ServletException in case of internal servlet troubles. Do not used directly in application.
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String commandKey = getMethod(request) + DELIMITER + getUri(request);
        Command command = commandHolder.findCommand(commandKey);

        String view = command.execute(request, response);
        if (!isRedirected(view)) {
            request.getRequestDispatcher(view).forward(request, response);
        }
    }

    private String getMethod(HttpServletRequest request) {
        return request.getMethod().toUpperCase();
    }

    private String getUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        logger.debug(request.getMethod().toUpperCase() + URI_IS + uri);
        return uri.toLowerCase();
    }

    private boolean isRedirected(String view) {
        return REDIRECTED.equals(view);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

}
