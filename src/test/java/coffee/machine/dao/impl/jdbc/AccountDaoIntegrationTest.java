package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoManagerFactory;
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
    private final DaoManagerFactory daoFactory = DaoFactoryImpl.getInstance();
    private final List<Account> testAccounts = new ArrayList<>();
    private DaoManager daoManager;
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
        daoManager = daoFactory.createDaoManager();
        accountDao = daoManager.getAccountDao();
        daoManager.beginTransaction();
    }

    @After
    public void tearDown() {
        daoManager.commitTransaction();
        daoManager.close();
    }

    @Test
    public void testGetAll() {

        List<Account> accounts = accountDao.getAll();
        assertEquals(testAccounts, accounts);

    }

    @Test
    public void testGetByIdReturnsOptionalWithCorrectAccountIfAccountIsPresent() {
        Account testAccount = AccountsData.USER_A.getCopy();
        Account account = accountDao
                .getById(testAccount.getId())
                .orElse(null);
        assertEquals(testAccount, account);
    }

    @Test
    public void testGetByIdIsNotPresent() {
        assertFalse(accountDao.getById(1005001).isPresent());
    }

    @Test
    public void testUpdate() {
        Account account = AccountsData.USER_A.getCopy();
        long savedAmount = account.getAmount();
        account.withdraw(savedAmount);

        accountDao.update(account);

        assertEquals("account amount must be updated",
                0, accountDao.getById(account.getId()).get().getAmount());
        account.add(savedAmount);
        accountDao.update(account);
        assertEquals("Check DB state after test",
                account.getAmount(), accountDao.getById(account.getId()).get().getAmount());

    }

    @Test
    public void testDelete() {
        Account account = AccountsData.USER_A.getCopy();
        account.setId(0);
        int newAccountId = accountDao.insert(account).getId();
        assertEquals("Quantity of account should increase by 1. It is necessary test condition ",
                testAccounts.size() + 1, accountDao.getAll().size());

        accountDao.deleteById(newAccountId);
        assertFalse("Inserted account should be deleted.", accountDao.getById(newAccountId).isPresent());

    }

    @Test
    public void testInsert() {
        Account account = AccountsData.USER_A.getCopy();
        account.setId(0);
        int newAccountId = accountDao.insert(account).getId();

        assertEquals("New entity should be placed to DB and be the same to test one",
                account, accountDao.getById(newAccountId).get());
        accountDao.deleteById(newAccountId);
        assertFalse("Check DB state after test", accountDao.getById(newAccountId).isPresent());

    }

    @Test
    public void testByUserId() {

        assertEquals(AccountsData.USER_A.getCopy(),
                accountDao.getByUserId(A.user.getId())
                        .orElseThrow(NoSuchElementException::new));

    }

}
