package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.view.Attributes;
import coffee_machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.view.PagesPaths.LOGIN_PAGE;

public class UserLoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_LOGIN);
		request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_USER_FORM_TITLE);
		request.setAttribute(Attributes.LOGIN_FORM_ACTION, PagesPaths.USER_LOGIN_PATH);
		return LOGIN_PAGE;
	}

}
