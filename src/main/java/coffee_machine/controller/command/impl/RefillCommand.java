package coffee_machine.controller.command.impl;

import coffee_machine.controller.command.Command;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static coffee_machine.config.Constants.ADDON_PARAMETER_STARTS_WITH;
import static coffee_machine.config.Constants.DRINK_PARAMETER_STARTS_WITH;
import static coffee_machine.controller.command.Pages.REFILL_PAGE;

public class RefillCommand implements Command {
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AddonService addonService = AddonServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Map<Integer, Integer> drinksToRefill = new HashMap<>();
        Map<Integer, Integer> addonsToRefill = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            pushParameterToMapIfStartsWith(paramName, DRINK_PARAMETER_STARTS_WITH, drinksToRefill,request);
            pushParameterToMapIfStartsWith(paramName, ADDON_PARAMETER_STARTS_WITH, drinksToRefill,request);
        }
        drinkService.refill(drinksToRefill);
        addonService.refill(addonsToRefill);

        request.setAttribute("drink", drinkService.getAll());
        request.setAttribute("addons", addonService.getAll());
        return REFILL_PAGE;
    }


    private void pushParameterToMapIfStartsWith(String param, String startsWith,
                                                Map<Integer, Integer> quantitiesById, HttpServletRequest request) {
        if (param.startsWith(startsWith)) {
            int id = Integer.parseInt(param.replace(startsWith, ""));
            int quantity = Integer.parseInt(request.getParameter(param));
            if (quantity<=0){
                return;
            }
            quantitiesById.put(id, quantity);
        }
    }
}
