package coffee.machine.controller;

import coffee.machine.controller.command.HomeCommand;
import coffee.machine.controller.command.LogoutCommand;
import coffee.machine.controller.command.UnsupportedPathCommand;
import coffee.machine.controller.command.admin.add.credit.AdminAddCreditCommand;
import coffee.machine.controller.command.admin.add.credit.AdminAddCreditSubmitCommand;
import coffee.machine.controller.command.admin.refill.AdminRefillCommand;
import coffee.machine.controller.command.admin.refill.AdminRefillSubmitCommand;
import coffee.machine.controller.command.login.LoginCommand;
import coffee.machine.controller.command.login.LoginSubmitCommand;
import coffee.machine.controller.command.user.UserOrderHistoryCommand;
import coffee.machine.controller.command.user.purchase.UserPurchaseCommand;
import coffee.machine.controller.command.user.purchase.UserPurchaseSubmitCommand;
import coffee.machine.controller.command.user.register.UserRegisterCommand;
import coffee.machine.controller.command.user.register.UserRegisterSubmitCommand;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static coffee.machine.view.PagesPaths.*;

/**
 * This class is implementation of CommandHolder. It defines command for every supported request uri.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class CommandHolderImpl implements CommandHolder {
    public static final Logger logger = Logger.getLogger(CommandHolderImpl.class);

    private final Command unsupportedPathCommand = new UnsupportedPathCommand();

    private final String deployPath;

    /**
     * Holder for GET commands
     */
    private Map<String, Command> getCommands = new HashMap<>() ;

    /**
     * Holder for POST commands
     */
    private final Map<String, Command> postCommands = new HashMap<>() ;

    public CommandHolderImpl(String deployPath) {
        this.deployPath = deployPath;
        init();
    }

    private void init() {
        getCommands.put(deployPath + HOME_PATH, new HomeCommand());
        getCommands.put(deployPath + LOGIN_PATH, new LoginCommand());

        getCommands.put(deployPath + LOGOUT_PATH, new LogoutCommand());
        getCommands.put(deployPath + USER_REGISTER_PATH, new UserRegisterCommand());
        getCommands.put(deployPath + USER_PURCHASE_PATH, new UserPurchaseCommand());
        getCommands.put(deployPath + USER_ORDER_HISTORY_PATH, new UserOrderHistoryCommand());

        getCommands.put(deployPath + ADMIN_REFILL_PATH, new AdminRefillCommand());
        getCommands.put(deployPath + ADMIN_ADD_CREDITS_PATH, new AdminAddCreditCommand());

        postCommands.put(deployPath + LOGIN_PATH, new LoginSubmitCommand());
        postCommands.put(deployPath + USER_REGISTER_PATH, new UserRegisterSubmitCommand());
        postCommands.put(deployPath + USER_PURCHASE_PATH, new UserPurchaseSubmitCommand());

        postCommands.put(deployPath + ADMIN_REFILL_PATH, new AdminRefillSubmitCommand());
        postCommands.put(deployPath + ADMIN_ADD_CREDITS_PATH, new AdminAddCreditSubmitCommand());
    }

    @Override
    public Command get(String path) {
        Command command = getCommands.get(path);
        command = changeForUnsupportedCommandIfNull(command, path);
        return command;
    }

    private Command changeForUnsupportedCommandIfNull(Command command, String path) {
        if (command == null) {
            command = unsupportedPathCommand;
        }
        return command;
    }

    @Override
    public Command post(String path) {
        Command command = postCommands.get(path);
        command = changeForUnsupportedCommandIfNull(command, path);
        return command;
    }


}
