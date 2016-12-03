package coffee.machine.service;

import coffee.machine.model.entity.HistoryRecord;
import coffee.machine.model.entity.item.Drink;

import java.util.List;

/**
 * This class represents service for main project logic
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CoffeeMachineService {

    /**
     * Decreases quantities of drink and addons in them in database,
     * withdrows from user's account and adds to coffee-machine account total price of drink list,
     * forms history record.
     *
     * Throws application exception with message key in case of impossibility to perform mentioned operations.
     *
     * @param drinks Specified by user drinks to buy with addons quantities in addon sets inside
     * @param userId User's id  drinks to sell for.
     * @return History record, which contains date, price and detailed description of purchased drinks
     */
    HistoryRecord prepareDrinksForUser(List<Drink> drinks, int userId);

}
