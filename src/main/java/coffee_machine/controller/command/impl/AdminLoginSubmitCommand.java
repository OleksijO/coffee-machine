package coffee_machine.controller.command.impl;

import coffee_machine.controller.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.command.Pages.ADMIN_LOGIN_FAILED_PAGE;
import static coffee_machine.controller.command.Pages.ADMIN_LOGIN_SUCCESS_PAGE;

public class AdminLoginSubmitCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		if (true) return ADMIN_LOGIN_FAILED_PAGE;
		return ADMIN_LOGIN_SUCCESS_PAGE;
	}

}
