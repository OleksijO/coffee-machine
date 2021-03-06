package coffee.machine.controller.command.admin.add.credit;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.service.AccountService;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_ADMIN_ADD_CREDIT;
import static coffee.machine.view.config.Attributes.*;
import static coffee.machine.view.config.Pages.ADMIN_ADD_CREDITS_PAGE;

/**
 * This class represents admin's add credits get method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminAddCreditCommand extends CommandWrapperTemplate {
    private UserService userService = UserServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();


    public AdminAddCreditCommand() {
        super(ADMIN_ADD_CREDITS_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {
        return ADMIN_ADD_CREDITS_PAGE;
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_ADMIN_ADD_CREDIT);
        request.setAttribute(USER_LIST, userService.getAllNonAdminUsers());
        request.setAttribute(COFFEE_MACHINE_BALANCE,
                accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                        .orElseThrow(IllegalStateException::new)
                        .getRealAmount());
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
