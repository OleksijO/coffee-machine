package coffee.machine.service.impl.wrapper;

import coffee.machine.dao.DaoFactory;

/**
 * Created by oleksij.onysymchuk@gmail on 24.01.2017.
 */
public class GenericService {
    protected DaoFactory daoFactory;

    protected GenericService(DaoFactory daoFactory) {
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

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
