package coffee_machine.controller.impl;

import coffee_machine.controller.Command;
import coffee_machine.controller.CommandHolder;
import coffee_machine.controller.impl.command.HomeCommand;
import coffee_machine.controller.impl.command.admin.*;
import coffee_machine.controller.impl.command.user.UserHomeCommand;
import coffee_machine.controller.impl.command.user.UserLoginCommand;
import coffee_machine.controller.impl.command.user.UserLoginSubmitCommand;
import coffee_machine.controller.impl.command.user.UserLogoutCommand;

import java.util.HashMap;
import java.util.Map;

import static coffee_machine.controller.PagesPaths.*;

/**
 * Created by oleksij.onysymchuk@gmail on 17.11.2016.
 */
public class CommandHolderImpl implements CommandHolder {
    private Map<String, Command> commands = new HashMap<>();

    @Override
    public Command get(String path) {
        return commands.get(path);
    }

    @Override
    public void init(){
        commands.put(HOME_PATH, new HomeCommand());
        commands.put(USER_LOGIN_PATH, new UserLoginCommand());
        commands.put(USER_LOGOUT_PATH, new UserLogoutCommand());
        commands.put(USER_LOGIN_SUBMIT_PATH, new UserLoginSubmitCommand());
        commands.put(USER_HOME_PATH, new UserHomeCommand());
        commands.put(ADMIN_LOGIN_PATH, new AdminLoginCommand());
        commands.put(ADMIN_LOGOUT_PATH, new AdminLogoutCommand());
        commands.put(ADMIN_REFILL_PATH, new AdminRefillCommand());
        commands.put(ADMIN_REFILL_SUBMIT_PATH, new AdminRefillSubmitCommand());
        commands.put(ADMIN_LOGIN_SUBMIT_PATH, new AdminLoginSubmitCommand());
        commands.put(ADMIN_HOME_PATH, new AdminHomeCommand());

    }
}
