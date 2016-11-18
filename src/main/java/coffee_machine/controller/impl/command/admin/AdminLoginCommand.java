package coffee_machine.controller.impl.command.admin;

import coffee_machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.PagesPaths.ADMIN_LOGIN_PAGE;

public class AdminLoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return ADMIN_LOGIN_PAGE;
	}

}
