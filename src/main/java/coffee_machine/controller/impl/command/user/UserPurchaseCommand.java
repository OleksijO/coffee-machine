package coffee_machine.controller.impl.command.user;

import static coffee_machine.controller.Attributes.REFILL_ADDONS;
import static coffee_machine.controller.Attributes.REFILL_DRINKS;
import static coffee_machine.controller.Attributes.USER_BALANCE;
import static coffee_machine.controller.Attributes.USER_ID;
import static coffee_machine.controller.PagesPaths.USER_PURCHASE_PAGE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.Messages;
import coffee_machine.model.entity.goods.Addon;
import coffee_machine.model.entity.goods.Drink;
import coffee_machine.service.AccountService;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

public class UserPurchaseCommand implements Command {
	private static final Logger logger = Logger.getLogger(UserPurchaseCommand.class);
	private DrinkService drinkService = DrinkServiceImpl.getInstance();
	private AddonService addonService = AddonServiceImpl.getInstance();
	private AccountService accountService = AccountServiceImpl.getInstance();

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		int userId = (int) request.getSession().getAttribute(USER_ID);
		request.setAttribute(Attributes.PAGE_TITLE, Messages.TITLE_USER_PURCHASE);
		// request.setAttribute(USER_BALANCE,
		// accountService.getByUserId(userId).getAmount()*DB_MONEY_COEFF);
		request.setAttribute(USER_BALANCE, 150.);
		request.setAttribute(REFILL_DRINKS, drinkService.getAll());
		request.setAttribute(REFILL_ADDONS, addonService.getAll());

		// TODO DELETE HARDCODE
		Addon addon1 = new Addon();
		addon1.setId(1);
		addon1.setQuantity(10);
		addon1.setName("Sugar");
		addon1.setPrice(500);

		Addon addon2 = new Addon();
		addon2.setId(2);
		addon2.setQuantity(15);
		addon2.setName("Milk");
		addon2.setPrice(700);

		Addon addon3 = new Addon();
		addon3.setId(3);
		addon3.setQuantity(0);
		addon3.setName("Cream");
		addon3.setPrice(800);

		Addon addon4 = new Addon();
		addon4.setId(3);
		addon4.setQuantity(7);
		addon4.setName("Lemon");
		addon4.setPrice(300);

		Set<Addon> addons1Set = new HashSet<>();
		addons1Set.add(addon1);
		addons1Set.add(addon4);

		Set<Addon> addons2Set = new HashSet<>();
		addons2Set.add(addon1);
		addons2Set.add(addon4);

		Set<Addon> addons3Set = new HashSet<>();
		addons3Set.add(addon1);
		addons3Set.add(addon2);
		addons3Set.add(addon3);

		Set<Addon> addons4Set = new HashSet<>();
		addons4Set.add(addon2);
		addons4Set.add(addon3);

		Drink drink1 = new Drink();
		drink1.setId(1);
		drink1.setQuantity(2);
		drink1.setName("Tea without sugar");
		drink1.setPrice(1000);
		drink1.setAddons(addons1Set);

		Drink drink2 = new Drink();
		drink2.setId(2);
		drink2.setQuantity(23);
		drink2.setName("Tea");
		drink2.setPrice(1500);
		drink2.setAddons(addons2Set);

		Drink drink3 = new Drink();
		drink3.setId(3);
		drink3.setQuantity(32);
		drink3.setName("Espresso");
		drink3.setPrice(2000);
		drink3.setAddons(addons3Set);

		Drink drink4 = new Drink();
		drink4.setId(3);
		drink4.setQuantity(32);
		drink4.setName("Americano");
		drink4.setPrice(2200);
		drink4.setAddons(addons4Set);

		List<Drink> drinks = new ArrayList<>();
		drinks.add(drink1);
		drinks.add(drink2);
		drinks.add(drink3);
		drinks.add(drink4);

		request.setAttribute(REFILL_DRINKS, drinks);
		request.setAttribute(REFILL_ADDONS, addons1Set);

		// TODO end of hardcode

		return USER_PURCHASE_PAGE;
	}

}
