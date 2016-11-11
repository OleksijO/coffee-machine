package coffee_machine.controller.command.impl;

import static coffee_machine.controller.command.Pages.USER_LOGIN_PAGE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coffee_machine.controller.command.Command;

public class UserLoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return USER_LOGIN_PAGE;
	}

}
