package coffee.machine.service.impl;

import coffee.machine.dao.DaoManagerFactory;
import coffee.machine.service.impl.wrapper.NonTransactionalWrapper;
import coffee.machine.service.impl.wrapper.SerializableTransactionalVoidWrapper;
import coffee.machine.service.impl.wrapper.SerializableTransactionalWrapper;
import coffee.machine.service.impl.wrapper.TransactionalWrapper;

/**
 * This class provides methods for wrapping service methods to reduce code duplication
 *
 * @author oleksij.onysymchuk@gmail.com
 */
abstract class GenericService {
    DaoManagerFactory daoFactory;

    GenericService(DaoManagerFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    <K> K executeInSerializableTransactionalWrapper(SerializableTransactionalWrapper<K> wrapper) {
        return wrapper.execute(daoFactory);
    }

    <K> K executeInNonTransactionalWrapper(NonTransactionalWrapper<K> wrapper) {
        return wrapper.execute(daoFactory);
    }

    <K> K executeInTransactionalWrapper(TransactionalWrapper<K> wrapper) {
        return wrapper.execute(daoFactory);
    }

    void executeInSerializableTransactionalVoidWrapper(SerializableTransactionalVoidWrapper wrapper) {
        wrapper.execute(daoFactory);
    }

    public void setDaoFactory(DaoManagerFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
