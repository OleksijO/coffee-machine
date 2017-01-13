package coffee.machine.controller.impl.command.request.data.extractor.impl;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.impl.command.request.data.extractor.PurchaseOwnDrinkFormExtractor;
import coffee.machine.view.Parameters;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class represents implementation of parameter values from refill form extractor
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class PurchaseOwnDrinkFormExtractorImpl implements PurchaseOwnDrinkFormExtractor {
    private final Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private final Pattern patternAddon = Pattern.compile(RegExp.REGEXP_ADDON_PARAM);

    private ItemsBySimpleParameterExtractor simpleParameterExtractor = new ItemsBySimpleParameterExtractor();


    @Override
    public int getBaseDrinkIdFromRequest(HttpServletRequest request){
        return simpleParameterExtractor.getIntFromRequestByParameter(Parameters.DRINK_PARAMETER_STARTS_WITH, request);
    }

    @Override
    public Map<Integer,Integer> getAddonsQuantityByIdFromRequest(HttpServletRequest request) {
        return simpleParameterExtractor.getItemQuantityByIdFromRequest(request, patternAddon);
    }





}
