package coffee.machine.controller.impl.command.request.data.extractor.impl;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.impl.command.request.data.extractor.ItemsStringFormDataExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class represents implementation of string item values parameter with their names  extractor
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ItemsStringFormDataExtractorImpl implements ItemsStringFormDataExtractor {
    private final Pattern PATTERN_ITEM = Pattern.compile(RegExp.REQEXP_ANY_ITEM);
    private FormStringValuesExtractor generalExtractor = new FormStringValuesExtractor();

    public Map<String, String> getAllItemParameterValuesFromRequest(HttpServletRequest request){
        return generalExtractor.getFormValuesFromRequestByPattern(request, PATTERN_ITEM);
    }


}
