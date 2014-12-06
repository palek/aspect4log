/*  This file is part of the aspect4log  project.
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License
 as published by the Free Software Foundation; version 2.1
 of the License.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.

 Copyright 2007-2014 Semochkin Vitaliy Evgenevich aka Yilativs
  
 */

package net.sf.aspect4log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Defines the logging rules on method or on the entire class.
 * 
 * @author Vitaliy S <a href="mailto:vitaliy.se@gmail.com">
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface Log {

	public enum Level {
		NONE, TRACE, DEBUG, INFO, WARN, ERROR
	}

	/**
	 * Allows to specify {@link Level} and exceptionTemplate to apply for a given group of exceptions.
	 */
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface Exceptions {
		public static final String EXCEPTION_DEFAULT_TEMPLATE = "${exception}";

		/**
		 * 
		 * @return {@link Level} to apply for a given group of exceptions
		 */
		Level level() default Level.ERROR;

		/**
		 * A group of exceptions.
		 * 
		 * @return
		 */
		Class<? extends Throwable>[] exceptions() default { Exception.class };

		/**
		 * @return true if printing stack trace is needed
		 */
		boolean stackTrace() default true;

		/**
		 * default value is $exception
		 * 
		 * @return $exception
		 */
		String template() default EXCEPTION_DEFAULT_TEMPLATE;
	}

	public static final String ARGUMENTS_DEFAULT_TEMPLATE = "${args}";
	public static final String RESULT_DEFAULT_TEMPLATE = "${result}";

	/**
	 * @return {@link Level} to use on method enter
	 */
	Level enterLevel() default Level.DEBUG;

	/**
	 * 
	 * @return {@link Level} to use on successful method exit
	 * 
	 *         WARNING: making exitLevel smaller than enterLevel can make the log hard to read
	 */
	Level exitLevel() default Level.DEBUG;

	/**
	 * @return an array of {@link Exceptions} that defines how exit on different exceptions is logged. For each exception it is possible to specify by default all runtime exceptions print a stack trace, all checked exception do not print a stack trace.
	 */
	Exceptions[] on() default { @Exceptions(level = Level.ERROR, exceptions = { RuntimeException.class }, stackTrace = true),
			@Exceptions(level = Level.WARN, exceptions = { Exception.class }, stackTrace = false) };

	String argumentsTemplate() default ARGUMENTS_DEFAULT_TEMPLATE;

	/**
	 * 
	 * @return A pattern representing value returned from the method. The default value is $result.
	 */
	String resultTemplate() default RESULT_DEFAULT_TEMPLATE;

	/**
	 * @return true if indent is needed, returns false otherwise.
	 * 
	 *         NOTE: You may consider not to use indent in case the method level in stack trace will be too high, or in case you log recursive method. 
	 */
	boolean indent() default true;

	/**
	 * 
	 * @return if specified will be set as MDC key.
	 * 
	 * 		NOTE: The typical use case for mdc key is identification method (e.g. in a filter). The typical key will be user's login.
	 * 
	 */
	String mdcKey() default "";

	/**
	 * 
	 * @return An mdc patter. Can be any expression using $args bean
	 */
	String mdcTemplate() default "";

}
