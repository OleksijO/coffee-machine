package coffee_machine.controller.impl.command.admin;

import coffee_machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.view.Attributes.ADMIN_ID;
import static coffee_machine.view.PagesPaths.HOME_PATH;
import static coffee_machine.view.PagesPaths.REDIRECTED;

public class AdminLogoutCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.getSession().removeAttribute(ADMIN_ID);

		response.sendRedirect(HOME_PATH);

		return REDIRECTED;
	}

}
