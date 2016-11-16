package coffee_machine.controller.command.impl;

import coffee_machine.controller.command.Command;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.command.Pages.REFILLED_PAGE;

public class RefillSubmitCommand implements Command {
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AddonService addonService = AddonServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("drink", drinkService.getAll());
        request.setAttribute("addons", addonService.getAll());
        return REFILLED_PAGE;
    }

}
