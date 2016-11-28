package coffee_machine.controller.impl;

import coffee_machine.controller.Command;
import coffee_machine.controller.CommandHolder;
import coffee_machine.controller.impl.command.HomeCommand;
import coffee_machine.controller.impl.command.admin.*;
import coffee_machine.controller.impl.command.user.*;

import java.util.HashMap;
import java.util.Map;

import static coffee_machine.view.PagesPaths.*;

/**
 * Created by oleksij.onysymchuk@gmail on 17.11.2016.
 */
public class CommandHolderImpl implements CommandHolder {
    private final Map<String, Command> getCommands = new HashMap<String, Command>() {
        {
            put(HOME_PATH, new HomeCommand());

            put(USER_LOGIN_PATH, new UserLoginCommand());
            put(USER_LOGOUT_PATH, new UserLogoutCommand());
            put(USER_HOME_PATH, new UserHomeCommand());
            put(USER_PURCHASE_PATH, new UserPurchaseCommand());
            put(USER_HISTORY_PATH, new UserHistoryCommand());

            put(ADMIN_LOGIN_PATH, new AdminLoginCommand());
            put(ADMIN_LOGOUT_PATH, new AdminLogoutCommand());
            put(ADMIN_REFILL_PATH, new AdminRefillCommand());
            put(ADMIN_HOME_PATH, new AdminHomeCommand());
        }
    };
    private final Map<String, Command> postCommands = new HashMap<String, Command>() {
        {
            put(USER_LOGIN_PATH, new UserLoginSubmitCommand());
            put(USER_PURCHASE_PATH, new UserPurchaseSubmitCommand());

            put(ADMIN_REFILL_PATH, new AdminRefillSubmitCommand());
            put(ADMIN_LOGIN_PATH, new AdminLoginSubmitCommand());
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
