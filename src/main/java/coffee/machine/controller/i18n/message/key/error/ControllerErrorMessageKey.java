package coffee.machine.controller.i18n.message.key.error;

/**
 * This interface is a holder for bundle keys of error messages, used in commands
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface ControllerErrorMessageKey {

	String QUANTITY_SHOULD_BE_INT = "error.command.quantity.field.should.be.only.int.number";

    String ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN = "error.login.you.are.already.logged.in" ;

    String ERROR_UNKNOWN = "error.unknown";
    String TITLE_HOME = "title.home";
    String TITLE_ADMIN_REFILL = "title.admin.refill";
    String TITLE_ADMIN_ADD_CREDIT = "title.admin.add.credit";
    String TITLE_USER_ORDER_HISTORY = "title.user.orders.history";
    String TITLE_USER_PURCHASE = "title.user.purchase";
    String TITLE_LOGIN = "title.login";
    String TITLE_USER_REGISTER = "title.user.register";
    String LOGIN_FORM_TITLE = "login.form.title";
    String REGISTER_USER_FORM_TITLE = "register.form.user.title";
}
