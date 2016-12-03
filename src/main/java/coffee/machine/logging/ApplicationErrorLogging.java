package coffee.machine.logging;

import coffee.machine.i18n.SupportedLocale;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 * This interface represents general utility methods for logging errors with
 * user error messages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface ApplicationErrorLogging {
    String MESSAGE_SEPARATOR = " : ";
    ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n.messages",
            SupportedLocale.EN.getLocale());

    /**
     * @param logger logger instance of class
     * @param messageKey text message resource bundle key
     * @param additionalMessage additional text message
     * @param e throwable instance
     */
    default void logApplicationError(Logger logger,
                                     String messageKey,
                                     String additionalMessage,
                                     Exception e) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey) + MESSAGE_SEPARATOR
                + ((additionalMessage == null) ? "" : additionalMessage), e);
    }

    /**
     * @param logger logger instance of class
     * @param messageKey text message resource bundle key
     * @param additionalMessage additional text message
     */
    default void logApplicationError(Logger logger,
                                     String messageKey,
                                     String additionalMessage) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey) + MESSAGE_SEPARATOR
                + ((additionalMessage == null) ? "" : additionalMessage));
    }

    /**
     * @param logger logger instance of class
     * @param messageKey text message resource bundle key
     */
    default void logApplicationError(Logger logger,
                                     String messageKey) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey));
    }

}
