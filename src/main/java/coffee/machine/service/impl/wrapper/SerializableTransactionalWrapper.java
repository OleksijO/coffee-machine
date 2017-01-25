package coffee.machine.service.impl.wrapper;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoFactory;

/**
 * Created by oleksij.onysymchuk@gmail on 24.01.2017.
 */
@FunctionalInterface
public interface SerializableTransactionalWrapper<T> {
    default T execute(DaoFactory daoFactory) {
        try (DaoManager daoManager = daoFactory.createDaoManager()) {
            daoManager.beginSerializableTransaction();
            T result = processMethod(daoManager);
            daoManager.commitTransaction();
            return result;
        }
    }

    T processMethod(DaoManager daoManager);
}
