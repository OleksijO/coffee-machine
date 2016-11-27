package coffee_machine.controller.impl.command.admin;

import coffee_machine.controller.Command;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.view.Attributes.ADMIN_ID;
import static coffee_machine.view.PagesPaths.HOME_PATH;
import static coffee_machine.view.PagesPaths.REDIRECTED;

public class AdminLogoutCommand implements Command {
	private static final Logger logger = Logger.getLogger(AdminLogoutCommand.class);

	private static final String ADMIN_LOGGED_OUT = "ADMIN id=%d LOGGED OUT.";
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.info(String.format(ADMIN_LOGGED_OUT, (int) request.getSession().getAttribute(ADMIN_ID)));

		request.getSession().removeAttribute(ADMIN_ID);

		response.sendRedirect(HOME_PATH);

		return REDIRECTED;
	}

}
