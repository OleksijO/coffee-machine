package coffee_machine.controller.impl.command.request.data.extractor.impl;

import coffee_machine.controller.RegExp;
import coffee_machine.controller.exception.ControllerException;
import coffee_machine.controller.impl.command.request.data.extractor.PurchaseFormDataExtractor;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.i18n.message.key.error.CommandErrorKey;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oleksij.onysymchuk@gmail on 29.11.2016.
 */
public class PurchaseFormExtractorImpl implements PurchaseFormDataExtractor {
    private Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);
    private Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private Pattern patternAddonInDrink = Pattern.compile(RegExp.REGEXP_ADDON_IN_DRINK_PARAM);


    private ItemsBySimpleParameterExtractor simpleParameterExtractor = new ItemsBySimpleParameterExtractor();

    @Override
    public Map<Integer, Integer> getDrinksQuantityByIdFromRequest(HttpServletRequest request) {
        return simpleParameterExtractor.getGoodsQuantityByIdFromRequest(request, patternDrink);
    }

    @Override
    public Map<Integer, Map<Integer, Integer>> getAddonsQuantityInDrinksByIdFromRequest(HttpServletRequest request) {
        Map<Integer, Map<Integer, Integer>> addonQuantityInDrinksById = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = patternAddonInDrink.matcher(param);
            if (matcher.matches()) {
                int addonId = getAddonIdFromParam(param);
                int addonQuantity = getIntFromRequestByParameter(param, request);
                if (addonQuantity > 0) {
                    int drinkId = getDrinkIdFromParam(param);
                    addonQuantityInDrinksById.putIfAbsent(drinkId, new HashMap<>());
                    addonQuantityInDrinksById
                            .get(drinkId)
                            .put(addonId, addonQuantity);
                }
            }
        }
        return addonQuantityInDrinksById;
    }


    private int getIntFromRequestByParameter(String param, HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter(param));
        } catch (Exception e) {
            throw new ControllerException(CommandErrorKey.QUANTITY_SHOULD_BE_INT);
        }

    }

    private int getDrinkIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);
        if (matcher.find(0)) {
            return Integer.parseInt(param.substring(matcher.start(), matcher.end()));
        }
        throw new ControllerException(GeneralKey.ERROR_UNKNOWN);
    }

    private int getAddonIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);
        if (matcher.find(0)) {    // passing by drink id
            if (matcher.find(matcher.end())) {
                return Integer.parseInt(param.substring(matcher.start(), matcher.end()));
            }
        }
        throw new ControllerException(GeneralKey.ERROR_UNKNOWN);
    }

}
