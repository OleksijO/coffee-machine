package coffee.machine.service.impl;

import coffee.machine.dao.DaoManagerFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.product.Product;
import coffee.machine.service.AddonService;

import java.util.List;

/**
 * This class is an implementation of AddonService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AddonServiceImpl extends GenericService implements AddonService {

    private AddonServiceImpl(DaoManagerFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final AddonService instance = new AddonServiceImpl(DaoFactoryImpl.getInstance());
    }

    public static AddonService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public List<Product> getAll() {

        return executeInNonTransactionalWrapper(daoManager ->
                daoManager
                        .getAddonDao()
                        .getAll()
        );
    }

    public void setDaoFactory(DaoManagerFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
