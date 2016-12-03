package coffee.machine.controller.impl.command.request.data.extractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This class represents parameter values from purchase drinks form extractor
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface PurchaseFormDataExtractor {
    /**
     * @param request request instance
     * @return  The map of pairs (drinkId, specifiedQuantity)  with nonZero quantity..
     */
    Map<Integer,Integer> getDrinksQuantityByIdFromRequest(HttpServletRequest request);


    /**
     * @param request request instance
     * @return  The map of pairs ( drinkId, map of pairs(addonId, specifiedQuantity) )  with nonZero quantity..
     */
    Map<Integer,Map<Integer,Integer>> getAddonsQuantityInDrinksByIdFromRequest(HttpServletRequest request);
}
