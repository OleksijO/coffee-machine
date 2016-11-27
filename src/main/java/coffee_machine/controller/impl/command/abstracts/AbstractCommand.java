package coffee_machine.controller.impl.command.abstracts;

import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.SupportedLocale;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 * Created by oleksij.onysymchuk@gmail on 27.11.2016.
 */
public class AbstractCommand {
    static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n.messages",
            SupportedLocale.EN.getLocale());
    private Logger logger;

    public AbstractCommand(Logger logger) {
        this.logger = logger;
    }

    protected void logApplicationError(ApplicationException e) {
        logger.error(RESOURCE_BUNDLE.getString(e.getMessage()) + " : " + e.getAdditionalMessage(), e);
    }

    protected void logError(Exception e) {
        logger.error(e.getMessage(), e);
    }
}
