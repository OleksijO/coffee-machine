package coffee.machine.controller.impl.command;

import coffee.machine.view.PagesPaths;
import coffee.machine.controller.Command;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents home page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class HomeCommand implements Command {
	private static final Logger logger = Logger.getLogger(HomeCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_HOME);
		return PagesPaths.HOME_PAGE;

	}

}
