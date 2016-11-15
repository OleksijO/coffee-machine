package coffee_machine.dao.impl.jdbc;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
abstract class AbstractUserDao<T> extends AbstractDao<T> {
    protected static final String SELECT_ALL_FROM_ABSTRACT_USER_SQL =
            "SELECT abstract_user.id, login, password, full_name %s FROM abstract_user ";
    protected static final String UPDATE_ABSTRACT_USER_SQL =
            "UPDATE abstract_user SET login = ?, password = ?, full_name = ? WHERE id = ?; ";
    protected static final String INSERT_ABSTRACT_USER_SQL =
            "INSERT INTO abstract_user (login, password, full_name) VALUES (?, ?, ?); ";
    protected static final String DELETE_ABSTRACT_USER_SQL =
            "DELETE FROM abstract_user WHERE id = ?; ";
}
