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

import static net.sf.aspect4log.LogLevel.ERROR;
import static net.sf.aspect4log.LogLevel.WARN;

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
@Target({ ElementType.TYPE, ElementType.METHOD,ElementType.CONSTRUCTOR })
public @interface Log {
	public static final String ARGUMENTS_DEFAULT_TEMPLATE = "${args}";
	public static final String RESULT_DEFAULT_TEMPLATE = "${result}";
	

	LogLevel enterLevel() default LogLevel.DEBUG;

	/**
	 * 
	 * @return
	 * 
	 *         WARNING: making exitLevel than enterLevel can make the log hard to read
	 */
	LogLevel exitLevel() default LogLevel.DEBUG;

	/**
	 * defines how exit on exception is logged
	 * @return
	 */
	LogException[] logExceptions() default { @LogException(level = ERROR, exceptions = { RuntimeException.class }, printStackTrace = true), @LogException(level = WARN, exceptions = { Exception.class }, printStackTrace = false) };

	String argumentsTemplate() default ARGUMENTS_DEFAULT_TEMPLATE;

	/**
	 * 
	 * @return A pattern representing value returned from the method. The default value is $result.
	 */
	String resultTemplate() default RESULT_DEFAULT_TEMPLATE;

	/**
	 * 
	 * @return if specified will be set as MDC key.
	 */
	String mdcKey() default "";

	/**
	 * 
	 * @return An mdc patter. Can be any expression using $args bean
	 */
	String mdcTemplate() default "";

}
