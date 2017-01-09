package coffee.machine.controller.command.login;

import coffee.machine.controller.Command;
import coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.TITLE_LOGIN;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.LOGIN_PATH;

/**
 * This class represents user login get request page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(PAGE_TITLE, TITLE_LOGIN);
		request.setAttribute(LOGIN_FORM_TITLE, ServiceErrorMessageKey.LOGIN_FORM_TITLE);
		request.setAttribute(LOGIN_FORM_ACTION, LOGIN_PATH);
		return PagesPaths.LOGIN_PAGE;
	}

}
