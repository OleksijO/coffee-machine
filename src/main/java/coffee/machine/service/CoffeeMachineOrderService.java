package coffee.machine.service;

import coffee.machine.model.entity.Order;

/**
 * This class represents service for main project logic
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CoffeeMachineOrderService {

    /**
     * Decreases quantities of drink and addons in them in database,
     * withdraws from user's account and adds to coffee-machine account total price of drink list,
     * fills with absent data, stores in database and returns preOrder.
     *
     * Throws application exception with message key in case of impossibility to perform mentioned operations.
     *
     * @param preOrder Order with particular data of order. Ids and quantities of drinks and addons should present
     * @return History record, which contains date, cost and detailed description of purchased drinks
     */
    Order prepareOrder(Order preOrder);

}
