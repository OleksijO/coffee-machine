package coffee.machine.controller.impl.command.request.data.extractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This class represents string values parameter with their names  extractor
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface ItemsStringFormDataExtractor {
    /**
     * @param request request instance
     * @return  The map of pairs (param name, param value)
     */
    Map<String, String> getAllItemParameterValuesFromRequest(HttpServletRequest request);


}
