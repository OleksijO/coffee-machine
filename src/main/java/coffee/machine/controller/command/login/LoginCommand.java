package coffee.machine.controller.command.login;

import coffee.machine.controller.Command;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee.machine.view.PagesPaths.LOGIN_PATH;

/**
 * This class represents user login get request page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_LOGIN);
		request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_FORM_TITLE);
		request.setAttribute(Attributes.LOGIN_FORM_ACTION, LOGIN_PATH);
		return PagesPaths.LOGIN_PAGE;
	}

}
