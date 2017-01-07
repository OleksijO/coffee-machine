package integration.dao;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.UserDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.User;
import data.test.entity.Users;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Performs tests of corresponding DAO on real test database (it should be already created)
 *
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
    public static void initTestDataBase() throws Exception {
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
        List<User> testUsersOrderedByFullName = new ArrayList<>();
        testUsersOrderedByFullName.add(testUsers.get(2));
        testUsersOrderedByFullName.add(testUsers.get(0));
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
        User user = userDao.getUserByLogin(userLogin).get();
        System.out.println(testUsers.get(userTestListId));
        System.out.println(user);
        assertEquals("Not null", testUsers.get(userTestListId), user);

    }

    @Test
    public void testUpdate() {

        User user = Users.A.user;
        int updatedUserId = user.getId();
        String savedFullName = user.getFullName();
        user = getUserCopyWithChangedFullName(user, "1111");


        userDao.update(user);

        assertEquals("1", "1111", userDao.getById(updatedUserId).getFullName());
        user = getUserCopyWithChangedFullName(user, savedFullName);;
        userDao.update(user);
        assertEquals("2", Users.A.user.getFullName(), userDao.getById(updatedUserId).getFullName());

    }

    @Test
    public void testInsertDelete() {
        User user = Users.A.user;
        int initialNumberOfUsers = Users.values().length;
        user = getUserCopyWithChangedEmailAndId(user, user.getEmail() + ".ua", 0);

        int newUserId = userDao.insert(user).getId();
        User userToTest = userDao.getById(newUserId);

        assertEquals("1", user, userToTest);
        assertEquals("2", initialNumberOfUsers + 1, userDao.getAll().size());
        userDao.deleteById(newUserId);
        assertNull("3", userDao.getById(newUserId));
        assertEquals("4", initialNumberOfUsers, userDao.getAll().size());

    }

    private User getUserCopyWithChangedEmailAndId(User user, String email, int id) {
        return new User.Builder()
                .setId(id)
                .setEmail(email)
                .setPassword(user.getPassword())
                .setFullName(user.getFullName())
                .setAdmin(user.isAdmin())
                .build();
    }

    private User getUserCopyWithChangedFullName(User user, String fullName) {
        return new User.Builder()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setFullName(fullName)
                .setAdmin(user.isAdmin())
                .setAccount(user.getAccount())
                .build();
    }

}
