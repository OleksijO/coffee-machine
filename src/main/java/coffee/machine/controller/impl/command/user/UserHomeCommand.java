package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.Command;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents user home page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserHomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_HOME);
		return PagesPaths.USER_HOME_PAGE;
	}

}
