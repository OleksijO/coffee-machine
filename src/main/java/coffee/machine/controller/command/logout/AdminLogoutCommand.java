package coffee.machine.controller.command.logout;

import coffee.machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.ADMIN_ID;
import static coffee.machine.view.PagesPaths.REDIRECTED;

/**
 * This class represents admin logout request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminLogoutCommand implements Command {
    private static final String ADMIN_LOGGED_OUT = "ADMIN id=%d LOGGED OUT.";

    private LogoutCommandHelper helper = new LogoutCommandHelper();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        helper.performActionsToLogOutOfRole(request, response, ADMIN_LOGGED_OUT, ADMIN_ID);
        return REDIRECTED;
    }

}
