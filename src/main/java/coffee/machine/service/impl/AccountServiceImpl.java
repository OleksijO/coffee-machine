package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.service.AccountService;
import coffee.machine.service.exception.ServiceException;

/**
 * This class is an implementation of AccountService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AccountServiceImpl implements AccountService {
    private static final String AMOUNT_FOR_ADD_SHOULD_BE_GREATER_THAN_ZERO_FORMAT =
            "Amount to add should be greater than zero. UserId=%d, amount=%d.";
    private static final String CANT_FIND_ACCOUNT_WITH_ID = "Can't find account with id = ";

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
    public Account getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            connection.beginTransaction();
            Account account = accountDao.getById(id);
            connection.commitTransaction();
            return account;
        }
    }

    @Override
    public Account getByUserId(int userId) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            connection.beginTransaction();
            Account account = accountDao.getByUserId(userId);
            connection.commitTransaction();
            return account;
        }
    }

    @Override
    public void addToAccountByUserId(int userId, long amountToAdd) {
        if (amountToAdd <= 0) {
            throw new ServiceException(
                    String.format(AMOUNT_FOR_ADD_SHOULD_BE_GREATER_THAN_ZERO_FORMAT, userId,amountToAdd));
        }
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            connection.beginTransaction();
            Account account = accountDao.getByUserId(userId);
            if (account == null) {
                throw new IllegalArgumentException(CANT_FIND_ACCOUNT_WITH_ID + userId);
            }
            account.add(amountToAdd);
            accountDao.update(account);
            connection.commitTransaction();
        }

    }
}
