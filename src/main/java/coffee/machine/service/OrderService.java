package coffee.machine.service;

import coffee.machine.model.entity.Order;

import java.util.List;

/**
 * This class represents order service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface OrderService {
    List<Order> getAllByUserId(int userId);
}
