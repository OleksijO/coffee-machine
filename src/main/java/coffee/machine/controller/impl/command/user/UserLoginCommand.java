package coffee.machine.controller.impl.command.user;

import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import coffee.machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents user login get request page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserLoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_LOGIN);
		request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_USER_FORM_TITLE);
		request.setAttribute(Attributes.LOGIN_FORM_ACTION, PagesPaths.USER_LOGIN_PATH);
		return PagesPaths.LOGIN_PAGE;
	}

}
