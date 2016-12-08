package coffee.machine.controller;

import coffee.machine.view.Parameters;

import java.util.ResourceBundle;

/**
 * This class is a constant holder for regexp, used in request processing.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface RegExp {
    ResourceBundle regexp = ResourceBundle.getBundle("controller.regexp");
    String REGEXP_EMAIL = regexp.getString("email");
    String REGEXP_PASSWORD = regexp.getString("password");
    String REGEXP_NUMBER = regexp.getString("number");
    String REGEXP_FULL_NAME = regexp.getString("fullName");

    String REGEXP_DRINK_PARAM = "^" + Parameters.DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    String REGEXP_ADDON_PARAM = "^" + Parameters.ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    String REGEXP_ADDON_IN_DRINK_PARAM = "^" + Parameters.DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER
            + Parameters.ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    String REQEXP_ANY_ITEM = "^("+
            Parameters.DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")|("+
            Parameters.ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")|("+
            Parameters.DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER +
            Parameters.ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")$";



}
