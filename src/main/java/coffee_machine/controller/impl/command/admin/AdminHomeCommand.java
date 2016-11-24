package coffee_machine.controller.impl.command.admin;

import static coffee_machine.controller.PagesPaths.ADMIN_HOME_PAGE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.General;

public class AdminHomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		request.setAttribute(Attributes.PAGE_TITLE, General.TITLE_ADMIN_HOME);
		return ADMIN_HOME_PAGE;
	}

}
