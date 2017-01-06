package coffee.machine.controller;

import coffee.machine.controller.command.HomeCommand;
import coffee.machine.controller.command.admin.add.credit.AdminAddCreditCommand;
import coffee.machine.controller.command.admin.add.credit.AdminAddCreditSubmitCommand;
import coffee.machine.controller.command.admin.refill.AdminRefillCommand;
import coffee.machine.controller.command.admin.refill.AdminRefillSubmitCommand;
import coffee.machine.controller.command.login.LoginCommand;
import coffee.machine.controller.command.login.LoginSubmitCommand;
import coffee.machine.controller.command.logout.LogoutCommand;
import coffee.machine.controller.command.user.order.history.UserOrderHistoryCommand;
import coffee.machine.controller.command.user.purchase.UserPurchaseCommand;
import coffee.machine.controller.command.user.purchase.UserPurchaseSubmitCommand;
import coffee.machine.controller.command.user.register.UserRegisterCommand;
import coffee.machine.controller.command.user.register.UserRegisterSubmitCommand;

import java.util.HashMap;
import java.util.Map;

import static coffee.machine.view.PagesPaths.*;

/**
 * This class is implementation of CommandHolder. It defines command for every supported request uri.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class CommandHolderImpl implements CommandHolder {
    /**
     * Holder for GET commands
     */
    private final Map<String, Command> getCommands = new HashMap<String, Command>() {
        {
            put(HOME_PATH, new HomeCommand());
            put(LOGIN_PATH, new LoginCommand());

            put(LOGOUT_PATH, new LogoutCommand());
            put(USER_REGISTER_PATH, new UserRegisterCommand());
            put(USER_PURCHASE_PATH, new UserPurchaseCommand());
            put(USER_ORDER_HISTORY_PATH, new UserOrderHistoryCommand());

            put(ADMIN_REFILL_PATH, new AdminRefillCommand());
            put(ADMIN_ADD_CREDITS_PATH, new AdminAddCreditCommand());
        }
    };

    /**
     * Holder for POST commands
     */
    private final Map<String, Command> postCommands = new HashMap<String, Command>() {
        {
            put(LOGIN_PATH, new LoginSubmitCommand());
            put(USER_REGISTER_PATH, new UserRegisterSubmitCommand());
            put(USER_PURCHASE_PATH, new UserPurchaseSubmitCommand());

            put(ADMIN_REFILL_PATH, new AdminRefillSubmitCommand());
            put(ADMIN_ADD_CREDITS_PATH, new AdminAddCreditSubmitCommand());
        }
    };

    @Override
    public Command get(String path) {
        return getCommands.get(path);
    }

    @Override
    public Command post(String path) {
        return postCommands.get(path);
    }

}
