package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.service.DrinkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is an implementation of DrinkService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DrinkServiceImpl implements DrinkService {
    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

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
            connection.beginTransaction();
            List<Drink> drinks = drinkDao.getAll();
            connection.commitTransaction();
            return (drinks == null) ? new ArrayList<>() : drinks;

        }
    }

    @Override
    public void refill(Map<Integer, Integer> quantitiesById) {
        if ((quantitiesById == null) || (quantitiesById.size() == 0)) {
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

}
