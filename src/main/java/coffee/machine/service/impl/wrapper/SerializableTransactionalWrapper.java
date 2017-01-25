package coffee.machine.service.impl.wrapper;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoManagerFactory;

/**
 * This interface provides wrapping template for methods, which use DaoManager in serializable transaction.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
@FunctionalInterface
public interface SerializableTransactionalWrapper<T> {
    default T execute(DaoManagerFactory daoFactory) {
        try (DaoManager daoManager = daoFactory.createDaoManager()) {
            daoManager.beginSerializableTransaction();
            T result = processMethod(daoManager);
            daoManager.commitTransaction();
            return result;
        }
    }

    T processMethod(DaoManager daoManager);
}
