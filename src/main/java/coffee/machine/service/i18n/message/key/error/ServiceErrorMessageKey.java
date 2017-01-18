package coffee.machine.service.i18n.message.key.error;

/**
 * This interface is a holder for bundle keys of error messages, used in service layer
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface ServiceErrorMessageKey {

	String ERROR_PREPARE_ORDER_PRODUCT_NO_LONGER_AVAILABLE = "error.service.goods.no.longer.available";
	String ERROR_PREPARE_ORDER_USER_HAS_NOT_ENOUGH_MONEY = "error.service.not.enough.money";

    String ERROR_REGISTER_USER_WITH_SPECIFIED_EMAIL_ALREADY_REGISTERED =
			"error.register.user.with.specified.email.already.registered";

	String ERROR_LOGIN_NO_SUCH_COMBINATION = "error.login.not.such.combination";


}
