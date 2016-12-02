package coffee_machine.service;


import coffee_machine.model.entity.item.Item;

import java.util.List;
import java.util.Map;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public interface AddonService {

    List<Item> getAll();

    void refill(Map<Integer, Integer> quantitiesById);

}
