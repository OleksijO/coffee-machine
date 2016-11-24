package coffee_machine.service.impl;

import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.Account;
import coffee_machine.service.AccountService;
import org.apache.log4j.Logger;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class AccountServiceImpl extends AbstractService implements AccountService {
    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);

    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    public AccountServiceImpl() {
        super(logger);
    }

    private static class InstanceHolder {
        private static AccountService instance = new AccountServiceImpl();
    }

    public static AccountService getInstance() {
        return AccountServiceImpl.InstanceHolder.instance;
    }


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
