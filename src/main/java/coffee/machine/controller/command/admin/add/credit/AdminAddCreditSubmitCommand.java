package coffee.machine.controller.command.admin.add.credit;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.i18n.message.key.CommandKey;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.model.entity.User;
import coffee.machine.service.AccountService;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static coffee.machine.config.CoffeeMachineConfig.DB_MONEY_COEFF;
import static coffee.machine.view.Attributes.COFFEE_MACHINE_BALANCE;
import static coffee.machine.view.Attributes.PAGE_TITLE;
import static coffee.machine.view.PagesPaths.ADMIN_ADD_CREDITS_PAGE;
import static coffee.machine.view.Parameters.CREDITS_TO_ADD;

/**
 * Created by oleksij.onysymchuk@gmail
 */
public class AdminAddCreditSubmitCommand extends CommandWrapperTemplate {
    private static final String TO_USER_ID_ACCOUNT_ADDED_N_CREDITS_FORMAT =
            "To user's (id=%d) account added %.2f credits";
    private static Logger logger = Logger.getLogger(AdminAddCreditSubmitCommand.class);
    AccountService accountService = AccountServiceImpl.getInstance();
    UserService userService = UserServiceImpl.getInstance();

    public AdminAddCreditSubmitCommand() {
        super(ADMIN_ADD_CREDITS_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {

        int userId = Integer.parseInt(request.getParameter(Parameters.USER_ID));
        long amountToAdd =
                (long) (Double.parseDouble(request.getParameter(CREDITS_TO_ADD)) / DB_MONEY_COEFF);
        accountService.addToAccountByUserId(userId, amountToAdd);
        List<User> users = userService.getAllNonAdminUsers();
        request.setAttribute(Attributes.USER_LIST, users);
        request.setAttribute(Attributes.USUAL_MESSAGE,
                CommandKey.ADD_CREDITS_YOU_ADDED_CREDITS_SUCCESSFULLY_ON_ACCOUNT_OF_USER);
        String userFullName = getUserFullNameByIdFromList(userId, users);
        request.setAttribute(Attributes.USUAL_ADDITIONAL_MESSAGE,
                userFullName + " / " + (amountToAdd * DB_MONEY_COEFF));
        request.setAttribute(COFFEE_MACHINE_BALANCE, accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                .getRealAmount());
        logger.info(String.format(TO_USER_ID_ACCOUNT_ADDED_N_CREDITS_FORMAT, userId, amountToAdd * DB_MONEY_COEFF));
        return ADMIN_ADD_CREDITS_PAGE;
    }

    private String getUserFullNameByIdFromList(int userId, List<User> users) {
        Optional<User> userOpt = users.stream().filter(user -> user.getId() == userId).findAny();
        if (userOpt.isPresent()) {
            return userOpt.get().getFullName();
        } else {
            return USER_ID_IS + userId;
        }
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, GeneralKey.TITLE_ADMIN_ADD_CREDIT);

    }
}
