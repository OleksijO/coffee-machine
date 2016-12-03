package coffee.machine.controller.impl.command.admin;

import coffee.machine.CoffeeMachineConfig;
import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.AddonServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        super(PagesPaths.ADMIN_REFILL_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // just putting all needed for jsp data
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_REFILL);
        request.setAttribute(Attributes.COFFEE_MACHINE_BALANCE,
                accountService.getById(CoffeeMachineConfig.ACCOUNT_ID).getRealAmount());
        request.setAttribute(Attributes.DRINKS, drinkService.getAll());
        request.setAttribute(Attributes.ADDONS, addonService.getAll());

        return PagesPaths.ADMIN_REFILL_PAGE;
    }

}
