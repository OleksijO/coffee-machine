package coffee_machine.logging;

import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.SupportedLocale;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 * Created by oleksij.onysymchuk@gmail on 28.11.2016.
 */
public interface ApplicationErrorLogging {
    ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n.messages",
            SupportedLocale.EN.getLocale());


    default void logError(Logger logger, Exception e) {
        logger.error(e);
    }

    default void logApplicationError(Logger logger,
                                     String messageKey,
                                     Exception e) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey), e);
    }

    default void logApplicationError(Logger logger,
                                     String messageKey,
                                     String additionalMessage,
                                     Exception e) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey) + additionalMessage, e);
    }

    default void logApplicationError(Logger logger,
                                     String messageKey,
                                     String additionalMessage) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey) + additionalMessage);
    }

    default void logApplicationError(Logger logger,
                                     String messageKey) {
        logger.error(RESOURCE_BUNDLE.getString(messageKey));
    }

    default void logApplicationError(Logger logger,
                                     ApplicationException e) {
        logger.error(RESOURCE_BUNDLE.getString(e.getMessage()), e);
    }


}
