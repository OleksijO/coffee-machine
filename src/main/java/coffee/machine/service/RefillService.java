package coffee.machine.service;

import coffee.machine.model.value.object.ProductsReceipt;

/**
 * This class represents service for main project logic
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface RefillService {

    /**
     * Refills coffee machine with quantity of drinks and addons specified in receipt
     *
     * @param receipt Contains lists of drinks and addons to refill
     */
    void refill(ProductsReceipt receipt);

}
