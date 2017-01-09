package coffee.machine.controller;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.PagesPaths.HOME_PATH;
import static coffee.machine.view.PagesPaths.REDIRECTED;

/**
 * This class represents request dispatcher. It calls commands for correspondent request uri
 * and forwards request to the appropriate view page.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class MainController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MainController.class);
    private static final String URI_IS = " : uri = ";


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

        String view = command.execute(request, response);

        if (!isRedirected(view)) {
            request.getRequestDispatcher(view).forward(request, response);
        }
    }



    private boolean isRedirected(String view) {
        return REDIRECTED.equals(view);
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
