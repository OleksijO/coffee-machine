package coffee.machine.controller.command.login;

import coffee.machine.controller.Command;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_FOR_LOGIN_FORM;
import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_LOGIN;
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
		request.setAttribute(LOGIN_FORM_TITLE, TITLE_FOR_LOGIN_FORM);
		request.setAttribute(LOGIN_FORM_ACTION, LOGIN_PATH);
		return PagesPaths.LOGIN_PAGE;
	}

}
