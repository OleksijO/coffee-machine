package coffee_machine.controller.impl.command.request.data.extractor.impl;

import coffee_machine.controller.RegExp;
import coffee_machine.controller.exception.ControllerException;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.i18n.message.key.error.CommandErrorKey;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents common functionality for form extractors
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class ItemsBySimpleParameterExtractor {
    private final Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);

    /**
     * @param request request instance
     * @return  The map of pairs (drinkId, specifiedQuantity) with nonZero quantity.
     */
    Map<Integer, Integer> getItemQuantityByIdFromRequest(HttpServletRequest request,
                                                         Pattern itemParameterPattern) {
        Enumeration<String> params = request.getParameterNames();
        Map<Integer, Integer> itemQuantityByIds = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = itemParameterPattern.matcher(param);
            if (matcher.matches()) {
                /*parameter matches specified pattern and we can process it*/
                int itemQuantity = getIntFromRequestByParameter(param, request);
                if (itemQuantity > 0) {
                    int itemId = getItemIdFromParam(param);
                    itemQuantityByIds.put(itemId, itemQuantity);
                } else if (itemQuantity < 0) {
                    /* validation error */
                    throw new ControllerException(CommandErrorKey.QUANTITY_SHOULD_BE_NON_NEGATIVE);
                }
            }
        }
        return itemQuantityByIds;
    }

    private int getIntFromRequestByParameter(String param, HttpServletRequest request) {
        try {

            return Integer.parseInt(request.getParameter(param));

        } catch (Exception e) {
            throw new ControllerException(CommandErrorKey.QUANTITY_SHOULD_BE_INT);
        }

    }

    int getItemIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);
        if (matcher.find(0)) {

            return Integer.parseInt(param.substring(matcher.start(), matcher.end()));

        } else {
            throw new ControllerException(GeneralKey.ERROR_UNKNOWN); //this normally should not ever happen
        }
    }
}
