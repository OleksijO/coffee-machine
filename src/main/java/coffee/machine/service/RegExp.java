package coffee.machine.service;

import java.util.ResourceBundle;

/**
 * This class is a constant holder for regexp, used in service processing.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RegExp {
    private final static ResourceBundle regexp = ResourceBundle.getBundle("service.regexp");
    public final static String REGEXP_EMAIL = regexp.getString("email");
    public final static String REGEXP_PASSWORD = regexp.getString("password");
    public final static String REGEXP_FULL_NAME = regexp.getString("full.name");
}
