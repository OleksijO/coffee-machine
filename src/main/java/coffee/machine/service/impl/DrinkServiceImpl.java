package coffee.machine.service.impl;

import coffee.machine.dao.DaoManagerFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.service.DrinkService;

import java.util.List;

/**
 * This class is an implementation of DrinkService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DrinkServiceImpl extends GenericService implements DrinkService {

    private DrinkServiceImpl(DaoManagerFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final DrinkService instance =
                new DrinkServiceImpl(DaoFactoryImpl.getInstance());
    }

    public static DrinkService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public List<Drink> getAll() {
        return executeInNonTransactionalWrapper(daoManager ->
                daoManager
                        .getDrinkDao()
                        .getAll()

        );
    }
}
