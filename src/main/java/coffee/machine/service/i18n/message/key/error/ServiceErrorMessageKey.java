package coffee.machine.service.i18n.message.key.error;

/**
 * This interface is a holder for bundle keys of error messages, used in service layer
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ServiceErrorMessageKey {

	public static final String ERROR_PREPARE_ORDER_PRODUCT_NO_LONGER_AVAILABLE = "error.service.goods.no.longer.available";
	public static final String ERROR_PREPARE_ORDER_USER_HAS_NOT_ENOUGH_MONEY = "error.service.not.enough.money";

	public static final String ERROR_REGISTER_USER_WITH_SPECIFIED_EMAIL_ALREADY_REGISTERED =
			"error.register.user.with.specified.email.already.registered";

	public static final String ERROR_LOGIN_NO_SUCH_COMBINATION = "error.login.not.such.combination";

	private ServiceErrorMessageKey() {
	}
}
