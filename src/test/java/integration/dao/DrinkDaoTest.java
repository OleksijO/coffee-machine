package integration.dao;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DrinkDao;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.goods.Drink;
import data.Drinks;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
@Ignore
public class DrinkDaoTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<Drink> testDrinks = new ArrayList<>();
    private AbstractConnection connection;
    private DrinkDao drinkDao;

    {
        for (Drinks drinkEnum : Drinks.values()) {
            testDrinks.add(drinkEnum.drink);
        }
    }

    @Before
    public void init() {
        System.out.println(testDrinks);
        connection = daoFactory.getConnection();
        drinkDao = daoFactory.getDrinkDao(connection);
        connection.beginTransaction();
    }

    @After
    public void post() {
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<Drink> drinks = drinkDao.getAll();
        System.out.println(testDrinks);
        System.out.println(drinks);
        assertEquals(testDrinks, drinks);

    }

    @Test
    public void testGetAllByList() {

        assertEquals(testDrinks, drinkDao.getAllFromList(testDrinks));

    }

    @Test
    public void testGetAllByListIds() {

        List<Integer> drinkIds = new ArrayList<>();
        testDrinks.forEach(drink -> drinkIds.add(drink.getId()));
        assertEquals(testDrinks, drinkDao.getAllFromList(testDrinks));

    }

    @Test
    public void testGetById() {
        int drinkId = 6;
        int drinkTestListId = 4;
        Drink drink = drinkDao.getById(drinkId);
        System.out.println(testDrinks.get(drinkTestListId));
        System.out.println(drink);
        assertEquals("Not null", testDrinks.get(drinkTestListId), drink);
        drink = drinkDao.getById(456);
        assertNull("Null", drink);
    }

    @Test
    public void testUpdate() {
        int drinkTestListId = 4;
        Drink drink = testDrinks.get(drinkTestListId);
        int drinkId = 6;
        
        long amount = drink.getPrice();
        drink.setPrice(0);

        drinkDao.update(drink);

        assertEquals("1", 0, drinkDao.getById(drinkId).getPrice());
        drink.setPrice(amount);
        drinkDao.update(drink);
        assertEquals("2", testDrinks.get(drinkTestListId).getPrice(), drinkDao.getById(drinkId).getPrice());

    }

    @Test
    public void testInsertDelete() {
        int drinkTestListId = 4;
        Drink drink = testDrinks.get(drinkTestListId);
        int size = testDrinks.size();
        int drinkId = 6;

        drink.setId(0);
        int newDrinkId = drinkDao.insert(drink).getId();

        drink.setId(newDrinkId);
        Drink drinkToTest = drinkDao.getById(newDrinkId);
        assertEquals("1", drink, drinkToTest);
        drink.setId(drinkId);
        assertEquals("2", size + 1, drinkDao.getAll().size());
        drinkDao.deleteById(newDrinkId);
        assertNull("3", drinkDao.getById(newDrinkId));
        assertEquals("4", size, drinkDao.getAll().size());

    }

    @Test
    public void testUpdateQuantity() {
        int drinkTestListId = 4;
        Drink testDrink = testDrinks.get(drinkTestListId);
        Drink drink = new Drink();
        int newQuantity = testDrink.getQuantity() + 1;
        drink.setId(testDrink.getId());
        drink.setQuantity(newQuantity);
        drink.setPrice(0);
        drink.setName("");

        drinkDao.updateQuantity(drink);

        assertEquals("1", newQuantity, drinkDao.getById(testDrink.getId()).getQuantity());
        drink.setQuantity(testDrink.getQuantity());
        assertEquals("2", testDrink, drinkDao.getById(testDrink.getId()));
        drinkDao.updateQuantity(drink);

    }

}