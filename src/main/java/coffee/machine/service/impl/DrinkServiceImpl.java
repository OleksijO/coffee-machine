package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.service.DrinkService;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * This class is an implementation of DrinkService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DrinkServiceImpl implements DrinkService {
    private static final Logger logger = Logger.getLogger(DrinkServiceImpl.class);

    private static final String QUANTITIES_BY_ID_SHOULD_CONTAIN_ANY_DATA_GOT_OBJECT =
            "Quantities by id should contain any data. Got object: ";

    DaoFactory daoFactory = DaoFactoryImpl.getInstance();

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

    @Override
    public void refill(Map<Integer, Integer> quantitiesById) {
        if ((quantitiesById == null) || (quantitiesById.size() == 0)) {
            logger.error(
                    QUANTITIES_BY_ID_SHOULD_CONTAIN_ANY_DATA_GOT_OBJECT + quantitiesById);
            return;
        }
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            List<Drink> drinksToUpdate = drinkDao.getAllByIds(quantitiesById.keySet());
            drinksToUpdate.forEach(
                    drink -> drink.setQuantity(drink.getQuantity() + quantitiesById.get(drink.getId())));
            drinkDao.updateQuantityAllInList(drinksToUpdate);
            connection.commitTransaction();

        }
    }

    @Override
    public List<Drink> getAllByIdSet(Set<Integer> drinkIds) {
        Objects.requireNonNull(drinkIds);
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            List<Drink> drinks = drinkDao.getAllByIds(drinkIds);
            connection.commitTransaction();
            return (drinks == null) ? new ArrayList<>() : drinks;

        }
    }

    @Override
    public List<Drink> getAllBaseByIdSet(Set<Integer> drinkIds) {

        List<Drink> drinks = getAllByIdSet(drinkIds);
        List<Drink> baseDrinks = new ArrayList<>();
        drinks.forEach(drink -> baseDrinks.add(drink.getBaseDrink()));
        return baseDrinks;

    }

    @Override
    public Drink getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            return drinkDao.getById(id);
        }
    }

}
