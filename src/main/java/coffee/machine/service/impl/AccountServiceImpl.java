package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.model.value.object.CreditsReceipt;
import coffee.machine.service.AccountService;
import coffee.machine.service.exception.ServiceException;

import java.util.Optional;

/**
 * This class is an implementation of AccountService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AccountServiceImpl implements AccountService {
    private static final String AMOUNT_FOR_ADD_SHOULD_BE_GREATER_THAN_ZERO_FORMAT =
            "Amount to add should be greater than zero. UserId=%d, amount=%d.";
    private static final String CANT_FIND_ACCOUNT_WITH_ID = "Can't find account with id = ";
    private static final String USER_ID_SHOULD_BE_GREATER_ZERO = "User's id=%d and should be greater zero";

    DaoFactory daoFactory = DaoFactoryImpl.getInstance();

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
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            return accountDao.getById(id);
        }
    }

    @Override
    public Optional<Account> getByUserId(int userId) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            return accountDao.getByUserId(userId);
        }
    }

    @Override
    public void addCredits(CreditsReceipt receipt) {
        checkReceipt(receipt);
        long amountToAdd = receipt.getAmount();
        int userId = receipt.getUserId();

        try (AbstractConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            connection.beginSerializableTransaction();

            accountDao.update(accountDao.getByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException(CANT_FIND_ACCOUNT_WITH_ID + userId))
                    .add(amountToAdd));

            connection.commitTransaction();
        }

    }

    private void checkReceipt(CreditsReceipt receipt) {
        long amountToAdd = receipt.getAmount();
        int userId = receipt.getUserId();

        if (amountToAdd <= 0) {
            throw new ServiceException(
                    String.format(AMOUNT_FOR_ADD_SHOULD_BE_GREATER_THAN_ZERO_FORMAT, userId, amountToAdd));
        }

        if (userId <= 0) {
            throw new ServiceException(
                    String.format(USER_ID_SHOULD_BE_GREATER_ZERO, userId));
        }
    }
}
