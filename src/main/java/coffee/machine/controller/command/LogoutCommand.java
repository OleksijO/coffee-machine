package coffee.machine.controller.command;

import coffee.machine.controller.Command;
import coffee.machine.model.entity.user.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.Attributes.USER_ROLE;
import static coffee.machine.view.PagesPaths.HOME_PATH;
import static coffee.machine.view.PagesPaths.REDIRECTED;

/**
 * This class represents user logout request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LogoutCommand implements Command {
    private static final Logger logger = Logger.getLogger(LogoutCommand.class);

    private static final String USER_LOGGED_OUT = "%s id=%s LOGGED OUT.";
    private static final String REDIRECT_PATH_AFTER_LOGOUT = HOME_PATH;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logDetails(request.getSession());

        request.getSession().invalidate();

        response.sendRedirect(REDIRECT_PATH_AFTER_LOGOUT);
        return REDIRECTED;
    }

    private void logDetails(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(USER_ID);
        UserRole role = (UserRole) session.getAttribute(USER_ROLE);
        logger.info(String.format(USER_LOGGED_OUT, role.toString(), userId.toString()));
    }

}
