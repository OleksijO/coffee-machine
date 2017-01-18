package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AbstractConnection;
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
    private AbstractConnection connection;
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
        System.out.println(testDrinks);
        connection = daoFactory.getConnection();
        drinkDao = daoFactory.getDrinkDao(connection);
        connection.beginTransaction();
    }

    @After
    public void tearDown() {
        connection.commitTransaction();
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
        assertEquals("Check state after test",testDrink.getPrice(), drinkDao.getById(drinkId).get().getPrice());

    }

    @Test
    public void testInsertDelete() {
        Drink testDrink = DrinksData.ESPRESSO.getCopy();
        int size = testDrinks.size();
        int drinkId = testDrink.getId();

        testDrink.setId(0);
        int newDrinkId = drinkDao.insert(testDrink).getId();

        Drink drinkToTest = drinkDao.getById(newDrinkId).get();
        assertEquals("New addon should be placed to DB and be the same",testDrink, drinkToTest);
        testDrink.setId(drinkId);
        assertEquals("Total size of drinks should increase by 1", size + 1, drinkDao.getAll().size());
        drinkDao.deleteById(newDrinkId);
        assertFalse("Inserted drink should be deleted", drinkDao.getById(newDrinkId).isPresent());
        assertEquals("Total size of drinks should decrease by 1", size, drinkDao.getAll().size());

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

        assertEquals("Quantity of drink is updated", newQuantity, drinkDao.getById(testDrink.getId()).get().getQuantity());
        drink.setQuantity(testDrink.getQuantity());
        drinkDao.updateQuantity(drink);
        assertEquals("Check state after test", testDrink, drinkDao.getById(testDrink.getId()).get());

    }


    @Test
    public void testGetAllFromList() throws Exception {
        List<Drink> productsToRetrieve = new ArrayList<>();
        Drink addon = DrinksData.BORJOMI.getCopy();
        productsToRetrieve.add(addon);
        addon = DrinksData.MOCACCINO.getCopy();
        productsToRetrieve.add(addon);
        assertEquals(productsToRetrieve, drinkDao.getAllFromList(productsToRetrieve));
    }

    @Test
    public void testGetAllByIds() throws Exception {
        List<Drink> productsToRetrieve = new ArrayList<Drink>() {{
            add(DrinksData.BORJOMI.getCopy());
            add(DrinksData.MOCACCINO.getCopy());
        }};
        Set<Integer> ids = new TreeSet<Integer>(){{add(2);add(11);}};
        assertEquals(productsToRetrieve, drinkDao.getAllByIds(ids));
    }

}