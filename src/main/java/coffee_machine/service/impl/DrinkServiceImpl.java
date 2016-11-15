package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.DrinkDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.goods.Drink;
import coffee_machine.service.DrinkService;
import coffee_machine.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class DrinkServiceImpl implements DrinkService {
    private static final Logger logger = Logger.getLogger(DrinkServiceImpl.class);
    private static DrinkServiceImpl instance;
    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    public static DrinkService getInstance() {
        DrinkServiceImpl localInstance = instance;
        if (instance == null) {
            synchronized (DrinkServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DrinkServiceImpl();
                }
            }
        }
        return localInstance;
    }


    @Override
    public Drink create(Drink drink) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            try {
                connection.beginTransaction();
                drinkDao.insert(drink);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
        return drink;
    }

    private void logErrorAndThrowWrapperServiceException(String message, Throwable e) {
        logger.error(message, e);
        throw new ServiceException(message, e);
    }


    @Override
    public void update(Drink drink) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            try {
                connection.beginTransaction();
                drinkDao.update(drink);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
    }

    @Override
    public List<Drink> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            List<Drink> drinks = null;
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            try {
                connection.beginTransaction();
                drinks = drinkDao.getAll();
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
            return (drinks == null) ? new ArrayList<>() : drinks;
        }
    }

    @Override
    public Drink getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            Drink drink = null;
            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            try {
                connection.beginTransaction();
                drink = drinkDao.getById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
            return drink;
        }

    }

    @Override
    public void delete(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            DrinkDao drinkDao = daoFactory.getDrinkDao(connection);
            try {
                connection.beginTransaction();
                drinkDao.deleteById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
    }

}
