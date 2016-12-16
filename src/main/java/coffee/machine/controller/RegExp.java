package coffee.machine.controller;

import coffee.machine.view.Parameters;

import java.util.ResourceBundle;

/**
 * This class is a constant holder for regexp, used in request processing.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RegExp {
    private static ResourceBundle regexp = ResourceBundle.getBundle("controller.regexp");
    public static String REGEXP_EMAIL = regexp.getString("email");
    public static String REGEXP_PASSWORD = regexp.getString("password");
    public static String REGEXP_NUMBER = regexp.getString("number");
    public static String REGEXP_FULL_NAME = regexp.getString("full.name");

    public static String REGEXP_DRINK_PARAM = "^" + Parameters.DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    public static String REGEXP_ADDON_PARAM = "^" + Parameters.ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    public static String REGEXP_ADDON_IN_DRINK_PARAM = "^" + Parameters.DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER
            + Parameters.ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    public static String REGEXP_ANY_ITEM = "^("+
            Parameters.DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")|("+
            Parameters.ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")|("+
            Parameters.DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER +
            Parameters.ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")$";



}
