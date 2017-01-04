package coffee.machine.controller.command.request.data.extractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This class represents parameter values from refill form extractor
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface RefillFormDataExtractor {
    /**
     * @param request request instance
     * @return  The map of pairs (drinkId, specifiedQuantity)  with nonZero quantity.
     */
    Map<Integer,Integer> getDrinksQuantityByIdFromRequest(HttpServletRequest request);

    /**
     * @param request request instance
     * @return  The map of pairs (addonId, specifiedQuantity)  with nonZero quantity.
     */
    Map<Integer,Integer> getAddonsQuantityByIdFromRequest(HttpServletRequest request);
}
