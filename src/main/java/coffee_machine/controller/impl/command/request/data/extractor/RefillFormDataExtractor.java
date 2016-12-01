package coffee_machine.controller.impl.command.request.data.extractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by oleksij.onysymchuk@gmail on 29.11.2016.
 */
public interface RefillFormDataExtractor {
    Map<Integer,Integer> getDrinksQuantityByIdFromRequest(HttpServletRequest request);

    Map<Integer,Integer> getAddonsQuantityByIdFromRequest(HttpServletRequest request);
}
