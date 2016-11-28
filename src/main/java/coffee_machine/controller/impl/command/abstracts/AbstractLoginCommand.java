package coffee_machine.controller.impl.command.abstracts;

import coffee_machine.controller.RegExp;
import org.apache.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Created by oleksij.onysymchuk@gmail on 27.11.2016.
 */
public class AbstractLoginCommand extends AbstractCommand {
    private static final Pattern PATTERN_EMAIL = Pattern.compile(RegExp.REGEXP_EMAIL);
    private static final Pattern PATTERN_PASSWORD = Pattern.compile(RegExp.REGEXP_PASSWORD);


    protected static final String TRY_FAILED_WRONG_EMAIL_OR_PASSWORD =
            "LOGIN TRY FAILED: no such combination of email and password.";

    protected static final String TRY_FAILED_WRONG_EMAIL = " LOGIN TRY FAILED: wrong email: ";
    protected static final String TRY_FAILED_WRONG_PASSWORD = " LOGIN TRY FAILED: password do not matches pattern.";
    protected static final String USER_LOGGED_IN = "USER id=%d LOGGED IN.";
    protected static final String ADMIN_LOGGED_IN = "ADMIN id=%d LOGGED IN.";

    public AbstractLoginCommand(Logger logger) {
        super(logger);
    }

    protected boolean checkPassword(String password) {

        return checkToPattern(PATTERN_PASSWORD, password);
    }

    protected boolean checkLogin(String email) {
        return checkToPattern(PATTERN_EMAIL, email);
    }

    private boolean checkToPattern(Pattern pattern, String stringToCheck){
        return (stringToCheck != null) && (pattern.matcher(stringToCheck).matches());
    }



}
