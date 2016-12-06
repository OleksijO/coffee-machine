package coffee.machine.controller.impl.command.admin;

import coffee.machine.CoffeeMachineConfig;
import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.model.entity.user.User;
import coffee.machine.service.AccountService;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Attributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static coffee.machine.view.Attributes.COFFEE_MACHINE_BALANCE;
import static coffee.machine.view.Attributes.PAGE_TITLE;
import static coffee.machine.view.PagesPaths.ADMIN_ADD_CREDITS_PAGE;

/**
 * Created by oleksij.onysymchuk@gmail on 06.12.2016.
 */
public class AdminAddCreditCommand extends CommandExecuteWrapper {
    UserService userService = UserServiceImpl.getInstance();
    AccountService accountService = AccountServiceImpl.getInstance();


    public AdminAddCreditCommand() {
        super(ADMIN_ADD_CREDITS_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(PAGE_TITLE, GeneralKey.TITLE_ADMIN_ADD_CREDIT);
        List<User> users = userService.getAllNonAdminUsers();
        request.setAttribute(Attributes.USER_LIST, users);
        request.setAttribute(COFFEE_MACHINE_BALANCE, accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                .getRealAmount());
        return ADMIN_ADD_CREDITS_PAGE;
    }
}
