package coffee.machine.service.impl.lambda.based.wrapper;

import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.DaoFactory;

/**
 * Created by oleksij.onysymchuk@gmail on 24.01.2017.
 */
@FunctionalInterface
public interface NonTransactionalWrapper<T> {
    default T execute(DaoFactory daoFactory) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            T result = processMethod(connection);
            connection.commitTransaction();
            return result;
        }
    }

    T processMethod(DaoConnection connection);
}
