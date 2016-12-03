package coffee.machine.controller.impl.command.admin;

import coffee.machine.controller.Command;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class represents admin logout request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminLogoutCommand implements Command {
    private static final Logger logger = Logger.getLogger(AdminLogoutCommand.class);

    private static final String ADMIN_LOGGED_OUT = "ADMIN id=%d LOGGED OUT.";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.info(String.format(ADMIN_LOGGED_OUT, (int) request.getSession().getAttribute(Attributes.ADMIN_ID)));

        request.getSession().removeAttribute(Attributes.ADMIN_ID);
        response.sendRedirect(PagesPaths.HOME_PATH);
        return PagesPaths.REDIRECTED;
    }

}
