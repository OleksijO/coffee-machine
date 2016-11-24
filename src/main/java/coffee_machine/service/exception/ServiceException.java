package coffee_machine.service.exception;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String additionalMessage;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}
	public ServiceException(String message, String additionalMessage) {
		super(message);
		this.additionalMessage = additionalMessage;
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public String getAdditionalMessage() {
		return additionalMessage;
	}

	public void setAdditionalMessage(String additionalMessage) {
		this.additionalMessage = additionalMessage;
	}
}
