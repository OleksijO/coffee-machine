package coffee.machine.service.impl;

import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.model.value.object.CreditsReceipt;
import coffee.machine.service.AccountService;

import java.util.Objects;
import java.util.Optional;

/**
 * This class is an implementation of AccountService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AccountServiceImpl implements AccountService {
    private static final String CANT_FIND_ACCOUNT_OF_USER_WITH_ID = "Can't find account of user with id = ";

    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private AccountServiceImpl() {
    }

    private static class InstanceHolder {
        private static final AccountService instance = new AccountServiceImpl();
    }

    public static AccountService getInstance() {
        return AccountServiceImpl.InstanceHolder.instance;
    }

    @Override
    public Optional<Account> getById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            return accountDao.getById(id);
        }
    }

    @Override
    public Optional<Account> getByUserId(int userId) {
        try (DaoConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            return accountDao.getByUserId(userId);
        }
    }

    @Override
    public void addCredits(CreditsReceipt receipt) {
        Objects.requireNonNull(receipt);
        long amountToAdd = receipt.getAmount();
        int userId = receipt.getUserId();

        try (DaoConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            connection.beginSerializableTransaction();

            accountDao.update(accountDao.getByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException(CANT_FIND_ACCOUNT_OF_USER_WITH_ID + userId))
                    .add(amountToAdd));

            connection.commitTransaction();
        }

    }
}
