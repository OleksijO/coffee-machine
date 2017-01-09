package coffee.machine.service;


import coffee.machine.model.entity.item.Item;

import java.util.List;

/**
 * This class represents addon service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface AddonService {

    /**
     * @return list of all addons with specified available quantity.
     */
    List<Item> getAll();

}
