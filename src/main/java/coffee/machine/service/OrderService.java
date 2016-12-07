package coffee.machine.service;

import coffee.machine.model.entity.Order;

import java.util.List;

/**
 * This class represents order service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface OrderService {
    /**
     * @param userId user's id, whose orders will be returned
     * @return list of orders of user with specified id
     */
    List<Order> getAllByUserId(int userId);
}
