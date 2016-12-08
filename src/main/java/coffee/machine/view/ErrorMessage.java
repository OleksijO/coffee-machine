package coffee.machine.view;

/**
 * This class is a constant holder for exception handler message format of log record on jsp pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */

public interface ErrorMessage {
    String JSP_HANDLER_MESSAGE_FORMAT = "User id=%d, admin id=%d. Statuc code = %d. Servlet name ='%s'.\n %s";
}
