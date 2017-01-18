package coffee.machine.controller.command.helper;

import coffee.machine.controller.RegExp;
import coffee.machine.controller.exception.ControllerException;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_UNKNOWN;

/**
 * This class represents functionality for extract data from request
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RequestDataExtractor {

    private static final String LOG_MESSAGE_CANNOT_FIND_FIRST_ID_IN_PARAM = "Cannot find first id in param ";
    private static final String LOG_MESSAGE_CANNOT_FIND_SECOND_ID_IN_PARAM = "Cannot find second id in param ";

    private static final String LOG_MESSAGE_PROBLEMS_WITH_PARSING_INT_FROM_PARAMETER_FORMAT =
            "Problems with parsing INT from parameter '%s', its value ='%s'";
    private static final String LOG_MESSAGE_PROBLEMS_WITH_PARSING_DOUBLE_FROM_PARAMETER_FORMAT =
            "Problems with parsing DOUBLE from parameter '%s', its value ='%s'";

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
     * @param param   Request parameter name
     * @return Parsed value of specified parameter name
     */
    public int getIntFromRequestByParameter(HttpServletRequest request, String param, String ErrorMessageKey) {
        try {

            return Integer.parseInt(request.getParameter(param));

        } catch (NumberFormatException e) {
            throw new ControllerException()
                    .addMessageKey(ErrorMessageKey)
                    .addLogMessage(String.format(LOG_MESSAGE_PROBLEMS_WITH_PARSING_INT_FROM_PARAMETER_FORMAT,
                            param, request.getParameter(param)));
        }
    }

    /**
     * @param request Request instance
     * @param param   Request parameter name
     * @return Parsed value of specified parameter name
     */
    public double getDoubleFromRequestByParameter(HttpServletRequest request, String param, String ErrorMessageKey) {
        try {

            return Double.parseDouble(request.getParameter(param));

        } catch (NumberFormatException e) {
            throw new ControllerException()
                    .addMessageKey(ErrorMessageKey)
                    .addLogMessage(String.format(LOG_MESSAGE_PROBLEMS_WITH_PARSING_DOUBLE_FROM_PARAMETER_FORMAT,
                            param, request.getParameter(param)));
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
            throw new ControllerException()
                    .addMessageKey(ERROR_UNKNOWN)
                    .addLogMessage(LOG_MESSAGE_CANNOT_FIND_FIRST_ID_IN_PARAM + param);
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
            throw new ControllerException()
                    .addMessageKey(ERROR_UNKNOWN)
                    .addLogMessage(LOG_MESSAGE_CANNOT_FIND_SECOND_ID_IN_PARAM + param);
        }
    }
}
