package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.Command;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.PagesPaths.HOME_PATH;
import static coffee.machine.view.PagesPaths.REDIRECTED;

/**
 * This class represents user logout request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserLogoutCommand implements Command {
	private static final Logger logger = Logger.getLogger(UserLogoutCommand.class);

	private static final String USER_LOGGED_OUT = "USER id=%d LOGGED OUT.";
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.info(String.format(USER_LOGGED_OUT, (int) request.getSession().getAttribute(Attributes.USER_ID)));

		request.getSession().removeAttribute(Attributes.USER_ID);
		response.sendRedirect(HOME_PATH);

		return REDIRECTED;
	}

}