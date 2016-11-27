package coffee_machine.controller;

import coffee_machine.controller.impl.CommandHolderImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.controller.PagesPaths.*;

/**
 * Servlet implementation class MainController
 */
// @WebServlet("/*")
public class MainController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MainController.class);
    private static final long serialVersionUID = 1L;
    private CommandHolder commandHolder;

    @Override
    public void init() throws ServletException {
        super.init();
        commandHolder = new CommandHolderImpl();
        commandHolder.init();
    }

    private void processRequest(Command command, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = getUri(request);
        processRequest(commandHolder.get(uri), request, response);
    }

    private String getUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        logger.debug(request.getMethod().toUpperCase()+" : uri = " + uri);
        return uri;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = getUri(request);
        processRequest(commandHolder.post(uri), request, response);
    }

}
