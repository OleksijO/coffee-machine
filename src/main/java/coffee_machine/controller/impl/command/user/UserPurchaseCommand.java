package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.impl.command.CommandExecuteWrapper;
import coffee_machine.service.AccountService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.i18n.message.key.GeneralKey.TITLE_USER_PURCHASE;
import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.USER_PURCHASE_PAGE;

public class UserPurchaseCommand extends CommandExecuteWrapper {
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();

    public UserPurchaseCommand() {
        super(USER_PURCHASE_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // just putting all needed for jsp data
        request.setAttribute(PAGE_TITLE, TITLE_USER_PURCHASE);
        int userId = (int) request.getSession().getAttribute(USER_ID);
        request.setAttribute(USER_BALANCE, accountService.getByUserId(userId).getRealAmount());
        request.setAttribute(DRINKS, drinkService.getAll());
        return USER_PURCHASE_PAGE;
    }

}
