package coffee.machine.controller.impl.command.admin;

import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import coffee.machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents administrator home page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminHomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_HOME);
		return PagesPaths.ADMIN_HOME_PAGE;
	}

}
