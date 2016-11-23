package coffee_machine.controller.impl.command.admin;

import static coffee_machine.controller.Attributes.ADMIN_ID;
import static coffee_machine.controller.PagesPaths.HOME_PATH;
import static coffee_machine.controller.PagesPaths.REDIRECTED;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coffee_machine.controller.Command;

public class AdminLogoutCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.getSession().removeAttribute(ADMIN_ID);

		response.sendRedirect(HOME_PATH);

		return REDIRECTED;
	}

}
