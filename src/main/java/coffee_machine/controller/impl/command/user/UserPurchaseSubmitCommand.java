package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.Command;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.Attributes.REFILL_ADDONS;
import static coffee_machine.controller.Attributes.REFILL_DRINKS;
import static coffee_machine.controller.PagesPaths.USER_PURCHASE_PAGE;

public class UserPurchaseSubmitCommand implements Command {
	private DrinkService drinkService = DrinkServiceImpl.getInstance();
	private AddonService addonService = AddonServiceImpl.getInstance();

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(REFILL_DRINKS, drinkService.getAll());
		request.setAttribute(REFILL_ADDONS, addonService.getAll());

		// TODO DELETE HARDCODE

		//TODO end of hardcode


		return USER_PURCHASE_PAGE;
	}

}
