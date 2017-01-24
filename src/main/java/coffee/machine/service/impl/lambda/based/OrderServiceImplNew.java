package coffee.machine.service.impl.lambda.based;

import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Order;
import coffee.machine.model.value.object.Orders;
import coffee.machine.service.OrderService;
import coffee.machine.service.impl.lambda.based.wrapper.GenericService;

import java.util.List;

/**
 * This class is an implementation of OrderService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class OrderServiceImplNew extends GenericService implements OrderService {

    private OrderServiceImplNew(DaoFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final OrderService instance = new OrderServiceImplNew(DaoFactoryImpl.getInstance());
    }

    public static OrderService getInstance() {
        return OrderServiceImplNew.InstanceHolder.instance;
    }

    @Override
    public List<Order> getAllByUserId(int userId) {

        return executeInNonTransactionalWrapper(connection ->
                daoFactory.getOrderDao(connection)
                        .getAllByUserId(userId)
        );
    }

    @Override
    public Orders getOrdersByUserWithLimits(int userId, int startFrom, int quantity) {

        return executeInNonTransactionalWrapper(connection ->
                daoFactory.getOrderDao(connection)
                        .getAllByUserId(userId, startFrom, quantity)
        );
    }

}
