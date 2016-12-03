package coffee_machine.controller.impl.command.admin;

import coffee_machine.CoffeeMachineConfig;
import coffee_machine.controller.impl.command.CommandExecuteWrapper;
import coffee_machine.controller.impl.command.request.data.extractor.RefillFormDataExtractor;
import coffee_machine.controller.impl.command.request.data.extractor.impl.RefillFormExtractorImpl;
import coffee_machine.i18n.message.key.CommandKey;
import coffee_machine.i18n.message.key.error.CommandErrorKey;
import coffee_machine.service.AccountService;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static coffee_machine.i18n.message.key.GeneralKey.TITLE_ADMIN_REFILL;
import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.ADMIN_REFILL_PAGE;

/**
 * This class represents admin refill page post method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminRefillSubmitCommand extends CommandExecuteWrapper {
    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    private final RefillFormDataExtractor formParser = new RefillFormExtractorImpl();

    public AdminRefillSubmitCommand() {
        super(ADMIN_REFILL_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute(PAGE_TITLE, TITLE_ADMIN_REFILL);

        Map<Integer, Integer> drinkAddQuantityByIds = formParser.getDrinksQuantityByIdFromRequest(request);
        Map<Integer, Integer> addonAddQuantityByIds = formParser.getAddonsQuantityByIdFromRequest(request);

        // check if we perform updating drinks or addons to select corresponding message
        boolean itemsAdded = false;
        if ((drinkAddQuantityByIds != null) && (drinkAddQuantityByIds.size() > 0)) {
            drinkService.refill(drinkAddQuantityByIds);
            itemsAdded = true;
        }
        if ((addonAddQuantityByIds != null) && (addonAddQuantityByIds.size() > 0)) {
            addonService.refill(addonAddQuantityByIds);
            itemsAdded = true;
        }

        // placing correspondent message or error for view
        if (itemsAdded) {
            request.setAttribute(USUAL_MESSAGE, CommandKey.ADMIN_REFILL_SUCCESSFULL);
        } else {
            request.setAttribute(ERROR_MESSAGE, CommandErrorKey.ADMIN_REFILL_NOTHING_TO_ADD);
        }

        // placing necessary for view data
        request.setAttribute(COFFEE_MACHINE_BALANCE, accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                .getRealAmount());
        request.setAttribute(DRINKS, drinkService.getAll());
        request.setAttribute(ADDONS, addonService.getAll());

        return ADMIN_REFILL_PAGE;
    }


}
