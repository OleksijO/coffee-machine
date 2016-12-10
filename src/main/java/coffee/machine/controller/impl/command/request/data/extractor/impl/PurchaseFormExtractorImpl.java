package coffee.machine.controller.impl.command.request.data.extractor.impl;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.impl.command.request.data.extractor.PurchaseFormDataExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents implementation of parameter values from purchase drinks form extractor
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class PurchaseFormExtractorImpl implements PurchaseFormDataExtractor {
    private Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);
    private Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private Pattern patternAddonInDrink = Pattern.compile(RegExp.REGEXP_ADDON_IN_DRINK_PARAM);


    private ItemsBySimpleParameterExtractor simpleParameterExtractor = new ItemsBySimpleParameterExtractor();

    @Override
    public Map<Integer, Integer> getDrinksQuantityByIdFromRequest(HttpServletRequest request) {
        return simpleParameterExtractor.getItemQuantityByIdFromRequest(request, patternDrink);
    }

    @Override
    public Map<Integer, Map<Integer, Integer>> getAddonsQuantityInDrinksByIdFromRequest(HttpServletRequest request) {
        Map<Integer, Map<Integer, Integer>> addonQuantityInDrinksById = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = patternAddonInDrink.matcher(param);

            if (matcher.matches()) {
                int addonId = simpleParameterExtractor.getSecondIdFromParam(param);
                int addonQuantity = simpleParameterExtractor.getIntFromRequestByParameter(param, request);

                if (addonQuantity > 0) {
                    int drinkId = simpleParameterExtractor.getItemIdFromParam(param);

                    addonQuantityInDrinksById.putIfAbsent(drinkId, new HashMap<>());
                    addonQuantityInDrinksById
                            .get(drinkId)
                            .put(addonId, addonQuantity);
                }
            }
        }

        return addonQuantityInDrinksById;
    }

}
