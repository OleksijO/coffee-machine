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
     * Additional usual text message (NON resource bundle key)
     */
    private String additionalMessage;

    /**
     * Message to be logged
     */
    private String logMessage;

    /**
     * @param message resource bundle message key
     * @param cause   cause exception instance
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message resource bundle message key
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * @param message           resource bundle message key
     * @param additionalMessage usual text message to be shown on view
     */
    public ApplicationException(String message, String additionalMessage) {
        super(message);
        this.additionalMessage = additionalMessage;
    }

    /**
     * @param message           resource bundle message key
     * @param additionalMessage usual text message to be shown on view
     * @param cause             cause exception instance
     */
    public ApplicationException(String message, String additionalMessage, Throwable cause) {
        super(message, cause);
        this.additionalMessage = additionalMessage;
    }

    /**
     * @param cause cause exception instance
     */
    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException addLogMessage(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }
}
