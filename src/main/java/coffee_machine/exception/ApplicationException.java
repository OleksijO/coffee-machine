package coffee_machine.exception;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String additionalMessage;

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, String additionalMessage) {
        super(message);
        this.additionalMessage = additionalMessage;
    }

    public ApplicationException(String message, String additionalMessage, Throwable cause) {
        super(message, cause);
        this.additionalMessage = additionalMessage;
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }
}
