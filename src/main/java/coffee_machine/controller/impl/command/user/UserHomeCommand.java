package coffee_machine.controller.impl.command.user;

import static coffee_machine.controller.PagesPaths.USER_HOME_PAGE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coffee_machine.Messages;
import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;

public class UserHomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		request.setAttribute(Attributes.PAGE_TITLE, Messages.TITLE_USER_HOME);
		return USER_HOME_PAGE;
	}

}
