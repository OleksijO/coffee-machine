package integration.dao;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Item;
import data.test.entity.Addons;
import org.junit.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class AddonDaoTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<Item> testAddons = new ArrayList<>();
    private AbstractConnection connection;
    private AddonDao addonDao;

    {
        for (Addons addonEnum : Addons.values()) {
            testAddons.add(addonEnum.addon);
        }

    }

    @BeforeClass
    public static void initTestDataBase() throws SQLException, ClassNotFoundException, InterruptedException, IOException {
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
    public void post() {
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<Item> addons = addonDao.getAll();
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
        int addonId = 7;
        int addonTestListId = 1;
        Item addon = addonDao.getById(addonId);
        System.out.println(testAddons.get(addonTestListId));
        System.out.println(addon);
        assertEquals("Not null", testAddons.get(addonTestListId), addon);
        addon = addonDao.getById(456);
        assertNull("Null", addon);
    }

    @Test
    public void testUpdate() {
        int addonTestListId = 1;
        Item addon = testAddons.get(addonTestListId);
        int addonId = 7;
        long amount = addon.getPrice();
        addon.setPrice(0);

        addonDao.update(addon);

        assertEquals("1", 0, addonDao.getById(addonId).getPrice());
        addon.setPrice(amount);
        addonDao.update(addon);
        assertEquals("2", testAddons.get(addonTestListId).getPrice(), addonDao.getById(addonId).getPrice());

    }

    @Test
    public void testInsertDelete() {
        int addonTestListId = 1;
        Item addon = testAddons.get(addonTestListId);
        int size = testAddons.size();
        int addonId = 7;
        addon.setId(0);
        int newAddonId = addonDao.insert(addon).getId();

        addon.setId(newAddonId);

        assertEquals("1", addon, addonDao.getById(newAddonId));
        addon.setId(addonId);
        assertEquals("2", size + 1, addonDao.getAll().size());
        addonDao.deleteById(newAddonId);
        assertNull("3", addonDao.getById(newAddonId));
        assertEquals("4", size, addonDao.getAll().size());

    }

    @Test
    public void testUpdateQuantity() {
        int addonTestListId = 1;
        Item testAddon = testAddons.get(addonTestListId);
        Item addon = new Item();
        int newQuantity = testAddon.getQuantity() + 1;
        addon.setId(testAddon.getId());
        addon.setQuantity(newQuantity);
        addon.setPrice(0);
        addon.setName("");

        addonDao.updateQuantity(addon);

        assertEquals("1", newQuantity, addonDao.getById(testAddon.getId()).getQuantity());
        addon.setQuantity(testAddon.getQuantity());
        addonDao.updateQuantity(addon);
        addon = addonDao.getById(testAddon.getId());
        assertEquals("2", testAddon, addon);


    }

    @Test
    public void testGetAllFromList() throws Exception {
        List<Item> itemsToRetrive = new ArrayList<>();
        Item addon = Addons.CREAM.getCopy();
        addon.setQuantity(4);
        itemsToRetrive.add(addon);
        addon = Addons.CINNAMON.getCopy();
        addon.setQuantity(4);
        itemsToRetrive.add(addon);
        assertEquals(itemsToRetrive, addonDao.getAllFromList(itemsToRetrive));
    }

    @Test
    public void testGetAllByIds() throws Exception {
        List<Item> itemsToRetrive = new ArrayList<Item>() {{
            add(Addons.CREAM.getCopy());
            add(Addons.CINNAMON.getCopy());
        }};
        Set<Integer> ids = new TreeSet<Integer>(){{add(9);add(13);}};
        assertEquals(itemsToRetrive, addonDao.getAllByIds(ids));
    }

}
