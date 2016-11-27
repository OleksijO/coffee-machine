package coffee_machine.controller.impl.command;

import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.view.PagesPaths.HOME_PAGE;

public class HomeCommand implements Command {
	private static final Logger logger = Logger.getLogger(HomeCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_HOME);
		return HOME_PAGE;
	}

}
