package coffee_machine.service;


import coffee_machine.model.entity.item.Item;

import java.util.List;
import java.util.Map;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface AddonService {

    List<Item> getAll();

    void refill(Map<Integer, Integer> quantitiesById);

}
