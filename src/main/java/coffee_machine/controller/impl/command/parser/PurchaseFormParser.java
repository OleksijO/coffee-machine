package coffee_machine.controller.impl.command.parser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by oleksij.onysymchuk@gmail on 29.11.2016.
 */
public interface PurchaseFormParser {
    Map<Integer,Integer> getDrinksQuantityByIdFromRequest(HttpServletRequest request);

    Map<Integer,Map<Integer,Integer>> getAddonsQuantityInDrinksByIdFromRequest(HttpServletRequest request);
}
