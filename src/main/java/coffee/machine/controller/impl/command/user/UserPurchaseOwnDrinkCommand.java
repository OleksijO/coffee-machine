package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.helper.UserPurchaseCommandHelper;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.AddonServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserPurchaseOwnDrinkCommand extends CommandExecuteWrapper {
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AddonService addonService = AddonServiceImpl.getInstance();
    private UserPurchaseCommandHelper helper = new UserPurchaseCommandHelper();

    public UserPurchaseOwnDrinkCommand() {
        super(PagesPaths.USER_PURCHASE_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        helper.setGeneralRegisterPageAttributes(request);
        request.setAttribute(Attributes.DRINKS, drinkService.getAll());
        request.setAttribute(Attributes.ADDONS, addonService.getAll());
        return PagesPaths.USER_PURCHASE_OWN_PAGE;
    }

}
