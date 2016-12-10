package coffee.machine.controller.impl.command.helper;

import coffee.machine.controller.logging.ControllerErrorLogging;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.PagesPaths.HOME_PATH;

/**
 * This class represents login page main functionality helper.
 * This is template for specific role user/admin login pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LogoutCommandHelper implements ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(LogoutCommandHelper.class);

    public void performActionsToLogOutOfRole(HttpServletRequest request,
                                             HttpServletResponse response,
                                             String logMessageFormat,
                                             String sessionAttribute) throws IOException {

        logger.info(String.format(logMessageFormat, (int) request.getSession().getAttribute(sessionAttribute)));
        request.getSession().removeAttribute(sessionAttribute);
        response.sendRedirect(HOME_PATH);
    }
}
