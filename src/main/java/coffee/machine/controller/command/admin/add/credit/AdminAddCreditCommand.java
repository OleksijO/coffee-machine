package coffee.machine.controller.command.admin.add.credit;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.model.entity.User;
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
 * Created by oleksij.onysymchuk@gmail
 */
public class AdminAddCreditCommand extends CommandWrapperTemplate {
    UserService userService = UserServiceImpl.getInstance();
    AccountService accountService = AccountServiceImpl.getInstance();


    public AdminAddCreditCommand() {
        super(ADMIN_ADD_CREDITS_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {

        List<User> users = userService.getAllNonAdminUsers();
        request.setAttribute(Attributes.USER_LIST, users);
        request.setAttribute(COFFEE_MACHINE_BALANCE, accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                .getRealAmount());
        return ADMIN_ADD_CREDITS_PAGE;
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, GeneralKey.TITLE_ADMIN_ADD_CREDIT);
    }
}
