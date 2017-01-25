package coffee.machine.service.impl.wrapper;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoManagerFactory;

/**
 * This interface provides wrapping template for void methods, which use DaoManager in serializable transaction.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
@FunctionalInterface
public interface SerializableTransactionalVoidWrapper {
    default void execute(DaoManagerFactory daoFactory) {
        try (DaoManager daoManager = daoFactory.createDaoManager()) {
            daoManager.beginSerializableTransaction();
            processMethod(daoManager);
            daoManager.commitTransaction();
        }
    }

    void processMethod(DaoManager daoManager);
}
