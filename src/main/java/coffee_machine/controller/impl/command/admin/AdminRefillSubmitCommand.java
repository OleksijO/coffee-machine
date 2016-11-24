package coffee_machine.controller.impl.command.admin;

import static coffee_machine.controller.PagesPaths.ADMIN_REFILL_PAGE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.General;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

public class AdminRefillSubmitCommand implements Command {
	private DrinkService drinkService = DrinkServiceImpl.getInstance();
	private AddonService addonService = AddonServiceImpl.getInstance();

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		// TODO refill logic

		request.setAttribute(Attributes.PAGE_TITLE, General.TITLE_ADMIN_REFILL);

		request.setAttribute("drink", drinkService.getAll());
		request.setAttribute("addons", addonService.getAll());
		request.setAttribute("message", "refilled");
		return ADMIN_REFILL_PAGE;
	}

}
