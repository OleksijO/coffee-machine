package integration.dao;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.UserDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.user.User;
import data.entity.Users;
import org.junit.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserDaoTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<User> testUsers = new ArrayList<>();
    private AbstractConnection connection;
    private UserDao userDao;

    {
        for (Users userEnum : Users.values()) {
            testUsers.add(userEnum.user);
        }
    }

    @BeforeClass
    public static void initTestDataBase() throws SQLException, ClassNotFoundException, InterruptedException, IOException {
        new TestDatabaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        System.out.println(testUsers);
        connection = daoFactory.getConnection();
        userDao = daoFactory.getUserDao(connection);
        connection.beginTransaction();
    }

    @After
    public void post() {
        connection.close();
    }

    @Test
    public void testGetAll() {
        List<User> testUsersOrderedByFullName=new ArrayList<>();
        testUsersOrderedByFullName.add(testUsers.get(0));
        testUsersOrderedByFullName.add(testUsers.get(2));
        testUsersOrderedByFullName.add(testUsers.get(1));
        List<User> users = userDao.getAll();
        System.out.println(testUsers);
        System.out.println(users);
        assertEquals(testUsersOrderedByFullName, users);

    }

   
    @Test
    public void testGetById() {
        int userId = 2;
        int userTestListId = 1;
        User user = userDao.getById(userId);
        System.out.println(testUsers.get(userTestListId));
        System.out.println(user);
        assertEquals("Not null", testUsers.get(userTestListId), user);
        user = userDao.getById(456);
        assertNull("Null", user);
    }

    @Test
    public void testGetByLogin() {

        int userTestListId = 1;
        String userLogin = testUsers.get(userTestListId).getEmail();
        User user = userDao.getUserByLogin(userLogin);
        System.out.println(testUsers.get(userTestListId));
        System.out.println(user);
        assertEquals("Not null", testUsers.get(userTestListId), user);

    }

    @Test
    public void testUpdate() {
        int userTestListId = 1;
        User user = testUsers.get(userTestListId);
        int userId = 2;
        
        String fullName = user.getFullName();
        user.setFullName("");

        userDao.update(user);

        assertEquals("1", "", userDao.getById(userId).getFullName());
        user.setFullName(fullName);
        userDao.update(user);
        assertEquals("2", testUsers.get(userTestListId).getFullName(), userDao.getById(userId).getFullName());

    }

    @Test
    public void testInsertDelete() {
        int userTestListId = 1;
        User user = testUsers.get(userTestListId);
        int size = testUsers.size();
        int userId = 2;
        int accountId=user.getAccount().getId();
                user.setId(0);
        user.setEmail(user.getEmail()+".ua");
        user.getAccount().setId(0);
        int newUserId = userDao.insert(user).getId();
        User userToTest = userDao.getById(newUserId);
        assertEquals("1", user, userToTest);
        user.setEmail(user.getEmail().replace(".ua",""));
        user.setId(userId);
        user.getAccount().setId(accountId);
        assertEquals("2", size + 1, userDao.getAll().size());
        userDao.deleteById(newUserId);
        assertNull("3", userDao.getById(newUserId));
        assertEquals("4", size, userDao.getAll().size());

    }

}
