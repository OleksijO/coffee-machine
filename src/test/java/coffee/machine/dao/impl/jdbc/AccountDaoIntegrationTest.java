package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.model.entity.Account;
import data.test.entity.AccountsData;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static data.test.entity.UsersData.A;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Performs tests of corresponding DAO on real test database (it should be already created)
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AccountDaoIntegrationTest {
    private final DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private final List<Account> testAccounts = new ArrayList<>();
    private DaoConnection connection;
    private AccountDao accountDao;

    {
        for (AccountsData accountEnum : AccountsData.values()) {
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
    public void tearDown() {
        connection.commitTransaction();
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<Account> accounts = accountDao.getAll();
        assertEquals(testAccounts, accounts);

    }

    @Test
    public void testGetById() {
        Account testAccount = AccountsData.USER_A.getCopy();
        Account account = accountDao.getById(testAccount.getId())
                .orElse(null);
        assertEquals(testAccount, account);
    }

    @Test
    public void testGetByIdIsNotPresent() {
        assertFalse(accountDao.getById(1005001).isPresent());
    }

    @Test
    public void testUpdate() {
        Account account = testAccounts.get(1);
        long amount = account.getAmount();
        account.withdraw(amount);

        accountDao.update(account);

        assertEquals("account amount must be updated", 0, accountDao.getById(2).get().getAmount());
        account.add(amount);
        accountDao.update(account);
        assertEquals("check state after test",
                testAccounts.get(1).getAmount(), accountDao.getById(2).get().getAmount());

    }

    @Test
    public void testInsertDelete() {
        Account account = testAccounts.get(1);
        account.setId(0);
        int newAccountId = accountDao.insert(account).getId();

        account.setId(newAccountId);

        assertEquals("New entity should be placed to DB and be the same to test one",
                account, accountDao.getById(newAccountId).get());
        account.setId(2);
        assertEquals("Total count of entities should increase by 1", 4, accountDao.getAll().size());
        accountDao.deleteById(newAccountId);
        assertFalse("Inserted entity should be deleted", accountDao.getById(newAccountId).isPresent());
        assertEquals("Total count of entities should decrease by 1", 3, accountDao.getAll().size());

    }

    @Test
    public void testByUserId() {

        assertEquals(AccountsData.USER_A.getCopy(),
                accountDao.getByUserId(A.user.getId())
                        .orElseThrow(NoSuchElementException::new));

    }

}
