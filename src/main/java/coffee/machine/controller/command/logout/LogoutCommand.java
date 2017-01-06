package coffee.machine.controller.command.logout;

import coffee.machine.controller.Command;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.ADMIN_ID;
import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.PagesPaths.HOME_PATH;
import static coffee.machine.view.PagesPaths.REDIRECTED;

/**
 * This class represents user logout request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LogoutCommand implements Command {
    private static final Logger logger = Logger.getLogger(LogoutCommand.class);

    private static final String USER_LOGGED_OUT = "User id=%d LOGGED OUT.";
    private static final String ADMIN_LOGGED_OUT = "Admin id=%d LOGGED OUT.";
    private static final String REDIRECT_PATH_AFTER_LOGOUT = HOME_PATH;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logDetails(request);

        request.getSession().invalidate();

        response.sendRedirect(REDIRECT_PATH_AFTER_LOGOUT);
        return REDIRECTED;
    }

    private void logDetails(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute(USER_ID);
        if (userId != null) {
            log(ADMIN_LOGGED_OUT, userId);
        }

        Integer adminId = (Integer) request.getAttribute(ADMIN_ID);
        if (adminId != null) {
            log(USER_LOGGED_OUT, adminId);
        }
    }

    private void log(String logMessageFormat, Integer id) {
        logger.info(String.format(logMessageFormat, id));
    }

}
