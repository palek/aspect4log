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

public class CompactFormatMessageBuilderFactory implements MessageBuilderFactory {
	private static final char METHOD_ENTER_SYMBOL = '↓';
	private static final char METHOD_RETURN_SYMBOL = '→';
	private static final String METHOD_EXCEPTION_RETURN_DELIMITER = " ⇒ ";
	private static final char METHOD_EXCEPTION_EXIT_SYMBOL = '⇑';

	@Override
	public MessageBuilder createEnterMessageBuilder(Integer indent, String methodName, Log log, Object[] args) {
		return new EnterMessageBuilder(indent, methodName, log, args);
	}

	@Override
	public MessageBuilder createSuccessfulReturnMessageBuilder(Integer indent, String methodName, Log log, Object[] args, boolean returnsNothing, Object result) {
		return new SuccessfulReturnMessageBuilder(indent, methodName, log, args, returnsNothing, result);
	}

	@Override
	public MessageBuilder createExceptionReturnMessageBuilder(Integer indent, String methodName, Log log, Object[] args, Throwable throwable) {
		return new ExceptionReturnMessageBuilder(indent, methodName, log, args, throwable);
	}

	private static class EnterMessageBuilder extends BaseCustomisableMessageBulder {

		public EnterMessageBuilder(Integer indent, String methodName, Log log, Object[] args) {
			super(indent, methodName, log, args);
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append(METHOD_ENTER_SYMBOL);
		}
	}

	private static class SuccessfulReturnMessageBuilder extends BaseCustomisableMessageBulder {

		private final Object result;
		private final boolean returnsNothing;

		public SuccessfulReturnMessageBuilder(Integer indent, String methodName, Log log, Object[] args, boolean returnsNothing, Object result) {
			super(indent, methodName, log, args);
			this.returnsNothing = returnsNothing;
			this.result = result;
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append('↑');
		}

		@Override
		protected void buildResultDelimeter() {
			if (isBuildingResultRequired()) {
				getStringBuilder().append(METHOD_RETURN_SYMBOL);
			}
		}

		@Override
		protected void buildResult() {
			if (isBuildingResultRequired()) {
				getStringBuilder().append(StringUtils.toString(getLog().resultTemplate(), result));
			}
		}

		private boolean isBuildingResultRequired() {
			return !getLog().resultTemplate().isEmpty() && !returnsNothing;
		}
	}

	private static class ExceptionReturnMessageBuilder extends BaseCustomisableMessageBulder {

		private final Throwable throwable;

		public ExceptionReturnMessageBuilder(Integer indent, String methodName, Log log, Object[] args, Throwable throwable) {
			super(indent, methodName, log, args);
			this.throwable = throwable;
		}

		@Override
		protected void buildDirectionSymbol() {
			getStringBuilder().append(METHOD_EXCEPTION_EXIT_SYMBOL);
		}

		@Override
		protected void buildResultDelimeter() {
			getStringBuilder().append(METHOD_EXCEPTION_RETURN_DELIMITER);
		}

		@Override
		protected void buildResult() {
			if (!getLog().exceptionTemplate().isEmpty()) {
				getStringBuilder().append(StringUtils.toString(getLog().exceptionTemplate(), throwable));
			}
		}
	}

}