package coffee.machine.service.impl.wrapper;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoFactory;

/**
 * Created by oleksij.onysymchuk@gmail on 24.01.2017.
 */
@FunctionalInterface
public interface SerializableTransactionalVoidWrapper<T> {
    default void execute(DaoFactory daoFactory) {
        try (DaoManager daoManager = daoFactory.createDaoManager()) {
            daoManager.beginSerializableTransaction();
            processMethod(daoManager);
            daoManager.commitTransaction();
        }
    }

    void processMethod(DaoManager daoManager);
}
