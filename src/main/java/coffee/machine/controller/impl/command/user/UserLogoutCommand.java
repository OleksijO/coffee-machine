package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.Command;
import coffee.machine.controller.impl.command.helper.LogoutCommandHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.PagesPaths.REDIRECTED;

/**
 * This class represents user logout request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserLogoutCommand implements Command {
	private LogoutCommandHelper helper = new LogoutCommandHelper();

	private static final String USER_LOGGED_OUT = "USER id=%d LOGGED OUT.";

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

		helper.performActionsToLogOutOfRole(request, response, USER_LOGGED_OUT, USER_ID);
		return REDIRECTED;
	}

}
