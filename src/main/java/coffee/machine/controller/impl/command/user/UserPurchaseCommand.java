package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.helper.UserPurchaseCommandHelper;
import coffee.machine.service.AccountService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserPurchaseCommand extends CommandExecuteWrapper {
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();
    private UserPurchaseCommandHelper helper = new UserPurchaseCommandHelper();

    public UserPurchaseCommand() {
        super(PagesPaths.USER_PURCHASE_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // just putting all needed for jsp data
        helper.setGeneralRegisterPageAttributes(request);
        int userId = (int) request.getSession().getAttribute(Attributes.USER_ID);
        request.setAttribute(Attributes.USER_ACCOUNT, accountService.getByUserId(userId));
        request.setAttribute(Attributes.DRINKS, drinkService.getAll());
        return PagesPaths.USER_PURCHASE_PAGE;
    }

}
