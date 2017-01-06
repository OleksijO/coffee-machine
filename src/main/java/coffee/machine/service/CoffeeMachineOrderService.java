package coffee.machine.service;

import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Drink;

import java.util.List;

/**
 * This class represents service for main project logic
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CoffeeMachineOrderService {

    /**
     * Decreases quantities of drink and addons in them in database,
     * withdraws from user's account and adds to coffee-machine account total price of drink list,
     * forms order, stores it in database and returns it.
     *
     * Throws application exception with message key in case of impossibility to perform mentioned operations.
     *
     * @param drinks Specified by user drinks to buy with addons quantities in addon sets inside
     * @param userId User's id  drinks to sell for.
     * @return History record, which contains date, price and detailed description of purchased drinks
     */
    Order prepareDrinksForUser(List<Drink> drinks, int userId);

}
