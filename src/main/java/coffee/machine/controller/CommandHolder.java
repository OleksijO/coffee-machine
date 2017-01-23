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

import java.util.HashMap;
import java.util.Map;

import static coffee.machine.view.config.Paths.*;

/**
 * This class is implementation of CommandHolder. It defines command for every supported request uri.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class CommandHolder {

    static final String DELIMITER = ":";
    private static final String GET = "GET" + DELIMITER;
    private static final String POST = "POST" + DELIMITER;

    private final Command unsupportedPathCommand = new UnsupportedPathCommand();

    private final String deployPath;

    /**
     * Holder for GET commands
     */
    private Map<String, Command> commands = new HashMap<>();

    CommandHolder(String deployPath) {
        this.deployPath = deployPath;
        init();
    }

    private void init() {

        commands.put(GET + deployPath + HOME_PATH, new HomeCommand());
        commands.put(GET + deployPath + LOGIN_PATH, new LoginCommand());

        commands.put(GET + deployPath + LOGOUT_PATH, new LogoutCommand());
        commands.put(GET + deployPath + USER_REGISTER_PATH, new UserRegisterCommand());
        commands.put(GET + deployPath + USER_PURCHASE_PATH, new UserPurchaseCommand());
        commands.put(GET + deployPath + USER_ORDER_HISTORY_PATH, new UserOrderHistoryCommand());

        commands.put(GET + deployPath + ADMIN_REFILL_PATH, new AdminRefillCommand());
        commands.put(GET + deployPath + ADMIN_ADD_CREDITS_PATH, new AdminAddCreditCommand());


        commands.put(POST + deployPath + LOGIN_PATH, new LoginSubmitCommand());
        commands.put(POST + deployPath + USER_REGISTER_PATH, new UserRegisterSubmitCommand());
        commands.put(POST + deployPath + USER_PURCHASE_PATH, new UserPurchaseSubmitCommand());

        commands.put(POST + deployPath + ADMIN_REFILL_PATH, new AdminRefillSubmitCommand());
        commands.put(POST + deployPath + ADMIN_ADD_CREDITS_PATH, new AdminAddCreditSubmitCommand());

    }

    Command findCommand(String commandKey) {
        return commands.getOrDefault(commandKey, unsupportedPathCommand);
    }

}
