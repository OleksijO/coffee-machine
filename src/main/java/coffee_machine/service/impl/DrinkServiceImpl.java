package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.DrinkDao;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.goods.Drink;
import coffee_machine.service.DrinkService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class DrinkServiceImpl implements DrinkService {
    private static final Logger logger = Logger.getLogger(DrinkServiceImpl.class);

    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private static class InstanceHolder {
        private static final DrinkService instance = new DrinkServiceImpl();
    }

    public static DrinkService getInstance() {
        return InstanceHolder.instance;
    }


    public Drink create(Drink drink) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            drinkDao.insert(drink);
            connection.commitTransaction();
            return drink;

        }
    }

    public void update(Drink drink) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            drinkDao.update(drink);
            connection.commitTransaction();

        }
    }

    @Override
    public List<Drink> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            List<Drink> drinks = drinkDao.getAll();
            connection.commitTransaction();
            return (drinks == null) ? new ArrayList<>() : drinks;

        }
    }


    public Drink getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            Drink drink = drinkDao.getById(id);
            connection.commitTransaction();
            return drink;

        }
    }


    public void delete(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            drinkDao.deleteById(id);
            connection.commitTransaction();

        }
    }

    @Override
    public void refill(Map<Integer, Integer> quantitiesById) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            quantitiesById.keySet().forEach(id -> {
                Drink drink = drinkDao.getById(id);
                drink.setQuantity(drink.getQuantity() + quantitiesById.get(id));
                drinkDao.update(drink);
            });
            connection.commitTransaction();

        }
    }

    @Override
    public List<Drink> getAllByIdSet(Set<Integer> drinkIds) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            connection.beginTransaction();
            List<Drink> drinks = drinkDao.getAllByIds(new ArrayList<>(drinkIds));
            connection.commitTransaction();
            return (drinks == null) ? new ArrayList<>() : drinks;

        }
    }

    @Override
    public List<Drink> getAllBaseByIdSet(Set<Integer> drinkIds) {

        List<Drink> drinks = getAllByIdSet(drinkIds);
        List<Drink> baseDrinks = new ArrayList();
        drinks.forEach(drink->baseDrinks.add(drink.getBaseDrink()));
        return baseDrinks;

    }


}
