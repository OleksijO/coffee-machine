package coffee.machine.service;


import coffee.machine.model.entity.product.Product;

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
    List<Product> getAll();

}
