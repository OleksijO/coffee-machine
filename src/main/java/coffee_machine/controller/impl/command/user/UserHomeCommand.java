package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.PagesPaths.USER_HOME_PAGE;

public class UserHomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

		return USER_HOME_PAGE;
	}

}
