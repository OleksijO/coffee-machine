package coffee_machine.service.impl;

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
    private static AccountService instance;
    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    public static AccountService getInstance() {
        AccountService localInstance = instance;
        if (instance == null) {
            synchronized (AddonServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AccountServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void update(Account account) {
        //TODO
    }

    @Override
    public Account getById(int id) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Account getByUserId(int userId) {
        //TODO
        throw new UnsupportedOperationException();
    }
}
