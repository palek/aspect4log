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
	public static final String METHOD_SUCCESSFUL_EXIT_SYMBOL = "↑";
	public static final String METHOD_THROWN_EXCEPTION_EXIT_SYMBOL = "⇑";
	public static final String RETURNED_VALUE_SEPORATOR = " → ";
	public static final String THROWN_EXCEPTION_SEPORATOR = " ⇒ ";

	private String methodEnterSymbol = METHOD_ENTER_SYMBOL;
	private String methodSuccessfulExitSymbol = METHOD_SUCCESSFUL_EXIT_SYMBOL;
	private String methodThrownExceptionExitSymbol = METHOD_THROWN_EXCEPTION_EXIT_SYMBOL;
	private String returnedValueSeporator = RETURNED_VALUE_SEPORATOR;
	private String thrownExceptionSeporator = THROWN_EXCEPTION_SEPORATOR;

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

	public void setUndefindedToStringMethodSymbol(String undefindedToStringValue) {
		stringUtils.setUndefindedToStringMethodSymbol(undefindedToStringValue);
	}

	public void setNullSymbol(String nullValue) {
		stringUtils.setNullSymbol(nullValue);
	}

	public void setErrorEvaluatingToStringSymbol(String errorValue) {
		stringUtils.setErrorEvaluatingToStringSymbol(errorValue);
	}

	public void setMethodEnterSymbol(String methodEnterSymbol) {
		this.methodEnterSymbol = methodEnterSymbol;
	}

	public void setMethodSuccessfulExitSymbol(String methodSuccessfulReturnSymbol) {
		this.methodSuccessfulExitSymbol = methodSuccessfulReturnSymbol;
	}

	public void setMethodThrownExceptionExitSymbol(String returnValueSymbol) {
		this.methodThrownExceptionExitSymbol = returnValueSymbol;
	}

	public void setThrownExceptionSeporator(String methodExceptionReturSymbol) {
		this.thrownExceptionSeporator = methodExceptionReturSymbol;
	}

	public void setReturnedValueSeporator(String exceptionValueSymbol) {
		this.returnedValueSeporator = exceptionValueSymbol;
	}

	private StringUtils stringUtils = new StringUtils();

	@Override
	public MessageBuilder createEnterMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args) {
		return new EnterMessageBuilder(indent, indentText, methodName, log, args);
	}

	@Override
	public MessageBuilder createSuccessfulReturnMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args, boolean returnsNothing, Object result) {
		return new MethodSuccessfulExitMessageBuilder(indent, indentText, methodName, log, args, returnsNothing, result);
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
			return stringUtils.toString(log.mdcTemplate(), args);
		}
	}

	private class EnterMessageBuilder extends CustomisableMessageBulder {

		public EnterMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args) {
			super(indent, indentText, methodName, log, args, stringUtils);
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append(methodEnterSymbol);
		}
	}

	private class MethodSuccessfulExitMessageBuilder extends CustomisableMessageBulder {

		private final Object result;
		private final boolean returnsNothing;

		public MethodSuccessfulExitMessageBuilder(Integer indent, String indentText, String methodName, Log log, Object[] args, boolean returnsNothing, Object result) {
			super(indent, indentText, methodName, log, args, stringUtils);
			this.returnsNothing = returnsNothing;
			this.result = result;
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append(methodSuccessfulExitSymbol);
		}

		@Override
		protected void buildResultDelimeter() {
			if (isBuildingResultRequired()) {
				getStringBuilder().append(returnedValueSeporator);
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
			super(indent, indentText, methodName, log, args, stringUtils);
			this.throwable = throwable;
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append(methodThrownExceptionExitSymbol);
		}

		@Override
		protected void buildResultDelimeter() {
			getStringBuilder().append(thrownExceptionSeporator);
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
