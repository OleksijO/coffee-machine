package integration.dao;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.AddonDao;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.goods.Addon;
import data.Addons;
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
public class AddonDaoTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<Addon> testAddons = new ArrayList<>();
    private AbstractConnection connection;
    private AddonDao addonDao;

    {
        for (Addons addonEnum : Addons.values()) {
            testAddons.add(addonEnum.addon);
        }

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

        List<Addon> addons = addonDao.getAll();
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

        List<Integer> addonIds =new ArrayList<>();
        testAddons.forEach(addon->addonIds.add(addon.getId()));
        assertEquals(testAddons, addonDao.getAllFromList(testAddons));

    }

    @Test
    public void testGetById() {
        int addonId=7;
        int addonTestListId = 1;
        Addon addon = addonDao.getById(addonId);
        System.out.println(testAddons.get(addonTestListId));
        System.out.println(addon);
        assertEquals("Not null", testAddons.get(addonTestListId), addon);
        addon = addonDao.getById(456);
        assertNull("Null", addon);
    }

    @Test
    public void testUpdate() {
        int addonTestListId = 1;
        Addon addon = testAddons.get(addonTestListId);
        int addonId=7;
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
        Addon addon = testAddons.get(addonTestListId);
        int size = testAddons.size();
        int addonId=7;
        addon.setId(0);
        int newAddonId = addonDao.insert(addon).getId();

        addon.setId(newAddonId);

        assertEquals("1", addon, addonDao.getById(newAddonId));
        addon.setId(addonId);
        assertEquals("2", size+1, addonDao.getAll().size());
        addonDao.deleteById(newAddonId);
        assertNull("3", addonDao.getById(newAddonId));
        assertEquals("4", size, addonDao.getAll().size());

    }

    @Test
    public void testUpdateQuantity() {
        int addonTestListId = 1;
        Addon testAddon = testAddons.get(addonTestListId);
        Addon addon = new Addon();
        int newQuantity = testAddon.getQuantity() + 1;
        addon.setId(testAddon.getId());
        addon.setQuantity(newQuantity);
        addon.setPrice(0);
        addon.setName("");

        addonDao.updateQuantity(addon);

        assertEquals("1", newQuantity, addonDao.getById(testAddon.getId()).getQuantity());
        addon.setQuantity(testAddon.getQuantity());
        assertEquals("2", testAddon, addonDao.getById(testAddon.getId()));
        addonDao.updateQuantity(addon);

    }

}
