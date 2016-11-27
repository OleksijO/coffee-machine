package coffee_machine.controller.impl.command;

import coffee_machine.view.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.GeneralKey;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static coffee_machine.view.Attributes.ERROR_MESSAGE;
import static coffee_machine.view.Attributes.USUAL_MESSAGE;
import static coffee_machine.view.PagesPaths.HOME_PAGE;
import static coffee_machine.i18n.message.key.GeneralKey.TEST_ERROR_MESSAGE;
import static coffee_machine.i18n.message.key.GeneralKey.TEST_USUAL_MESSAGE;

public class HomeCommand implements Command {
	private static final Logger logger = Logger.getLogger(HomeCommand.class);
	private static int counter = 0; // TODO DELETE AFTER TESTING

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO DELETE HARDCODE AFTER TESTS

		if (counter % 2 == 0) {
			logger.debug(counter+" usual message");
			request.setAttribute(USUAL_MESSAGE, TEST_USUAL_MESSAGE);
		}
		if (counter % 3 == 0) {
			logger.debug(counter+" error message");
			request.setAttribute(ERROR_MESSAGE, TEST_ERROR_MESSAGE);
		}
		counter++;
		// TODO end of hardcode
		request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_HOME);
		return HOME_PAGE;
	}

}
