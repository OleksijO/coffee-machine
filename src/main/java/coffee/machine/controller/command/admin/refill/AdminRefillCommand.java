package coffee.machine.controller.command.admin.refill;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.AddonServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_ADMIN_REFILL;
import static coffee.machine.view.config.Attributes.*;
import static coffee.machine.view.config.Pages.ADMIN_REFILL_PAGE;

/**
 * This class represents admin refill page get method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminRefillCommand extends CommandWrapperTemplate {
    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    public AdminRefillCommand() {
        super(ADMIN_REFILL_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        return ADMIN_REFILL_PAGE;
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_ADMIN_REFILL);
        request.setAttribute(COFFEE_MACHINE_BALANCE,
                accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                        .orElseThrow(IllegalStateException::new)
                        .getRealAmount());
        request.setAttribute(DRINKS, drinkService.getAll());
        request.setAttribute(ADDONS, addonService.getAll());

    }
}
