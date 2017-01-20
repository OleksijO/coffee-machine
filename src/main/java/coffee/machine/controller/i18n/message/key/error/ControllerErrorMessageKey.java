package coffee.machine.controller.i18n.message.key.error;

/**
 * This interface is a holder for bundle keys of error messages, used in commands
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ControllerErrorMessageKey {

    public static final String QUANTITY_SHOULD_BE_INT = "error.command.quantity.field.should.be.only.int.number";

    public static final String ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN = "error.login.you.are.already.logged.in" ;

    public static final String ERROR_UNKNOWN = "error.unknown";
    public static final String REGISTER_USER_FORM_TITLE = "register.form.user.title";
    public static final String ERROR_PREPARE_ORDER_NOTHING_TO_BUY = "user.purchase.you.did.not.specified.drinks.to.buy";
    public static final String ADMIN_REFILL_NOTHING_TO_ADD = "error.command.nothing.to.refill";
    public static final String QUANTITY_SHOULD_BE_NON_NEGATIVE =
            "error.command.quantity.field.should.be.greater.than.or.equals.to.zero";
    public static final String ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN = "error.login.email.do.not.match.pattern";
    public static final String ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN = "error.login.password.do.not.match.pattern";
    public static final String ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN =
            "error.register.full.name.do.not.match.pattern" ;
    public static final String ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE = "error.add.credits.amount.is.negative";
    public static final String ERROR_INCORRECT_USER_ID = "error.incorrect.user.id";

    private ControllerErrorMessageKey() {
    }
}
