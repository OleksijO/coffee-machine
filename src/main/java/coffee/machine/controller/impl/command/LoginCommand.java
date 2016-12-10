package coffee.machine.controller.impl.command;

import coffee.machine.controller.Command;
import coffee.machine.controller.impl.command.helper.LoginCommandHelper;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents user login get request page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginCommand implements Command {
	private LoginCommandHelper helper = new LoginCommandHelper();

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		helper.setGeneralLoginPageAttributes(request);
		return PagesPaths.LOGIN_PAGE;
	}

}
