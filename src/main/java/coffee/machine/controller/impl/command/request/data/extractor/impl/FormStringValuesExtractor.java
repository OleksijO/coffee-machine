package coffee.machine.controller.impl.command.request.data.extractor.impl;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * This class represents functionality for for form extractor
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class FormStringValuesExtractor {

    /**
     * @param request request instance
     * @param pattern pattern for parameters to be extracted from request
     * @return Map of pairs [parameterName = parameter's string value]
     */
    Map<String, String> getFormValuesFromRequestByPattern(HttpServletRequest request, Pattern pattern) {
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(request);
        Enumeration<String> params = request.getParameterNames();
        Map<String, String> formValuesByParam = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (pattern.matcher(param).matches()) {
                formValuesByParam.put(param, request.getParameter(param));
            }
        }
        return formValuesByParam;
    }





}
