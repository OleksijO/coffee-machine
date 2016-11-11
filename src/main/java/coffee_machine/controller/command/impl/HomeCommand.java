package coffee_machine.controller.command.impl;

import static coffee_machine.controller.command.Pages.HOME_PAGE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coffee_machine.controller.command.Command;

public class HomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return HOME_PAGE;
	}

}
