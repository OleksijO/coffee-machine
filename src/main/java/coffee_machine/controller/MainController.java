package coffee_machine.controller;

import static coffee_machine.controller.command.Pages.HOME_PATH;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import coffee_machine.controller.command.Command;
import coffee_machine.controller.command.impl.AdminLoginCommand;
import coffee_machine.controller.command.impl.HomeCommand;
import coffee_machine.controller.command.impl.UserLoginCommand;

/**
 * Servlet implementation class MainController
 */
// @WebServlet("/*")
public class MainController extends HttpServlet {
	private static final Logger logger = Logger.getLogger(MainController.class);
	private static final long serialVersionUID = 1L;
	private Map<String, Command> commands = new HashMap<>();

	@Override
	public void init() throws ServletException {
		super.init();
		commands.put("/home", new HomeCommand());
		commands.put("/user/login", new UserLoginCommand());
		commands.put("/admin/login", new AdminLoginCommand());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		logger.debug("Requested uri = " + uri);
		Command command = commands.get(uri);
		if (command == null) {
			logger.debug("Redirecting to home page");
			response.sendRedirect(HOME_PATH);
			return;
		}
		String view = command.execute(request, response);
		logger.debug("Forwarding to " + view);
		request.getRequestDispatcher(view).forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
