package integration.dao;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.UserDao;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.user.User;
import data.Users;
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

        List<User> users = userDao.getAll();
        System.out.println(testUsers);
        System.out.println(users);
        assertEquals(testUsers, users);

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
        user = userDao.getById(456);
        assertNull("Null", user);
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
        user.setId(newUserId);
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