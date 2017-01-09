package coffee.machine.service.i18n.message.key.error;

/**
 * This interface is a holder for bundle keys of error messages, used in service layer
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface ServiceErrorMessageKey {

	String ITEM_NO_LONGER_AVAILABLE = "error.service.goods.no.longer.available";
	String NOT_ENOUGH_MONEY = "error.service.not.enough.money";
	String YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY = "user.purchase.you.did.not.specified.drinks.to.buy";
    String USER_WITH_SPECIFIED_EMAIL_ALREADY_REGISTERED =
			"error.service.register.user.with.specified.email.already.registered";

	String ADMIN_REFILL_NOTHING_TO_ADD = "error.command.nothing.to.refill";
	String QUANTITY_SHOULD_BE_NON_NEGATIVE = "error.command.quantity.field.should.be.greater.than.or.equals.to.zero";

	String ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN = "error.login.email.do.not.match.pattern";
	String ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN = "error.login.password.do.not.match.pattern";
	String ERROR_LOGIN_NO_SUCH_COMBINATION = "error.login.not.such.combination";

	String ERROR_ADD_CREDITS_AMOUNT_IS_NEGATIVE = "error.add.credits.amount.is.negative";
    String ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN = "error.register.full.name.do.not.match.pattern" ;
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
