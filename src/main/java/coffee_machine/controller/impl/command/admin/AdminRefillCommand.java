package coffee_machine.controller.impl.command.admin;

import coffee_machine.CoffeeMachineConfig;
import coffee_machine.controller.impl.command.CommandExecuteWrapper;
import coffee_machine.service.AccountService;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.i18n.message.key.GeneralKey.TITLE_ADMIN_REFILL;
import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.ADMIN_REFILL_PAGE;

/**
 * This class represents admin refill page get method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminRefillCommand extends CommandExecuteWrapper {
    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    public AdminRefillCommand() {
        super(ADMIN_REFILL_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // just putting all needed for jsp data
        request.setAttribute(PAGE_TITLE, TITLE_ADMIN_REFILL);
        request.setAttribute(COFFEE_MACHINE_BALANCE,
                accountService.getById(CoffeeMachineConfig.ACCOUNT_ID).getRealAmount());
        request.setAttribute(DRINKS, drinkService.getAll());
        request.setAttribute(ADDONS, addonService.getAll());

        return ADMIN_REFILL_PAGE;
    }

}
