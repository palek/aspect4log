package net.sf.aspect4log;

public @interface ExceptionExit {
	public static final String EXCEPTION_DEFAULT_TEMPLATE = "${exception}";
	
	LogLevel level() default LogLevel.ERROR;

	Class<? extends Throwable>[] exceptions() default { Exception.class };

	/**
	 * @return true if printing stack trace is needed
	 */
	boolean printStackTrace() default true;

	/**
	 * default value is $exception
	 * @return $exception
	 */
	String exceptionTemplate() default EXCEPTION_DEFAULT_TEMPLATE;
}
