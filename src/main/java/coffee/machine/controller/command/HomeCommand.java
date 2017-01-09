package coffee.machine.controller.command;

import coffee.machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.TITLE_HOME;
import static coffee.machine.view.Attributes.PAGE_TITLE;
import static coffee.machine.view.PagesPaths.HOME_PAGE;

/**
 * This class represents home page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class HomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute(PAGE_TITLE, TITLE_HOME);
		return HOME_PAGE;

	}

}
