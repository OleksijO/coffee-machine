package coffee.machine.controller.command.request.data.extractor.impl;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.exception.ControllerException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coffee.machine.i18n.message.key.GeneralKey.ERROR_UNKNOWN;
import static coffee.machine.i18n.message.key.error.CommandErrorKey.QUANTITY_SHOULD_BE_INT;
import static coffee.machine.i18n.message.key.error.CommandErrorKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;

/**
 * This class represents common functionality for form extractors
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class ItemsBySimpleParameterExtractor {
    private static final Logger logger = Logger.getLogger(ItemsBySimpleParameterExtractor.class);
    private static final String QUANTITY_UNDER_ZERO_IN_PARAM = "Quantity under zero in param ";
    private static final String PROBLEMS_WITH_PARSING_INT_FROM_PARAMETER_FORMAT = "Problems with parsing INT from parameter '%s', its value ='%s'";
    private final Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);


    /**
     * @param request request instance
     * @return The map of pairs (drinkId, specifiedQuantity) with nonZero quantity.
     */
    Map<Integer, Integer> getItemQuantityByIdFromRequest(HttpServletRequest request,
                                                         Pattern itemParameterPattern) {
        Enumeration<String> params = request.getParameterNames();
        Map<Integer, Integer> itemQuantityByIds = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = itemParameterPattern.matcher(param);
            if (matcher.matches()) {
                int itemQuantity = getIntFromRequestByParameter(param, request);
                if (itemQuantity > 0) {
                    int itemId = getItemIdFromParam(param);
                    itemQuantityByIds.put(itemId, itemQuantity);
                } else if (itemQuantity < 0) {
                    logger.error(QUANTITY_UNDER_ZERO_IN_PARAM + param);
                    throw new ControllerException(QUANTITY_SHOULD_BE_NON_NEGATIVE);
                }
            }
        }
        return itemQuantityByIds;
    }

    int getIntFromRequestByParameter(String param, HttpServletRequest request) {
        try {

            return Integer.parseInt(request.getParameter(param));

        } catch (Exception e) {
            logger.error(String.format(PROBLEMS_WITH_PARSING_INT_FROM_PARAMETER_FORMAT,
                    param, request.getParameter(param)));
            throw new ControllerException(QUANTITY_SHOULD_BE_INT);
        }

    }

    int getItemIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);
        if (matcher.find(0)) {

            return Integer.parseInt(param.substring(matcher.start(), matcher.end()));

        } else {
            throw new ControllerException(ERROR_UNKNOWN); //this normally should not ever happen
        }
    }

    int getSecondIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);

        if (matcher.find(0)) {    // passing by first id
            if (matcher.find(matcher.end())) {

                return Integer.parseInt(param.substring(matcher.start(), matcher.end()));
            }
        }
        throw new ControllerException(ERROR_UNKNOWN);    // this should not happen
    }                                                               // in case of normal in-page operation

}
