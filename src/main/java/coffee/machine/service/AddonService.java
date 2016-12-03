package coffee.machine.service;


import coffee.machine.model.entity.item.Item;

import java.util.List;
import java.util.Map;

/**
 * This class represents addon service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface AddonService {

    List<Item> getAll();

    /**
     * Adds specified quantity for every addon with specified id
     *
     * @param quantitiesById Map of pairs (addonId, quantityToAdd)
     */
    void refill(Map<Integer, Integer> quantitiesById);

}
