package coffee.machine.controller.command.helper;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.exception.ControllerException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coffee.machine.i18n.message.key.GeneralKey.ERROR_UNKNOWN;
import static coffee.machine.i18n.message.key.error.CommandErrorKey.QUANTITY_SHOULD_BE_INT;

/**
 * This class represents functionality for extract data from request
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RequestDataExtractor {
    private final static Logger logger = Logger.getLogger(RequestDataExtractor.class);

    private final static String PROBLEMS_WITH_PARSING_INT_FROM_PARAMETER_FORMAT =
            "Problems with parsing INT from parameter '%s', its value ='%s'";

    private final Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);

    /**
     * @param request request instance
     * @param pattern pattern for parameters to be extracted from request
     * @return Map of pairs [parameterName = parameter's string value]
     */
    public Map<String, String> getParametersFromRequestByPattern(HttpServletRequest request, Pattern pattern) {
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(request);
        Enumeration<String> params = request.getParameterNames();
        Map<String, String> parametersMap = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (pattern.matcher(param).matches()) {
                parametersMap.put(param, request.getParameter(param));
            }
        }
        return parametersMap;
    }

    /**
     * @param request Request instance
     * @param param Request parameter name
     * @return Int value of specified parameter name
     */
    public int getIntFromRequestByParameter(HttpServletRequest request, String param) {
        try {

            return Integer.parseInt(request.getParameter(param));

        } catch (Exception e) {
            logger.error(String.format(PROBLEMS_WITH_PARSING_INT_FROM_PARAMETER_FORMAT,
                    param, request.getParameter(param)));
            throw new ControllerException(QUANTITY_SHOULD_BE_INT);
        }
    }

    /**
     * @param param Request parameter name
     * @return First number from parameter name
     */
    public int getFirstNumberFromParameterName(String param) {
        Matcher matcher = patternNumber.matcher(param);
        if (matcher.find(0)) {

            return Integer.parseInt(param.substring(matcher.start(), matcher.end()));

        } else {
            throw new ControllerException(ERROR_UNKNOWN); //this normally should not ever happen
        }
    }

    /**
     * @param param Request parameter name
     * @return Second number from parameter name
     */
    public int getSecondNumberFromParameterName(String param) {
        Matcher matcher = patternNumber.matcher(param);
        if (matcher.find(0) && matcher.find(matcher.end())) {

            return Integer.parseInt(param.substring(matcher.start(), matcher.end()));

        } else {
            throw new ControllerException(ERROR_UNKNOWN); //this normally should not ever happen
        }
    }

}
