package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.UserDao;
import coffee.machine.model.entity.user.User;
import data.test.entity.UsersData;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Performs tests of corresponding DAO on real test database (it should be already created)
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserDaoIntegrationTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<User> testUsers = new ArrayList<>();
    private DaoManager daoManager;
    private UserDao userDao;

    {
        for (UsersData userEnum : UsersData.values()) {
            testUsers.add(userEnum.user);
        }
    }

    @BeforeClass
    public static void initTestDataBase() throws Exception {
        new TestDatabaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        daoManager = daoFactory.createDaoManager();
        userDao = daoManager.getUserDao();
        daoManager.beginTransaction();
    }

    @After
    public void tearDown() {
        daoManager.commitTransaction();
        daoManager.close();
    }

    @Test
    public void testGetAll() {
        List<User> testUsersOrderedByFullName = new ArrayList<>();
        testUsersOrderedByFullName.add(testUsers.get(2));
        testUsersOrderedByFullName.add(testUsers.get(0));
        testUsersOrderedByFullName.add(testUsers.get(1));
        List<User> users = userDao.getAll();
        assertEquals(testUsersOrderedByFullName, users);

    }


    @Test
    public void testGetById() {
        User testUser = UsersData.B.user;
        int userId = testUser.getId();
        User user = userDao.getById(userId).get();
        assertEquals(testUser, user);
    }

    @Test
    public void testGetByIdIsNotPreset() {
        assertFalse(userDao.getById(100500).isPresent());
    }

    @Test
    public void testGetByLogin() {
        User testUser = UsersData.B.user;
        String login = testUser.getEmail();
        User user = userDao.getUserByLogin(login).get();
        assertEquals(testUser, user);
    }

    @Test
    public void testGetByLoginIsNotPreset() {
        assertFalse(userDao.getUserByLogin("100500").isPresent());
    }

    @Test
    public void testUpdate() {

        User user = UsersData.A.user;
        int updatedUserId = user.getId();
        String savedFullName = user.getFullName();
        String newFullName = "1111";
        user = getUserCopyWithChangedFullName(user,  newFullName);

        userDao.update(user);

        assertEquals("Property should be updated", newFullName, userDao.getById(updatedUserId).get().getFullName());
        user = getUserCopyWithChangedFullName(user, savedFullName);
        userDao.update(user);
        assertEquals("DB state should be the same as before test ",
                user.getFullName(), userDao.getById(updatedUserId).get().getFullName());

    }

    @Test
    public void testInsert() {
        User user = UsersData.A.user;
        user = getUserCopyWithChangedEmailAndId(user, user.getEmail() + ".ua", 0);
        int newUserId = userDao.insert(user).getId();

        User userToTest = userDao.getById(newUserId).get();
        assertEquals("New entity should be placed to DB and be the same to test one", user, userToTest);
        userDao.deleteById(newUserId);
        assertFalse("DB state should be the same as before test", userDao.getById(newUserId).isPresent());

    }

    @Test
    public void testDelete() {
        User user = UsersData.A.user;
        int initialNumberOfUsers = UsersData.values().length;
        user = getUserCopyWithChangedEmailAndId(user, user.getEmail() + ".ua", 0);
        int newUserId = userDao.insert(user).getId();
        assertEquals("Total count of entities should increase by 1. It is necessary test condition ",
                initialNumberOfUsers + 1, userDao.getAll().size());

        userDao.deleteById(newUserId);
        assertFalse("Inserted entity should be deleted", userDao.getById(newUserId).isPresent());
    }

    private User getUserCopyWithChangedEmailAndId(User user, String email, int id) {
        return new User.Builder()
                .setId(id)
                .setEmail(email)
                .setPassword(user.getPassword())
                .setFullName(user.getFullName())
                .setRole(user.getRole())
                .setAccount(user.getAccount())
                .build();
    }

    private User getUserCopyWithChangedFullName(User user, String fullName) {
        return new User.Builder()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setFullName(fullName)
                .setRole(user.getRole())
                .setAccount(user.getAccount())
                .build();
    }

}
