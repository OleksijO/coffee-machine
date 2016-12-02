package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.Command;
import coffee_machine.controller.logging.ControllerErrorLogging;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.service.AccountService;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;
import coffee_machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.USER_PURCHASE_PAGE;

public class UserPurchaseCommand implements Command, ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(UserPurchaseCommand.class);
    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AddonService addonService = AddonServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_PURCHASE);

        try {
                /* just putting all needed for jsp data */
            int userId = (int) request.getSession().getAttribute(USER_ID);
            request.setAttribute(USER_BALANCE, accountService.getByUserId(userId).getRealAmount());
            request.setAttribute(DRINKS, drinkService.getAll());

        } catch (ApplicationException e) {
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }

        return USER_PURCHASE_PAGE;
    }

}
