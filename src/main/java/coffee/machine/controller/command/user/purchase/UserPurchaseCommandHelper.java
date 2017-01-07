package coffee.machine.controller.command.user.purchase;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.logging.ControllerErrorLogging;

import javax.servlet.http.HttpServletRequest;

import static coffee.machine.i18n.message.key.GeneralKey.TITLE_USER_PURCHASE;
import static coffee.machine.view.Attributes.*;

/**
 * This class represents purchase page main functionality helper.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserPurchaseCommandHelper implements ControllerErrorLogging {

    public void setGeneralRegisterPageAttributes(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_USER_PURCHASE);
        request.setAttribute(ADMIN_CONTACTS, CoffeeMachineConfig.ADMIN_CONTACT_INFO);
        request.setAttribute(BALANCE_LOW_WARN_LIMIT, CoffeeMachineConfig.BALANCE_WARN_LIMIT);
    }

}
