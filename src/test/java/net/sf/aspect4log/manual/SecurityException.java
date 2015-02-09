package net.sf.aspect4log.manual;

public class SecurityException extends Exception{

	private static final long serialVersionUID = 1L;

	public SecurityException(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "SecurityException [" + getMessage() + "]";
	}
	
	
	

}
