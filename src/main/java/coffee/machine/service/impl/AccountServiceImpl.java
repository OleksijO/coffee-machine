package coffee.machine.service.impl;

import coffee.machine.dao.AccountDao;
import coffee.machine.service.AccountService;
import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import org.apache.log4j.Logger;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
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
