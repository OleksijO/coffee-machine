package coffee.machine.controller.command.admin.add.credit;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.model.value.object.CreditsReceipt;
import coffee.machine.model.entity.User;
import coffee.machine.service.AccountService;
import coffee.machine.service.UserService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.UserServiceImpl;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static coffee.machine.i18n.message.key.CommandKey.ADD_CREDITS_YOU_ADDED_CREDITS_SUCCESSFULLY_ON_ACCOUNT_OF_USER;
import static coffee.machine.i18n.message.key.GeneralKey.TITLE_ADMIN_ADD_CREDIT;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.ADMIN_ADD_CREDITS_PAGE;
import static coffee.machine.view.Parameters.CREDITS_TO_ADD;

/**
 * This class represents admin's add credits post method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminAddCreditSubmitCommand extends CommandWrapperTemplate {
    private static Logger logger = Logger.getLogger(AdminAddCreditSubmitCommand.class);

    private static final String TO_USER_ID_ACCOUNT_ADDED_N_CREDITS_FORMAT =
            "To user's (id=%d) account added %.2f credits";
    private static final String USER_AMOUNT_SEPARATOR = " / ";
    private static final String USERS_ID = "User's id = ";

    private static final int COFFEE_MACHINE_ACCOUNT_ID = CoffeeMachineConfig.ACCOUNT_ID;

    private AccountService accountService = AccountServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();

    public AdminAddCreditSubmitCommand() {
        super(ADMIN_ADD_CREDITS_PAGE);
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_ADMIN_ADD_CREDIT);

    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {

        CreditsReceipt receipt = getReceiptDataFromRequest(request);

        accountService.addCredits(receipt);

        List<User> users = userService.getAllNonAdminUsers();
        placeUserDataToRequest(request, users);
        placeMessageToRequest(request, receipt, users);
        logDetails(receipt);
        return ADMIN_ADD_CREDITS_PAGE;
    }

    private CreditsReceipt getReceiptDataFromRequest(HttpServletRequest request) {
        return new CreditsReceipt.Builder()
                .setUserId(Integer.parseInt(request.getParameter(Parameters.USER_ID)))
                .setAmount(Double.parseDouble(request.getParameter(CREDITS_TO_ADD)))
                .build();
    }

    private void placeUserDataToRequest(HttpServletRequest request, List<User> users) {
        request.setAttribute(USER_LIST, users);
    }

    private void placeMessageToRequest(HttpServletRequest request, CreditsReceipt receipt, List<User> users) {
        request.setAttribute(USUAL_MESSAGE, ADD_CREDITS_YOU_ADDED_CREDITS_SUCCESSFULLY_ON_ACCOUNT_OF_USER);
        request.setAttribute(USUAL_ADDITIONAL_MESSAGE,
                getUserFullName(receipt.getUserId(), users) + USER_AMOUNT_SEPARATOR + receipt.getRealAmount());
        request.setAttribute(COFFEE_MACHINE_BALANCE,
                accountService.getById(COFFEE_MACHINE_ACCOUNT_ID)
                        .orElseThrow(IllegalStateException::new)
                        .getRealAmount());
    }

    private String getUserFullName(int userId, List<User> users) {
        return users.stream()
                .filter(user -> user.getId() == userId)
                .map(User::getFullName)
                .findAny()
                .orElse(String.valueOf(USERS_ID + userId));

    }

    private void logDetails(CreditsReceipt receipt) {

        logger.info(String.format(TO_USER_ID_ACCOUNT_ADDED_N_CREDITS_FORMAT,
                receipt.getUserId(), receipt.getRealAmount()));
    }


}
