package coffee.machine.service.impl.lambda.based.wrapper;

import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.DaoFactory;

/**
 * Created by oleksij.onysymchuk@gmail on 24.01.2017.
 */
@FunctionalInterface
public interface SerializableTransactionalWrapper<T> {
    default T execute(DaoFactory daoFactory) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.beginSerializableTransaction();
            T result = processMethod(connection);
            connection.commitTransaction();
            return result;
        }
    }

    T processMethod(DaoConnection connection);
}
