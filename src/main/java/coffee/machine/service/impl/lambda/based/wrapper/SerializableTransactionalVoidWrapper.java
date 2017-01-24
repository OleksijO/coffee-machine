package coffee.machine.service.impl.lambda.based.wrapper;

import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.DaoFactory;

/**
 * Created by oleksij.onysymchuk@gmail on 24.01.2017.
 */
@FunctionalInterface
public interface SerializableTransactionalVoidWrapper<T> {
    default void execute(DaoFactory daoFactory) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.beginSerializableTransaction();
            processMethod(connection);
            connection.commitTransaction();
        }
    }

    void processMethod(DaoConnection connection);
}
