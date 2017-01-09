package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.service.DrinkService;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an implementation of DrinkService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DrinkServiceImpl implements DrinkService {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private DrinkServiceImpl() {
    }

    private static class InstanceHolder {
        private static final DrinkService instance = new DrinkServiceImpl();
    }

    public static DrinkService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public List<Drink> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            List<Drink> drinks = drinkDao.getAll();
            return (drinks == null) ? new ArrayList<>() : drinks;

        }
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
