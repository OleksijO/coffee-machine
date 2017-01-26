package coffee.machine.exception;

/**
 * This class defines specific application object exception object.
 * Instances of this class carries in message bundle key of message for user.
 * Also they could carry additional message to be shown on view.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ApplicationException extends RuntimeException {

    /**
     * Error text message bundle key
     */
    private String messageKey;

    /**
     * Additional usual text message (NON resource bundle key)
     */
    private String additionalMessage;

    /**
     * Message to be logged
     */
    private String logMessage;

    protected ApplicationException() {
    }

    /**
     * @param messageKey resource bundle message key
     * @param cause   cause exception instance
     */
    protected ApplicationException(String messageKey, Throwable cause) {
        super(cause);
        this.messageKey = messageKey;
    }

    /**
     * @param messageKey resource bundle message key
     */
    protected ApplicationException(String messageKey) {
        this.messageKey = messageKey;
    }

    protected ApplicationException addLogMessage(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    protected ApplicationException addMessageKey(String messageKey) {
        this.messageKey = messageKey;
        return this;
    }

    protected ApplicationException addAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
        return this;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public String getMessageKey() {
        return messageKey;
    }

}
