package coffee_machine.controller;

import java.util.ResourceBundle;

import static coffee_machine.view.Parameters.ADDON_PARAMETER_STARTS_WITH;
import static coffee_machine.view.Parameters.DRINK_PARAMETER_STARTS_WITH;

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

    String REGEXP_DRINK_PARAM = "^" + DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    String REGEXP_ADDON_PARAM = "^" + ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";
    String REGEXP_ADDON_IN_DRINK_PARAM = "^" + DRINK_PARAMETER_STARTS_WITH + REGEXP_NUMBER
            + ADDON_PARAMETER_STARTS_WITH + REGEXP_NUMBER + "$";


}
