package coffee.machine.service.impl.wrapper;

import coffee.machine.dao.DaoManagerFactory;

/**
 * This class provides methods for wrapping service methods to reduce code duplication
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public abstract class GenericService {
    protected DaoManagerFactory daoFactory;

    protected GenericService(DaoManagerFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    protected <K> K executeInSerializableTransactionalWrapper(SerializableTransactionalWrapper<K> wrapper) {
        return wrapper.execute(daoFactory);
    }

    protected <K> K executeInNonTransactionalWrapper(NonTransactionalWrapper<K> wrapper) {
        return wrapper.execute(daoFactory);
    }

    protected <K> K executeInTransactionalWrapper(TransactionalWrapper<K> wrapper) {
        return wrapper.execute(daoFactory);
    }

    protected void executeInSerializableTransactionalVoidWrapper(SerializableTransactionalVoidWrapper wrapper) {
        wrapper.execute(daoFactory);
    }

    public void setDaoFactory(DaoManagerFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
