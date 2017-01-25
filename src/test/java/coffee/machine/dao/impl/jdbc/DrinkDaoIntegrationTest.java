package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import data.test.entity.DrinksData;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Performs tests of corresponding DAO on real test database (it should be already created)
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DrinkDaoIntegrationTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<Drink> testDrinks = new ArrayList<>();
    private DaoManager daoManager;
    private DrinkDao drinkDao;

    {
        for (DrinksData drinkEnum : DrinksData.values()) {
            testDrinks.add(drinkEnum.drink);
        }
    }

    @BeforeClass
    public static void initTestDataBase() throws Exception {
        new TestDatabaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        daoManager = daoFactory.createDaoManager();
        drinkDao = daoManager.getDrinkDao();
        daoManager.beginTransaction();
    }

    @After
    public void tearDown() {
        daoManager.commitTransaction();
        daoManager.close();
    }

    @Test
    public void testGetAll() {

        List<Drink> drinks = drinkDao.getAll();
        assertEquals(testDrinks, drinks);

    }

    @Test
    public void testGetAllByList() {

        assertEquals(testDrinks, drinkDao.getAllFromList(testDrinks));

    }

    @Test
    public void testGetAllByListIds() {

        List<Integer> drinkIds = testDrinks
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        assertEquals(testDrinks, drinkDao.getAllByIds(new HashSet<>(drinkIds)));

    }

    @Test
    public void testGetById() {
        Drink testDrink = DrinksData.ESPRESSO.getCopy();
        int drinkId = testDrink.getId();
        Drink drink = drinkDao.getById(drinkId).get();
        assertEquals(testDrink, drink);
    }

    @Test
    public void testGetByIdIsNotPresent() {
        assertFalse(drinkDao.getById(456).isPresent());
    }

    @Test
    public void testUpdate() {
        Drink testDrink = DrinksData.ESPRESSO.getCopy();
        int drinkId = testDrink.getId();

        long amount = testDrink.getPrice();
        testDrink.setPrice(0);

        drinkDao.update(testDrink);

        assertEquals(0, drinkDao.getById(drinkId).get().getPrice());

        testDrink.setPrice(amount);
        drinkDao.update(testDrink);
        assertEquals("Check state after test", testDrink.getPrice(), drinkDao.getById(drinkId).get().getPrice());

    }

    @Test
    public void testInsert() {
        Drink testDrink = DrinksData.ESPRESSO.getCopy();
        testDrink.setId(0);

        int newDrinkId = drinkDao.insert(testDrink).getId();

        assertEquals("New addon should be placed to DB and be the same",
                testDrink, drinkDao.getById(newDrinkId).get());
        drinkDao.deleteById(newDrinkId);
        assertFalse("Check state DB after test", drinkDao.getById(newDrinkId).isPresent());

    }

    @Test
    public void testDelete() {
        Drink testDrink = DrinksData.ESPRESSO.getCopy();
        testDrink.setId(0);
        int newDrinkId = drinkDao.insert(testDrink).getId();
        assertEquals("Total size of drinks should increase by 1. It is necessary test condition ",
                testDrinks.size() + 1, drinkDao.getAll().size());

        drinkDao.deleteById(newDrinkId);
        assertFalse("Inserted drink should be deleted", drinkDao.getById(newDrinkId).isPresent());

    }

    @Test
    public void testUpdateQuantity() {
        int drinkTestListId = 4;
        Drink testDrink = testDrinks.get(drinkTestListId);
        int newQuantity = testDrink.getQuantity() + 1;
        Drink drink = new Drink.Builder()
                .setId(testDrink.getId())
                .setQuantity(newQuantity)
                .setPrice(0)
                .setName("")
                .build();

        drinkDao.updateQuantity(drink);

        assertEquals("Quantity of drink is updated", newQuantity, drinkDao.getById(testDrink.getId()).get().getQuantity());
        drink.setQuantity(testDrink.getQuantity());
        drinkDao.updateQuantity(drink);
        assertEquals("Check state after test", testDrink, drinkDao.getById(testDrink.getId()).get());

    }


    @Test
    public void testGetAllFromList() {
        List<Drink> productsToRetrieve = new ArrayList<>();
        Drink addon = DrinksData.BORJOMI.getCopy();
        productsToRetrieve.add(addon);
        addon = DrinksData.MOCACCINO.getCopy();
        productsToRetrieve.add(addon);
        assertEquals(productsToRetrieve, drinkDao.getAllFromList(productsToRetrieve));
    }

    @Test
    public void testGetAllByIds() {
        List<Drink> productsToRetrieve = new ArrayList<Drink>() {{
            add(DrinksData.BORJOMI.getCopy());
            add(DrinksData.MOCACCINO.getCopy());
        }};
        Set<Integer> ids = new TreeSet<Integer>() {{
            add(2);
            add(11);
        }};
        assertEquals(productsToRetrieve, drinkDao.getAllByIds(ids));
    }

}
