package coffee.machine.service.impl;

import coffee.machine.dao.DaoConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.OrderDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Order;
import coffee.machine.model.value.object.Orders;
import coffee.machine.service.OrderService;

import java.util.List;

/**
 * This class is an implementation of OrderService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class OrderServiceImpl implements OrderService {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();

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
        try (DaoConnection connection = daoFactory.getConnection()) {

            OrderDao orderDao = daoFactory.getOrderDao(connection);
            return orderDao.getAllByUserId(userId);

        }
    }

    @Override
    public Orders getAllOrdersByUser(int userId, int startFrom, int quantity) {
        try (DaoConnection connection = daoFactory.getConnection()) {

            OrderDao orderDao = daoFactory.getOrderDao(connection);
            return orderDao.getAllByUserId(userId, startFrom, quantity);

        }
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
