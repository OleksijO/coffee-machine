package coffee_machine.controller.impl.command.admin;

import coffee_machine.view.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.GeneralKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.view.PagesPaths.ADMIN_LOGIN_PAGE;

public class AdminLoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_LOGIN);
		return ADMIN_LOGIN_PAGE;
	}

}
