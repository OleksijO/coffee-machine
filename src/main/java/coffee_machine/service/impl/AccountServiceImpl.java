package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.AccountDao;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.Account;
import coffee_machine.service.AccountService;
import org.apache.log4j.Logger;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);

    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private static class InstanceHolder {
        private static final AccountService instance = new AccountServiceImpl();
    }

    public static AccountService getInstance() {
        return AccountServiceImpl.InstanceHolder.instance;
    }


    public void update(Account account) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            connection.beginTransaction();
            accountDao.update(account);
            connection.commitTransaction();

        }
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
}
