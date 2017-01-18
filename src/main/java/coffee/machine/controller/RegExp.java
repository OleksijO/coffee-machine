package coffee.machine.controller;

import java.util.ResourceBundle;

import static coffee.machine.view.Parameters.ADDON_PARAMETER_STARTS_WITH;
import static coffee.machine.view.Parameters.DRINK_PARAMETER_STARTS_WITH;

/**
 * This class is a constant holder for regexp, used in request processing.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RegExp {
    private static ResourceBundle regexpBundle = ResourceBundle.getBundle("controller.regexp");
    public final static String REGEXP_NUMBER = regexpBundle.getString("number");
    public final static String REGEXP_EMAIL = regexpBundle.getString("email");
    public final static String REGEXP_PASSWORD = regexpBundle.getString("password");
    public final static String REGEXP_FULL_NAME = regexpBundle.getString("full.name");

    public final static String REGEXP_DRINK_PARAM = "^" +
            DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    public final static String REGEXP_ADDON_PARAM = "^" +
            ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    public final static String REGEXP_ADDON_IN_DRINK_PARAM = "^" +
            DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER +
            ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    public final static String REGEXP_ANY_PRODUCT = "^("+
            DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")|("+
            ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")|("+
            DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER +
            ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + ")$";



}
