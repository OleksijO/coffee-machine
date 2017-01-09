package coffee.machine.controller.command;

import coffee.machine.controller.Command;
import coffee.machine.controller.command.helper.LoggingHelper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.PagesPaths.HOME_PATH;
import static coffee.machine.view.PagesPaths.REDIRECTED;

/**
 * This class represents behaviour in case of handler for requested path is not found.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UnsupportedPathCommand implements Command {
    private static final Logger logger = Logger.getLogger(LogoutCommand.class);
    private static LoggingHelper loggingHelper = new LoggingHelper();

    private static final String REQUESTED_UNSUPPORTED_URI = "Requested unsupported URI. Redirecting to home page.";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.error(REQUESTED_UNSUPPORTED_URI + loggingHelper.buildLogMessage(request));
        response.sendRedirect(HOME_PATH);
        return REDIRECTED;
    }
}
