package net.sf.aspect4log.manual;

public class BusinessException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public BusinessException(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "SecurityException [" + getMessage() + "]";
	}

}
