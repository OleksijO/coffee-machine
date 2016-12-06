package coffee.machine.controller.impl;

import coffee.machine.controller.impl.command.admin.*;
import coffee.machine.controller.impl.command.user.*;
import coffee.machine.view.PagesPaths;
import coffee.machine.controller.Command;
import coffee.machine.controller.CommandHolder;
import coffee.machine.controller.impl.command.HomeCommand;

import java.util.HashMap;
import java.util.Map;

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
            put(PagesPaths.HOME_PATH, new HomeCommand());

            put(PagesPaths.USER_LOGIN_PATH, new UserLoginCommand());
            put(PagesPaths.USER_LOGOUT_PATH, new UserLogoutCommand());
            put(PagesPaths.USER_HOME_PATH, new UserHomeCommand());
            put(PagesPaths.USER_PURCHASE_PATH, new UserPurchaseCommand());
            put(PagesPaths.USER_ORDER_HISTORY_PATH, new UserOrderHistoryCommand());

            put(PagesPaths.ADMIN_LOGIN_PATH, new AdminLoginCommand());
            put(PagesPaths.ADMIN_LOGOUT_PATH, new AdminLogoutCommand());
            put(PagesPaths.ADMIN_REFILL_PATH, new AdminRefillCommand());
            put(PagesPaths.ADMIN_HOME_PATH, new AdminHomeCommand());
            put(PagesPaths.ADMIN_ADD_CREDITS_PATH, new AdminAddCreditCommand());
        }
    };

    /**
     * Holder for POST commands
     */
    private final Map<String, Command> postCommands = new HashMap<String, Command>() {
        {
            put(PagesPaths.USER_LOGIN_PATH, new UserLoginSubmitCommand());
            put(PagesPaths.USER_PURCHASE_PATH, new UserPurchaseSubmitCommand());

            put(PagesPaths.ADMIN_REFILL_PATH, new AdminRefillSubmitCommand());
            put(PagesPaths.ADMIN_LOGIN_PATH, new AdminLoginSubmitCommand());
            put(PagesPaths.ADMIN_ADD_CREDITS_PATH, new AdminAddCreditSubmitCommand());
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
