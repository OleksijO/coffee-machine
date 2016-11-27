package coffee_machine.controller.impl.command.admin;

import coffee_machine.CoffeeMachineAccountConfig;
import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.controller.impl.command.abstracts.AbstractCommand;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.service.AccountService;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.Attributes.*;
import static coffee_machine.controller.PagesPaths.ADMIN_REFILL_PAGE;

public class AdminRefillCommand extends AbstractCommand implements Command {
    private static final Logger logger = Logger.getLogger(AdminRefillCommand.class);

    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AddonService addonService = AddonServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();


    public AdminRefillCommand() {
        super(logger);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_REFILL);

        try {
            request.setAttribute(COFFEE_MACHINE_BALANCE,
                    accountService.getById(CoffeeMachineAccountConfig.ID).getRealAmount());
            request.setAttribute(REFILL_DRINKS, drinkService.getAll());
            request.setAttribute(REFILL_ADDONS, addonService.getAll());
        } catch (ApplicationException e) {
            logApplicationError(e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }

        return ADMIN_REFILL_PAGE;
    }

}
