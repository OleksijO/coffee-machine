package coffee_machine.dao.exception;

import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.error.DaoErrorKey;

public class DaoException extends ApplicationException {

	public DaoException(){
		super(DaoErrorKey.DAO_ERROR);
	}

	public DaoException(Throwable cause) {
		super(DaoErrorKey.DAO_ERROR, cause);
	}

}
