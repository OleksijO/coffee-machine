package coffee.machine.controller.command.user.purchase;

import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.model.entity.Account;
import coffee.machine.service.AccountService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.USER_PURCHASE_PAGE;

public class UserPurchaseCommand extends CommandWrapperTemplate {
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();
    private UserPurchaseCommandHelper helper = new UserPurchaseCommandHelper();

    public UserPurchaseCommand() {
        super(USER_PURCHASE_PAGE);
    }


    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int userId = (int) request.getSession().getAttribute(USER_ID);
        request.setAttribute(USER_BALANCE, accountService.getByUserId(userId)
                .map(Account::getRealAmount)
                .orElseThrow(IllegalStateException::new));
        request.setAttribute(DRINKS, drinkService.getAll());
        return USER_PURCHASE_PAGE;
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        helper.setGeneralRegisterPageAttributes(request);
    }

}
