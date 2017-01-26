package coffee.machine.service.impl;

import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoManager;
import coffee.machine.dao.DaoManagerFactory;
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
public class AccountServiceImpl extends GenericService implements AccountService {
    private static final String CANT_FIND_ACCOUNT_OF_USER_WITH_ID = "Can't find account of user with id = ";

    private AccountServiceImpl(DaoManagerFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final AccountService instance =
                new AccountServiceImpl(DaoFactoryImpl.getInstance());
    }

    public static AccountService getInstance() {
        return AccountServiceImpl.InstanceHolder.instance;
    }

    @Override
    public Optional<Account> getById(int id) {
        return executeInNonTransactionalWrapper(daoManager ->
                daoManager
                        .getAccountDao()
                        .getById(id)
        );
    }

    @Override
    public Optional<Account> getByUserId(int userId) {
        return executeInNonTransactionalWrapper(daoManager ->
                daoManager
                        .getAccountDao()
                        .getByUserId(userId)
        );
    }

    @Override
    public void addCredits(CreditsReceipt receipt) {
        Objects.requireNonNull(receipt);


        executeInSerializableTransactionalVoidWrapper(
                daoManager ->
                        AddCreditsByReceiptData(receipt, daoManager)
        );

    }

    private void AddCreditsByReceiptData(CreditsReceipt receipt, DaoManager daoManager) {
        AccountDao accountDao = daoManager.getAccountDao();
        daoManager.getAccountDao().update(
                accountDao
                        .getByUserId(receipt.getUserId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(CANT_FIND_ACCOUNT_OF_USER_WITH_ID + receipt.getUserId()))
                        .add(receipt.getAmount()));
    }
}
