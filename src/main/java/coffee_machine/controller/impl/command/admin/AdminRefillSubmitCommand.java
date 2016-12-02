package coffee_machine.controller.impl.command.admin;

import coffee_machine.CoffeeMachineConfig;
import coffee_machine.controller.Command;
import coffee_machine.controller.impl.command.request.data.extractor.RefillFormDataExtractor;
import coffee_machine.controller.impl.command.request.data.extractor.impl.RefillFormExtractorImpl;
import coffee_machine.controller.logging.ControllerErrorLogging;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.CommandKey;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.i18n.message.key.error.CommandErrorKey;
import coffee_machine.service.AccountService;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;
import coffee_machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.ADMIN_REFILL_PAGE;

public class AdminRefillSubmitCommand implements Command, ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(AdminRefillCommand.class);

    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    private final RefillFormDataExtractor formParser = new RefillFormExtractorImpl();



    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_REFILL);
        try {

            return performExecute(request);

        } catch (ApplicationException e) {
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }

        return ADMIN_REFILL_PAGE;
    }

    private String performExecute(HttpServletRequest request) {
        Map<Integer, Integer> drinkAddQuantityByIds = formParser.getDrinksQuantityByIdFromRequest(request);
        Map<Integer, Integer> addonAddQuantityByIds = formParser.getAddonsQuantityByIdFromRequest(request);

        boolean drinksAdded = false;
        boolean addonsAdded = false;
        if ((drinkAddQuantityByIds != null) && (drinkAddQuantityByIds.size() > 0)) {
            drinkService.refill(drinkAddQuantityByIds);
            drinksAdded = true;
        }
        if ((addonAddQuantityByIds != null) && (addonAddQuantityByIds.size() > 0)) {
            addonService.refill(addonAddQuantityByIds);
            addonsAdded = true;
        }

        if (drinksAdded || addonsAdded) {
            request.setAttribute(USUAL_MESSAGE, CommandKey.ADMIN_REFILL_SUCCESSFULL);

        } else {
            request.setAttribute(ERROR_MESSAGE, CommandErrorKey.ADMIN_REFILL_NOTHING_TO_ADD);
        }

        request.setAttribute(COFFEE_MACHINE_BALANCE, accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                .getRealAmount());
        request.setAttribute(DRINKS, drinkService.getAll());
        request.setAttribute(ADDONS, addonService.getAll());

        return ADMIN_REFILL_PAGE;
    }


}
