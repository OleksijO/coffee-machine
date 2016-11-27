package coffee_machine.controller;

import static coffee_machine.view.Parameters.ADDON_PARAMETER_STARTS_WITH;
import static coffee_machine.view.Parameters.DRINK_PARAMETER_STARTS_WITH;

public interface RegExp {
    String REGEXP_EMAIL = "^([a-z0-9_-]+\\.)*[a-z0-9_\\-]+@[a-z0-9_-]+(\\.[a-z0-9_\\-]+)*\\.[a-z]{2,6}$";
    String REGEXP_PASSWORD = "^[A-Za-z0-9_\\-]{4,12}$";

    String REGEXP_NUMBER = "\\d+";
    String REGEXP_DRINK_PARAM = "^" + DRINK_PARAMETER_STARTS_WITH + "\\d+$";
    String REGEXP_ADDON_PARAM = "^" + ADDON_PARAMETER_STARTS_WITH + "\\d+$";
    String REGEXP_ADDON_IN_DRINK_PARAM = "^"
            + DRINK_PARAMETER_STARTS_WITH + "\\d+"
            + ADDON_PARAMETER_STARTS_WITH + "\\d+$";



}
