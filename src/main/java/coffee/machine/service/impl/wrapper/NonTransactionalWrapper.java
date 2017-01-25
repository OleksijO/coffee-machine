package coffee.machine.service.impl.wrapper;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoFactory;

/**
 * Created by oleksij.onysymchuk@gmail on 24.01.2017.
 */
@FunctionalInterface
public interface NonTransactionalWrapper<T> {
    default T execute(DaoFactory daoFactory) {
        try (DaoManager daoManager = daoFactory.createDaoManager()) {
            return processMethod(daoManager);
        }
    }

    T processMethod(DaoManager daoManager);
}
