package coffee.machine.service.impl.lambda.based;

import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.model.value.object.CreditsReceipt;
import coffee.machine.service.AccountService;
import coffee.machine.service.impl.lambda.based.wrapper.GenericService;

import java.util.Objects;
import java.util.Optional;

/**
 * This class is an implementation of AccountService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AccountServiceImplNew extends GenericService implements AccountService {
    private static final String CANT_FIND_ACCOUNT_OF_USER_WITH_ID = "Can't find account of user with id = ";

    private AccountServiceImplNew(DaoFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final AccountService instance =
                new AccountServiceImplNew(DaoFactoryImpl.getInstance());
    }

    public static AccountService getInstance() {
        return AccountServiceImplNew.InstanceHolder.instance;
    }

    @Override
    public Optional<Account> getById(int id) {
        return executeInNonTransactionalWrapper(connection ->
                daoFactory.getAccountDao(connection)
                        .getById(id)
        );
    }

    @Override
    public Optional<Account> getByUserId(int userId) {
        return executeInNonTransactionalWrapper(connection ->
                daoFactory.getAccountDao(connection)
                        .getByUserId(userId)
        );
    }

    @Override
    public void addCredits(CreditsReceipt receipt) {
        Objects.requireNonNull(receipt);
        long amountToAdd = receipt.getAmount();
        int userId = receipt.getUserId();

        executeInSerializableTransactionalVoidWrapper(connection -> {

            AccountDao accountDao = daoFactory.getAccountDao(connection);
            accountDao.update(
                    accountDao.getByUserId(userId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(CANT_FIND_ACCOUNT_OF_USER_WITH_ID + userId))
                            .add(amountToAdd));

        });

    }
}
