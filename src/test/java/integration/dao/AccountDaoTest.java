package integration.dao;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import data.test.entity.Accounts;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static data.test.entity.Users.A;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Performs tests of corresponding DAO on real test database (it should be already created)
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AccountDaoTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<Account> testAccounts = new ArrayList<>();
    private AbstractConnection connection;
    private AccountDao accountDao;

    {
        for (Accounts accountEnum : Accounts.values()) {
            testAccounts.add(accountEnum.getCopy());
        }
    }

    @BeforeClass
    public static void initTestDataBase() throws Exception {
        new TestDatabaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        connection = daoFactory.getConnection();
        accountDao = daoFactory.getAccountDao(connection);
        connection.beginTransaction();
    }

    @After
    public void post() {
        connection.commitTransaction();
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<Account> accounts = accountDao.getAll();
        System.out.println(testAccounts);
        System.out.println(accounts);
        assertEquals(testAccounts, accounts);

    }

    @Test
    public void testGetById() {

        Account account = accountDao.getById(2);
        System.out.println(testAccounts.get(1));
        System.out.println(account);
        assertEquals("Not null", testAccounts.get(1), account);
        account = accountDao.getById(4);
        assertNull("Null", account);
    }

    @Test
    public void testUpdate() {
        Account account=testAccounts.get(1);
        long amount = account.getAmount();
        account.setAmount(0);

        accountDao.update(account);

        assertEquals("1", 0, accountDao.getById(2).getAmount());
        account.setAmount(amount);
        accountDao.update(account);
        assertEquals("2", testAccounts.get(1).getAmount(), accountDao.getById(2).getAmount());

    }

    @Test
    public void testInsertDelete() {
        Account account=testAccounts.get(1);
        account.setId(0);
        int newAccountId=accountDao.insert(account).getId();

        account.setId(newAccountId);

        assertEquals("1", account, accountDao.getById(newAccountId));
        account.setId(2);
        assertEquals("2", 4, accountDao.getAll().size());
        accountDao.deleteById(newAccountId);
        assertNull("3", accountDao.getById(newAccountId));
        assertEquals("4", 3, accountDao.getAll().size());

    }

    @Test
    public void testByUserId() {

        assertEquals( testAccounts.get(1), accountDao.getByUserId(A.user.getId()));

    }

}
