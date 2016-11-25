package coffee_machine.i18n.message.key.error;

public interface DaoErrorKey {
    String CAN_NOT_CREATE_EMPTY = "can.not.create.empty.entity";
    String CAN_NOT_CREATE_ALREADY_SAVED = "can.not.create.already.saved.entity";
    String CAN_NOT_UPDATE_EMPTY = "can.not.update.null.entity";
    String CAN_NOT_UPDATE_UNSAVED = "can.not.update.unsaved.entity";
    String DB_ERROR_WHILE_INSERTING = "db.error.while.inserting";
    String DB_ERROR_WHILE_UPDATING = "db.error.while.updating";
    String DB_ERROR_UNEXPECTED_MULTIPLE_RESULT_WHILE_GETTING_BY_ID =
            "db.error.unexpected.multiple.results.while.getting.by.id";
    String DB_ERROR_WHILE_GETTING_BY_ID = "db.error.while.getting.by.id";
    String DB_ERROR_WHILE_DELETING_BY_ID = "db.error.while.deleting.by.id";
    String DB_ERROR_WHILE_GETTING_ALL = "db.error.while.getting.all";

    // UserDao specific
    String DB_ERROR_WHILE_GETTING_BY_LOGIN = "db.error.while.getting.by.login";

    // AbstractConnection specific
    String CAN_NOT_BEGIN_TRANSACTION = "error.connection.can.not.begin.transaction";
    String CAN_NOT_COMMIT_TRANSACTION = "error.connection.can.not.commit.transaction";
    String CAN_NOT_ROLLBACK_TRANSACTION ="error.connection.can.not.rollback.transaction";
    String CAN_NOT_CLOSE_CONNECTION = "error.connection.can.not.close.connection";
}
