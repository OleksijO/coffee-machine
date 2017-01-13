package coffee.machine.controller.impl.command.request.data.extractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by User on 21.12.2016.
 */
public interface PurchaseOwnDrinkFormExtractor {

    int getBaseDrinkIdFromRequest(HttpServletRequest request);

    Map<Integer,Integer> getAddonsQuantityByIdFromRequest(HttpServletRequest request);
}
