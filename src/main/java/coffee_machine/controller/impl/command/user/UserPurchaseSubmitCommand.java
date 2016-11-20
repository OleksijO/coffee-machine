package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.Command;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.Messages.TEST_USUAL_MESSAGE;
import static coffee_machine.controller.Attributes.USUAL_MESSAGE;

public class UserPurchaseSubmitCommand implements Command {
	private DrinkService drinkService = DrinkServiceImpl.getInstance();
	private AddonService addonService = AddonServiceImpl.getInstance();

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {



		request.setAttribute(USUAL_MESSAGE, TEST_USUAL_MESSAGE);

		//TODO remove hardcore harcode below )
		return new UserPurchaseCommand().execute(request,response);
	}

}
