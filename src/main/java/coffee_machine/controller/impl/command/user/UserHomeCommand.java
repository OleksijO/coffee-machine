package coffee_machine.controller.impl.command.user;

import coffee_machine.view.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.GeneralKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.view.PagesPaths.USER_HOME_PAGE;

public class UserHomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_HOME);
		return USER_HOME_PAGE;
	}

}
