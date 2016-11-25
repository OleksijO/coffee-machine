package integration.dao;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.AdminDao;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.user.Admin;
import data.Admins;
import org.junit.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
@Ignore
public class AdminDaoTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<Admin> testAdmins = new ArrayList<>();
    private AbstractConnection connection;
    private AdminDao adminDao;

    {
        for (Admins adminEnum : Admins.values()) {
            testAdmins.add(adminEnum.admin);
        }
    }

    @BeforeClass
    public static void initTestDataBase() throws SQLException, ClassNotFoundException, InterruptedException, IOException {
        new TestDataBaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        System.out.println(testAdmins);
        connection = daoFactory.getConnection();
        adminDao = daoFactory.getAdminDao(connection);
        connection.beginTransaction();
    }

    @After
    public void post() {
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<Admin> admins = adminDao.getAll();
        System.out.println(testAdmins);
        System.out.println(admins);
        assertEquals(testAdmins, admins);

    }

   
    @Test
    public void testGetById() {
        int adminId = 3;
        int adminTestListId = 1;
        Admin admin = adminDao.getById(adminId);
        System.out.println(testAdmins.get(adminTestListId));
        System.out.println(admin);
        assertEquals("Not null", testAdmins.get(adminTestListId), admin);
        admin = adminDao.getById(456);
        assertNull("Null", admin);
    }

    @Test
    public void testGetByLogin() {

        int adminTestListId = 1;
        String adminLogin = testAdmins.get(adminTestListId).getEmail();
        Admin admin = adminDao.getAdminByLogin(adminLogin);
        System.out.println(testAdmins.get(adminTestListId));
        System.out.println(admin);
        assertEquals("Not null", testAdmins.get(adminTestListId), admin);
        admin = adminDao.getById(456);
        assertNull("Null", admin);
    }

    @Test
    public void testUpdate() {
        int adminTestListId = 1;
        Admin admin = testAdmins.get(adminTestListId);
        int adminId = 3;
        
        String fullName = admin.getFullName();
        admin.setFullName("");

        adminDao.update(admin);

        assertEquals("1", "", adminDao.getById(adminId).getFullName());
        admin.setFullName(fullName);
        adminDao.update(admin);
        assertEquals("2", testAdmins.get(adminTestListId).getFullName(), adminDao.getById(adminId).getFullName());

    }

    @Test
    public void testInsertDelete() {
        int adminTestListId = 1;
        Admin admin = testAdmins.get(adminTestListId);
        int size = testAdmins.size();
        int adminId = 3;

                admin.setId(0);
        admin.setEmail(admin.getEmail()+".ua");

        int newAdminId = adminDao.insert(admin).getId();
        admin.setId(newAdminId);
        Admin adminToTest = adminDao.getById(newAdminId);
        assertEquals("1", admin, adminToTest);
        admin.setEmail(admin.getEmail().replace(".ua",""));
        admin.setId(adminId);

        assertEquals("2", size + 1, adminDao.getAll().size());
        adminDao.deleteById(newAdminId);
        assertNull("3", adminDao.getById(newAdminId));
        assertEquals("4", size, adminDao.getAll().size());

    }

}
