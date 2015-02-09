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
import net.sf.aspect4log.aspect.LogAspect;
import net.sf.aspect4log.conf.LogFormatConfiguration;

/**
 *
 * This class is responsible for creating {@link MessageBuilder} instances.
 * Log decoration properties are set by {@link LogFormatConfiguration via reflection.
 * 
 * @author yilativs
 *
 */
public class CustomisableMessageBuilderFactory implements MessageBuilderFactory {
	public static final String METHOD_ENTER_SYMBOL = "↓";
	public static final String METHOD_SUCCESSFUL_EXIT_SYMBOL = "↑";
	public static final String METHOD_THROWN_EXCEPTION_EXIT_SYMBOL = "⇑";
	public static final String RETURNED_VALUE_SEPARATOR = " → ";
	public static final String THROWN_EXCEPTION_SEPARATOR = " ⇒ ";

	private String methodEnterSymbol = METHOD_ENTER_SYMBOL;
	private String methodSuccessfulExitSymbol = METHOD_SUCCESSFUL_EXIT_SYMBOL;
	private String methodThrownExceptionExitSymbol = METHOD_THROWN_EXCEPTION_EXIT_SYMBOL;
	private String returnedValueSeparator = RETURNED_VALUE_SEPARATOR;
	private String thrownExceptionSeparator = THROWN_EXCEPTION_SEPARATOR;

	/**
	 * 
	 * @return specifies if indent should be used for methods called inside marked method. The default value is true.
	 */

	private boolean useIndent = true;

	/**
	 * @return a java.lang.String that represents text of Indent. The default value is 1 tab symbol.
	 */

	private String indentText = "\t";

	public boolean isUseIndent() {
		return useIndent;
	}

	public void setUseIndent(boolean useIndent) {
		this.useIndent = useIndent;
	}

	public String getIndentText() {
		return indentText;
	}

	public void setIndentText(String indentText) {
		this.indentText = indentText;
	}

	public void setElementsDelitmiter(String elementsDelitmiter) {
		logFormatter.setElementsDelitmiter(elementsDelitmiter);
	}

	public void setMapKeyValueDelimeter(String mapKeyValueDelimeter) {
		logFormatter.setMapKeyValueDelimeter(mapKeyValueDelimeter);
	}

	public void setArrayBeginsBracket(String arrayBeginsBracket) {
		logFormatter.setArrayBeginsBracket(arrayBeginsBracket);
	}

	public void setArrayEndsBracket(String arrayEndsBracket) {
		logFormatter.setArrayEndsBracket(arrayEndsBracket);
	}

	public void setIterableBeginsBracket(String iterableBeginsBracket) {
		logFormatter.setIterableBeginsBracket(iterableBeginsBracket);
	}

	public void setIterableEndsBracket(String iterableEndsBracket) {
		logFormatter.setIterableEndsBracket(iterableEndsBracket);
	}

	public void setUndefindedToStringMethodSymbol(String undefindedToStringValue) {
		logFormatter.setUndefindedToStringMethodSymbol(undefindedToStringValue);
	}

	public void setNullSymbol(String nullValue) {
		logFormatter.setNullSymbol(nullValue);
	}

