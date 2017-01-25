package coffee.machine.service.impl.wrapper;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoManagerFactory;

/**
 * This interface provides wrapping template for methods, which use DaoManager without transaction.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
@FunctionalInterface
public interface NonTransactionalWrapper<T> {

    default T execute(DaoManagerFactory daoFactory) {
        try (DaoManager daoManager = daoFactory.createDaoManager()) {
            return processMethod(daoManager);
        }
    }

    T processMethod(DaoManager daoManager);
}
