package coffee.machine.service.impl;

import coffee.machine.dao.DaoManagerFactory;
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
public class OrderServiceImpl extends GenericService implements OrderService {

    private OrderServiceImpl(DaoManagerFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final OrderService instance = new OrderServiceImpl(DaoFactoryImpl.getInstance());
    }

    public static OrderService getInstance() {
        return OrderServiceImpl.InstanceHolder.instance;
    }

    @Override
    public List<Order> getAllByUserId(int userId) {

        return executeInNonTransactionalWrapper(daoManager ->
                daoManager
                        .getOrderDao()
                        .getAllByUserId(userId)
        );
    }

    @Override
    public Orders getOrdersByUserWithLimits(int userId, int startFrom, int quantity) {

        return executeInNonTransactionalWrapper(daoManager ->
                daoManager
                        .getOrderDao()
                        .getAllByUserId(userId, startFrom, quantity)
        );
    }

}
