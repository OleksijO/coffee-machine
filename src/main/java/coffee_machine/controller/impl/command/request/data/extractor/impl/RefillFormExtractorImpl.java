package coffee_machine.controller.impl.command.request.data.extractor.impl;

import coffee_machine.controller.RegExp;
import coffee_machine.controller.impl.command.request.data.extractor.RefillFormDataExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by oleksij.onysymchuk@gmail on 29.11.2016.
 */
public class RefillFormExtractorImpl implements RefillFormDataExtractor {
    private final Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private final Pattern patternAddon = Pattern.compile(RegExp.REGEXP_ADDON_PARAM);

    private ItemsBySimpleParameterExtractor simpleParameterParser = new ItemsBySimpleParameterExtractor();

    @Override
    public Map<Integer,Integer> getDrinksQuantityByIdFromRequest(HttpServletRequest request) {
        return simpleParameterParser.getGoodsQuantityByIdFromRequest(request, patternDrink);
    }

    @Override
    public Map<Integer,Integer> getAddonsQuantityByIdFromRequest(HttpServletRequest request) {
        return simpleParameterParser.getGoodsQuantityByIdFromRequest(request, patternAddon);
    }





}
