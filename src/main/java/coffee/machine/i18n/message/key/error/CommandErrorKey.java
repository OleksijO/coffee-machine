package coffee.machine.i18n.message.key.error;

/**
 * This interface is a holder for bundle keys of error messages, used in commands
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CommandErrorKey {

	String QUANTITY_SHOULD_BE_INT = "error.command.quantity.field.should.be.only.int.number";
	String QUANTITY_SHOULD_BE_NON_NEGATIVE = "error.command.quantity.field.should.be.greater.than.or.equals.to.zero";
	String ADMIN_REFILL_NOTHING_TO_ADD = "error.command.nothing.to.refill";

	String ERROR_LOGIN_NO_SUCH_COMBINATION = "error.login.not.such.combination";
	String ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN = "error.login.email.do.not.match.pattern";
	String ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN = "error.login.password.do.not.match.pattern";


    Object ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN = "error.register.full.name.do.not.match.pattern" ;
    Object ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN = "error.login.you.are.already.logged.in" ;
}
