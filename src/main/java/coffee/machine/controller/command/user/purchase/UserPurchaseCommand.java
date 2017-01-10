package coffee.machine.controller.command.user.purchase;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.model.entity.Account;
import coffee.machine.service.AccountService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.TITLE_USER_PURCHASE;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.USER_PURCHASE_PAGE;

public class UserPurchaseCommand extends CommandWrapperTemplate {
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();

    public UserPurchaseCommand() {
        super(USER_PURCHASE_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        return USER_PURCHASE_PAGE;
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_USER_PURCHASE);
        request.setAttribute(ADMIN_CONTACTS, CoffeeMachineConfig.ADMIN_CONTACT_INFO);
        request.setAttribute(BALANCE_LOW_WARN_LIMIT, CoffeeMachineConfig.BALANCE_WARN_LIMIT);

        int userId = getUserIdFromSession(request.getSession());
        request.setAttribute(USER_BALANCE, accountService.getByUserId(userId)
                .map(Account::getRealAmount)
                .orElseThrow(IllegalStateException::new));
        request.setAttribute(DRINKS, drinkService.getAll());
    }

    private int getUserIdFromSession(HttpSession session) {
        return (int) session.getAttribute(USER_ID);
    }

}
