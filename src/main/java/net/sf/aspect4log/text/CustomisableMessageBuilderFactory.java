/*  This file is part of the aspect  project.
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
package net.sf.aspect4log.text;

import net.sf.aspect4log.Log;

public class CustomisableMessageBuilderFactory implements MessageBuilderFactory {
	public static final String METHOD_ENTER_SYMBOL = "↓";
	public static final String METHOD_SUCCESSFUL_RETURN_SYMBOL = "↑";
	public static final String METHOD_EXCEPTION_RETURN_SYMBOL = "⇑";
	public static final String RETURN_VALUE_SYMBOL = "→";
	public static final String EXCEPTION_VALUE_SYMBOL = " ⇒ ";

	public static final String ELEMENTS_DELITMETER = ", ";
	public static final String MAP_KEY_VALUE_DELIMETER = ":";
	public static final String ARRAY_BEGINS_BRACKET = "{";
	public static final String ARRAY_ENDS_BRACKET = "}";

	public static final String ITERABLE_BEGINS_BRACKET = "<";
	public static final String ITERABLE_ENDS_BRACKET = ">";

	public static final String UNDEFINDED_TO_STRING_VALUE = "¿";
	public static final String NULL_VALUE = "Ø";
	public static final String ERROR_VALUE = "É";

	private String methodEnterSymbol = METHOD_ENTER_SYMBOL;
	private String methodSuccessfulReturnSymbol = METHOD_SUCCESSFUL_RETURN_SYMBOL;
	private String returnValueSymbol = RETURN_VALUE_SYMBOL;
	private String methodExceptionReturSymbol = METHOD_EXCEPTION_RETURN_SYMBOL;
	private String exceptionValueSymbol = EXCEPTION_VALUE_SYMBOL;

	public void setElementsDelitmeter(String elementsDelitmeter) {
		stringUtils.setElementsDelitmeter(elementsDelitmeter);
	}

	public void setMapKeyValueDelimeter(String mapKeyValueDelimeter) {
		stringUtils.setMapKeyValueDelimeter(mapKeyValueDelimeter);
	}

	public void setArrayBeginsBracket(String arrayBeginsBracket) {
		stringUtils.setArrayBeginsBracket(arrayBeginsBracket);
	}

	public void setArrayEndsBracket(String arrayEndsBracket) {
		stringUtils.setArrayEndsBracket(arrayEndsBracket);
	}

	public void setIterableBeginsBracket(String iterableBeginsBracket) {
		stringUtils.setIterableBeginsBracket(iterableBeginsBracket);
	}

	public void setIterableEndsBracket(String iterableEndsBracket) {
		stringUtils.setIterableEndsBracket(iterableEndsBracket);
	}

	public void setUndefindedToStringValue(String undefindedToStringValue) {
		stringUtils.setUndefindedToStringValue(undefindedToStringValue);
	}

	public void setNullValue(String nullValue) {
		stringUtils.setNullValue(nullValue);
	}

	public void setErrorValue(String errorValue) {
		stringUtils.setErrorValue(errorValue);
	}

	public void setMethodEnterSymbol(String methodEnterSymbol) {
		this.methodEnterSymbol = methodEnterSymbol;
	}

	public void setMethodSuccessfulReturnSymbol(String methodSuccessfulReturnSymbol) {
		this.methodSuccessfulReturnSymbol =methodSuccessfulReturnSymbol;
	}

	public void setReturnValueSymbol(String returnValueSymbol) {
		this.returnValueSymbol = returnValueSymbol;
	}

	public void setMethodExceptionReturSymbol(String methodExceptionReturSymbol) {
		this.methodExceptionReturSymbol = methodExceptionReturSymbol;
	}

	public void setExceptionValueSymbol(String exceptionValueSymbol) {
		this.exceptionValueSymbol = exceptionValueSymbol;
	}

	private StringUtils stringUtils = new StringUtils();

	@Override
	public MessageBuilder createEnterMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args) {
		return new EnterMessageBuilder(indent, indentText, methodName, log, args);
	}

	@Override
	public MessageBuilder createSuccessfulReturnMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args, boolean returnsNothing, Object result) {
		return new SuccessfulReturnMessageBuilder(indent, indentText, methodName, log, args, returnsNothing, result);
	}

	@Override
	public MessageBuilder createExceptionReturnMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args, Throwable throwable) {
		return new ExceptionReturnMessageBuilder(indent, indentText, methodName, log, args, throwable);
	}

	private final class SimpleMdcMessageBuilder implements MessageBuilder {
		private final Log log;
		private final Object[] args;

		private SimpleMdcMessageBuilder(Log log, Object[] args) {
			this.log = log;
			this.args = args;
		}

		@Override
		public String build() {
			return stringUtils.toString(log.mdcTemplate(),args);
		}
	}

	private class EnterMessageBuilder extends CustomisableMessageBulder {

		public EnterMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args) {
			super(indent, indentText, methodName, log, args,stringUtils);
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append(methodEnterSymbol);
		}
	}

	private class SuccessfulReturnMessageBuilder extends CustomisableMessageBulder {

		private final Object result;
		private final boolean returnsNothing;

		public SuccessfulReturnMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args, boolean returnsNothing, Object result) {
			super(indent, indentText, methodName, log, args,stringUtils);
			this.returnsNothing = returnsNothing;
			this.result = result;
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append(methodSuccessfulReturnSymbol);
		}

		@Override
		protected void buildResultDelimeter() {
			if (isBuildingResultRequired()) {
				getStringBuilder().append(returnValueSymbol);
			}
		}

		@Override
		protected void buildResult() {
			if (isBuildingResultRequired()) {
				getStringBuilder().append(stringUtils.toString(getLog().resultTemplate(), result));
			}
		}

		private boolean isBuildingResultRequired() {
			return !getLog().resultTemplate().isEmpty() && !returnsNothing;
		}
	}

	private class ExceptionReturnMessageBuilder extends CustomisableMessageBulder {

		private final Throwable throwable;

		public ExceptionReturnMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args, Throwable throwable) {
			super(indent, indentText, methodName, log, args,stringUtils);
			this.throwable = throwable;
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append(methodExceptionReturSymbol);
		}

		@Override
		protected void buildResultDelimeter() {
			getStringBuilder().append(exceptionValueSymbol);
		}

		@Override
		protected void buildResult() {
			if (!getLog().exceptionTemplate().isEmpty()) {
				getStringBuilder().append(stringUtils.toString(getLog().exceptionTemplate(), throwable));
			}
		}
	}

	@Override
	public MessageBuilder createMdcTemplate(final Log log, final Object[] args) {
		return new SimpleMdcMessageBuilder(log, args);
	}

}
