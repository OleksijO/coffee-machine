package coffee.machine.controller.impl.command.helper;

import coffee.machine.CoffeeMachineConfig;
import coffee.machine.controller.logging.ControllerErrorLogging;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.view.Attributes;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents purchase page main functionality helper.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserPurchaseCommandHelper implements ControllerErrorLogging {

    public void setGeneralRegisterPageAttributes(HttpServletRequest request) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_PURCHASE);
        request.setAttribute(Attributes.ADMIN_CONTACTS, CoffeeMachineConfig.ADMIN_CONTACT_INFO);
        request.setAttribute(Attributes.BALANCE_LOW_WARN_LIMIT, CoffeeMachineConfig.BALANCE_WARN_LIMIT);
    }

}
