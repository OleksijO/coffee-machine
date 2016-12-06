package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.OrderDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Order;
import coffee.machine.service.OrderService;

import java.util.List;

/**
 * This class is an implementation of OrderService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class OrderServiceImpl implements OrderService {
    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private OrderServiceImpl() {
    }

    private static class InstanceHolder {
        private static final OrderService instance = new OrderServiceImpl();
    }

    public static OrderService getInstance() {
        return OrderServiceImpl.InstanceHolder.instance;
    }

    @Override
    public List<Order> getAllByUserId(int userId) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            OrderDao orderDao = daoFactory.getOrderDao(connection);
            connection.beginTransaction();
            List<Order> orders =  orderDao.getAllByUserId(userId);
            connection.commitTransaction();
            return orders;

        }
    }

}
