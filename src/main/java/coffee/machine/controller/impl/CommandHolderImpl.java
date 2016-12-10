package coffee.machine.controller.impl;

import coffee.machine.controller.Command;
import coffee.machine.controller.CommandHolder;
import coffee.machine.controller.impl.command.HomeCommand;
import coffee.machine.controller.impl.command.LoginCommand;
import coffee.machine.controller.impl.command.LoginSubmitCommand;
import coffee.machine.controller.impl.command.admin.*;
import coffee.machine.controller.impl.command.user.*;

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

            put(USER_LOGOUT_PATH, new UserLogoutCommand());
            put(USER_REGISTER_PATH, new UserRegisterCommand());
            put(USER_PURCHASE_PATH, new UserPurchaseCommand());
            put(USER_ORDER_HISTORY_PATH, new UserOrderHistoryCommand());

            put(ADMIN_LOGOUT_PATH, new AdminLogoutCommand());
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
