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
import java.lang.annotation.Target;

/**
 * 
 * Allows to specify {@link LogLevel} and exceptionTemplate to apply for a given group of exceptions.
 * 
 * @author Vitaliy S aka Yilativs <a href="mailto:vitaliy.se@gmail.com">
 * 
 */ 
@Target(ElementType.ANNOTATION_TYPE )
public @interface LogException {
	public static final String EXCEPTION_DEFAULT_TEMPLATE = "${exception}";
	
	/**
	 * 
	 * @return {@link LogLevel} to apply for a given group of exceptions
	 */
	LogLevel level() default LogLevel.ERROR;

	/**
	 * A group of exceptions.
	 * @return
	 */
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
