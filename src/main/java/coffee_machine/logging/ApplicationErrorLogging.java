package coffee_machine.logging;

import coffee_machine.i18n.SupportedLocale;
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

    default void logApplicationError(Logger logger,
                                     String messageKey,
                                     String additionalMessage,
                                     Exception e) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey) + MESSAGE_SEPARATOR
                + ((additionalMessage == null) ? "" : additionalMessage), e);
    }

    default void logApplicationError(Logger logger,
                                     String messageKey,
                                     String additionalMessage) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey) + MESSAGE_SEPARATOR
                + ((additionalMessage == null) ? "" : additionalMessage));
    }

    default void logApplicationError(Logger logger,
                                     String messageKey) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey));
    }

}
