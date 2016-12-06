package coffee.machine.dao;

import coffee.machine.model.entity.Order;

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

}