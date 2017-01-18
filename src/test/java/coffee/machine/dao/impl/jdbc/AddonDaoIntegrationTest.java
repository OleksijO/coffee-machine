package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.model.entity.product.Product;
import data.test.entity.AddonsData;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Performs tests of corresponding DAO on real test database (it should be already created)
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AddonDaoIntegrationTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<Product> testAddons = new ArrayList<>();
    private AbstractConnection connection;
    private AddonDao addonDao;

    {
        for (AddonsData addonEnum : AddonsData.values()) {
            testAddons.add(addonEnum.addon);
        }

    }

    @BeforeClass
    public static void initTestDataBase() throws Exception {
        new TestDatabaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        System.out.println(testAddons);
        connection = daoFactory.getConnection();
        addonDao = daoFactory.getAddonDao(connection);
        connection.beginTransaction();
    }

    @After
    public void tearDown() {
        connection.commitTransaction();
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<Product> addons = addonDao.getAll();
        System.out.println(testAddons);
        System.out.println(addons);
        assertEquals(testAddons, addons);

    }

    @Test
    public void testGetAllByList() {

        assertEquals(testAddons, addonDao.getAllFromList(testAddons));

    }

    @Test
    public void testGetAllByListIds() {

        List<Integer> addonIds = new ArrayList<>();
        testAddons.forEach(addon -> addonIds.add(addon.getId()));
        assertEquals(testAddons, addonDao.getAllFromList(testAddons));

    }

    @Test
    public void testGetById() {
        Product testAddon = AddonsData.SUGAR.getCopy();
        Product addon = addonDao.getById(testAddon.getId()).get();
        assertEquals(testAddon, addon);

    }

    @Test
    public void testGetByIdIsNotPresent() {
        assertFalse(addonDao.getById(456).isPresent());
    }

    @Test
    public void testUpdate() {
        Product testAddon = AddonsData.SUGAR.getCopy();
        long amount = testAddon.getPrice();
        testAddon.setPrice(0);

        addonDao.update(testAddon);

        assertEquals("Addon price should be updated", 0, addonDao.getById(testAddon.getId()).get().getPrice());

        testAddon.setPrice(amount);
        addonDao.update(testAddon);
        assertEquals("State after test should be as before test",
                testAddon.getPrice(), addonDao.getById(testAddon.getId()).get().getPrice());

    }

    @Test
    public void testInsertDelete() {
        Product testAddon = AddonsData.SUGAR.getCopy();
        int size = testAddons.size();
        int addonId = testAddon.getId();
        testAddon.setId(0);
        int newAddonId = addonDao.insert(testAddon).getId();

        assertEquals("New addon should be placed to DB and be the same", testAddon, addonDao.getById(newAddonId).get());
        testAddon.setId(addonId);
        assertEquals("Total size of addons should increase by 1", size + 1, addonDao.getAll().size());
        addonDao.deleteById(newAddonId);
        assertFalse("Inserted addon should be deleted", addonDao.getById(newAddonId).isPresent());
        assertEquals("Total size of addons should decrease by 1", size, addonDao.getAll().size());

    }

    @Test
    public void testUpdateQuantity() {

        Product testAddon = AddonsData.SUGAR.getCopy();
        Product addon = new Product();
        int newQuantity = testAddon.getQuantity() + 1;
        addon.setId(testAddon.getId());
        addon.setQuantity(newQuantity);
        addon.setPrice(0);
        addon.setName("");

        addonDao.updateQuantity(addon);

        assertEquals("Quantity of addon should be updated", newQuantity, addonDao.getById(testAddon.getId()).get().getQuantity());
        addon.setQuantity(testAddon.getQuantity());
        addonDao.updateQuantity(addon);
        addon = addonDao.getById(testAddon.getId()).get();
        assertEquals("check state after test", testAddon, addon);
    }

    @Test
    public void testGetAllFromList() throws Exception {
        List<Product> productsToRetrieve = new ArrayList<>();
        Product addon = AddonsData.CREAM.getCopy();
        productsToRetrieve.add(addon);
        addon = AddonsData.CINNAMON.getCopy();
        productsToRetrieve.add(addon);
        assertEquals(productsToRetrieve, addonDao.getAllFromList(productsToRetrieve));
    }

    @Test
    public void testGetAllByIds() throws Exception {
        List<Product> productsToRetrieve = new ArrayList<Product>() {{
            add(AddonsData.CREAM.getCopy());
            add(AddonsData.CINNAMON.getCopy());
        }};
        Set<Integer> ids = new TreeSet<Integer>(){{add(9);add(13);}};
        assertEquals(productsToRetrieve, addonDao.getAllByIds(ids));
    }

}