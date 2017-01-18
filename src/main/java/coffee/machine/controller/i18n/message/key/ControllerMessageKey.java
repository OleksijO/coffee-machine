package coffee.machine.controller.i18n.message.key;

/**
 * This interface is a holder for bundle keys of messages, used in commands
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface ControllerMessageKey {
    String ADMIN_REFILL_SUCCESSFUL = "refill.successful";

    String PURCHASE_THANKS_MESSAGE = "user.purchase.thanks.for.purchase";

    String ADD_CREDITS_YOU_ADDED_CREDITS_SUCCESSFULLY_ON_ACCOUNT_OF_USER = "admin.add.credits.you.added.credits.to.account.of.user";

    String CREDITS_AMOUNT_IS_NOT_DOUBLE = "error.add.credits.amount.is.not.double";


    String TITLE_HOME = "title.home";
    String TITLE_ADMIN_REFILL = "title.admin.refill";
    String TITLE_ADMIN_ADD_CREDIT = "title.admin.add.credit";
    String TITLE_USER_ORDER_HISTORY = "title.user.orders.history";
    String TITLE_USER_PURCHASE = "title.user.purchase";
    String TITLE_LOGIN = "title.login";
    String TITLE_FOR_LOGIN_FORM = "login.form.title";
    String TITLE_USER_REGISTER = "title.user.register";
}
