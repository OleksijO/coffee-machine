package coffee_machine.service;

import coffee_machine.model.entity.HistoryRecord;
import coffee_machine.model.entity.item.Drink;

import java.util.List;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public interface CoffeeMachineService {

    HistoryRecord prepareDrinksForUser(List<Drink> drinks, int userId);

}
