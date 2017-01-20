package coffee.machine.view;

/**
 * This class is a constant holder for exception handler message format of log record on jsp pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */

public class ErrorMessage {
    public static final String JSP_HANDLER_MESSAGE_FORMAT = "%s id=%s. Status code = %d. Servlet name ='%s'.\n %s";

    private ErrorMessage() {
    }
}
