package net.sf.aspect4log.manual;

public class PaymentException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public PaymentException(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "SecurityException [" + getMessage() + "]";
	}

}
