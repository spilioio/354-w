package tests;

@SuppressWarnings("serial")
public class CircularityException extends Exception {
	public CircularityException() {
	}
	
	public CircularityException(String message) {
		super(message);
	}
	
	public CircularityException(Throwable cause) {
		super(cause);
	}
	
	public CircularityException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CircularityException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
