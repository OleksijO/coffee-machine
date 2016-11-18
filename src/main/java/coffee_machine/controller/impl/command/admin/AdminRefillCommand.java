package coffee_machine.controller.impl.command.admin;

import coffee_machine.controller.Command;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.PagesPaths.ADMIN_REFILL_PAGE;


public class AdminRefillCommand implements Command {
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AddonService addonService = AddonServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("drink", drinkService.getAll());
        request.setAttribute("addons", addonService.getAll());
        return ADMIN_REFILL_PAGE;
    }

}
