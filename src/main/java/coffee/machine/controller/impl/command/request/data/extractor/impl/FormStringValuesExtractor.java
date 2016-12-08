package coffee.machine.controller.impl.command.request.data.extractor.impl;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oleksij.onysymchuk@gmail on 08.12.2016.
 */
public class FormStringValuesExtractor {

    Map<String, String> getFormValuesFromRequestByPattern(HttpServletRequest request, Pattern pattern) {
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(request);
        Enumeration<String> params = request.getParameterNames();
        Map<String, String> formValuesByParam = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = pattern.matcher(param);
            if (pattern.matcher(param).matches()) {
                // parameter matches specified pattern and we can save it
                formValuesByParam.put(param, request.getParameter(param));
            }
        }
        return formValuesByParam;
    }





}
