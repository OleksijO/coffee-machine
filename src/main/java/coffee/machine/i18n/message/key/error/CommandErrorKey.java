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

    String ERROR_LOGIN_YOU_ARE_ALREADY_LOGGED_IN = "error.login.you.are.already.logged.in" ;
}
