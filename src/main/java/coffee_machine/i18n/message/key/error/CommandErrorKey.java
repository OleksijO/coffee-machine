package coffee_machine.i18n.message.key.error;

public interface CommandErrorKey {

	String QUANTITY_SHOULD_BE_INT = "error.command.quantity.field.should.be.only.int.number";
	String QUANTITY_SHOULD_BE_NON_NEGATIVE = "error.command.quantity.field.should.be.greater.than.or.equals.to.zero";
	String ADMIN_REFILL_NOTHING_TO_ADD = "error.command.nothing.to.refill";

	String ERROR_LOGIN_NO_SUCH_COMBINATION = "error.login.not.such.combination";
	String ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN = "error.login.email.do.not.match.pattern";
	String ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN = "error.login.password.do.not.match.pattern";
	String ERROR_LOGIN_ADMIN_DISABLED = "error.login.admin.is.disabled";


}
