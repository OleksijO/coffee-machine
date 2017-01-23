package coffee.machine.dao;

import coffee.machine.model.entity.Order;
import coffee.machine.model.value.object.Orders;

import java.util.List;

/**
 * This class represents order entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface OrderDao extends GenericDao<Order> {

    /**
     * @param userId user's id
     * @return list of history record corresponding specified user id
     */
    List<Order> getAllByUserId(int userId);

    /**
     * @param userId user's id, whose orders will be returned
     * @param startFrom Only orders, starting from this offset number in all history, will be placed to result.
     * @param quantity Number of orders to be placed in result, starting form specified
     * @return Value object with list of orders of user with specified id and total count number
     */
    Orders getAllByUserId(int userId, int startFrom, int quantity);
}