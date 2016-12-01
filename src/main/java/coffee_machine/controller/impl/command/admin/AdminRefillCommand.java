package coffee_machine.controller.impl.command.admin;

import coffee_machine.CoffeeMachineConfig;
import coffee_machine.controller.Command;
import coffee_machine.controller.logging.ControllerErrorLogging;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.GeneralKey;
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

import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.ADMIN_REFILL_PAGE;

public class AdminRefillCommand implements Command, ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(AdminRefillCommand.class);

    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_REFILL);

        try {
            request.setAttribute(COFFEE_MACHINE_BALANCE,
                    accountService.getById(CoffeeMachineConfig.ACCOUNT_ID).getRealAmount());
            request.setAttribute(DRINKS, drinkService.getAll());
            request.setAttribute(ADDONS, addonService.getAll());
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

}
