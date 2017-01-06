package coffee.machine.service;

import coffee.machine.model.entity.item.ItemReceipt;

/**
 * This class represents service for main project logic
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CoffeeMachineRefillService {

    /**
     * Refills coffee machine with quantity of drinks and items specified in receipt
     *
     * @param receipt Contains lists of drinks and addons to refill
     */
    void refill(ItemReceipt receipt);

}