	public void setErrorEvaluatingToStringSymbol(String errorValue) {
		logFormatter.setErrorEvaluatingToStringSymbol(errorValue);
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

	public void setThrownExceptionSeparator(String methodExceptionReturSymbol) {
		this.thrownExceptionSeparator = methodExceptionReturSymbol;
	}

	public void setReturnedValueSeparator(String exceptionValueSymbol) {
		this.returnedValueSeparator = exceptionValueSymbol;
	}

	private final LogFormatter logFormatter = new LogFormatter();

	@Override
	public MessageBuilder createEnterMessageBuilder(String methodName, Log log, Object[] args) {
		return new EnterMessageBuilder(methodName, log, args);
	}

	@Override
	public MessageBuilder createSuccessfulReturnMessageBuilder(String methodName, Log log, Object[] args, boolean returnsNothing, Object result) {
		return new MethodSuccessfulExitMessageBuilder(methodName, log, args, returnsNothing, result);
	}

	@Override
	public MessageBuilder createExceptionReturnMessageBuilder(String methodName, Log log, Object[] args, Throwable throwable, String exceptionExitTemplate) {
		return new ExceptionReturnMessageBuilder(methodName, log, args, throwable, exceptionExitTemplate);
	}

	private final class SimpleMdcMessageBuilder implements MessageBuilder {
		private final Log log;
		private final Object[] args;

		protected SimpleMdcMessageBuilder(Log log, Object[] args) {
			this.log = log;
			this.args = (Object[])args.clone();
		}

		@Override
		public String build() {
			return logFormatter.toString(log.mdcTemplate(), args);
		}
	}

	private class EnterMessageBuilder extends CustomisableMessageBulder {

		public EnterMessageBuilder(String methodName, Log log, Object[] args) {
			super(methodName, log, args, logFormatter);
		}

		@Override
		protected void buildDirectionSymbol(StringBuilder stringBuilder) {
			stringBuilder.append(methodEnterSymbol);
		}
	}

	private class MethodSuccessfulExitMessageBuilder extends CustomisableMessageBulder {

		private final Object result;
		private final boolean returnsNothing;

		public MethodSuccessfulExitMessageBuilder(String methodName, Log log, Object[] args, boolean returnsNothing, Object result) {
			super(methodName, log, args, logFormatter);
			this.returnsNothing = returnsNothing;
			this.result = result;
		}

		@Override
		protected void buildDirectionSymbol(StringBuilder stringBuilder) {
			stringBuilder.append(methodSuccessfulExitSymbol);
		}

		@Override
		protected void buildResultDelimeter(StringBuilder stringBuilder) {
			if (isBuildingResultRequired()) {
				stringBuilder.append(returnedValueSeparator);
			}
		}

		@Override
		protected void buildResult(StringBuilder stringBuilder) {
			if (isBuildingResultRequired()) {
				stringBuilder.append(logFormatter.toString(getLog().resultTemplate(), result));
			}
		}

		private boolean isBuildingResultRequired() {
			return !getLog().resultTemplate().isEmpty() && !returnsNothing;
		}
	}

	private class ExceptionReturnMessageBuilder extends CustomisableMessageBulder {

		private final Throwable throwable;
		private final String exceptionExitTemplate;

		public ExceptionReturnMessageBuilder(String methodName, Log log, Object[] args, Throwable throwable, String exceptionExitTemplate) {
			super(methodName, log, args, logFormatter);
			this.throwable = throwable;
			this.exceptionExitTemplate = exceptionExitTemplate;
		}

		@Override
		protected void buildDirectionSymbol(StringBuilder stringBuilder) {
			stringBuilder.append(methodThrownExceptionExitSymbol);
		}

		@Override
		protected void buildResultDelimeter(StringBuilder stringBuilder) {
			stringBuilder.append(thrownExceptionSeparator);
		}

		@Override
		protected void buildResult(StringBuilder stringBuilder) {
			if (!exceptionExitTemplate.isEmpty()) {
				stringBuilder.append(logFormatter.toString(exceptionExitTemplate, throwable));
			}
		}
	}

	@Override
	public MessageBuilder createMdcTemplate(final Log log, final Object[] args) {
		return new SimpleMdcMessageBuilder(log, args);
	}

	private abstract class CustomisableMessageBulder implements MessageBuilder {
		private final String methodName;
		private final Log log;
		private final Object[] args;
		private final LogFormatter logFormatter;

		public CustomisableMessageBulder(String methodName, Log log, Object[] args, LogFormatter logFormatter) {
			this.methodName = methodName;
			this.log = log;
			this.logFormatter = logFormatter;
			this.args = args.clone();
		}

		@Override
		public final String build() {
			StringBuilder stringBuilder = new StringBuilder();
			buildDirectionSymbol(stringBuilder);
			buildIndent(stringBuilder);
			buildMethodName(stringBuilder);
			buildMethodOpenBracket(stringBuilder);
			buildArguments(stringBuilder);
			buildMethodClosedBracket(stringBuilder);
			buildResultDelimeter(stringBuilder);
			buildResult(stringBuilder);
			return stringBuilder.toString();
		}

		protected void buildIndent(StringBuilder stringBuilder) {
			for (int i = 0; i < LogAspect.getThreadLocalIdent().intValue(); i++) {
				stringBuilder.append(indentText);
			}
		}

		abstract protected void buildDirectionSymbol(StringBuilder stringBuilder);

		protected void buildMethodName(StringBuilder stringBuilder) {
			stringBuilder.append(methodName);
		}

		protected void buildMethodOpenBracket(StringBuilder stringBuilder) {
			stringBuilder.append("(");
		}

		protected void buildArguments(StringBuilder stringBuilder) {
			if (!log.argumentsTemplate().isEmpty()) {
				if (Log.ARGUMENTS_DEFAULT_TEMPLATE.equals(log.argumentsTemplate())) {
					stringBuilder.append(logFormatter.toString(args));
				} else {
					stringBuilder.append(logFormatter.toString(log.argumentsTemplate(), args));
				}
			}
		}

		protected void buildMethodClosedBracket(StringBuilder stringBuilder) {
			stringBuilder.append(")");
		}

		protected void buildResultDelimeter(StringBuilder stringBuilder) {
			// there is no result by default
		}

		protected void buildResult(StringBuilder stringBuilder) {
			// there is no result by default
		}

		protected Log getLog() {
			return log;
		}

	}

	@Override
	public String buildIdent() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < LogAspect.getThreadLocalIdent().intValue(); i++) {
			stringBuilder.append(indentText);
		}
		return stringBuilder.toString();
	}

}
