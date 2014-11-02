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

public abstract class CustomisableMessageBulder implements MessageBuilder {
	private final StringBuilder stringBuilder = new StringBuilder();
	private final Integer indent;
	private final String methodName;
	private final Log log;
	private final Object[] args;
	private final String indentText;
	private LogFormatter logFormatter;

	public CustomisableMessageBulder(Integer indent, String indentText, String methodName, Log log, Object[] args, LogFormatter logFormatter) {
		this.indent = indent;
		this.indentText = indentText;
		this.methodName = methodName;
		this.log = log;
		this.logFormatter = logFormatter;
		this.args = args.clone();
	}

	@Override
	public final String build() {
		buildDirectionSymbol();
		buildIndent();
		buildMethodName();
		buildMethodOpenBracket();
		buildArguments();
		buildMethodClosedBracket();
		buildResultDelimeter();
		buildResult();
		return stringBuilder.toString();
	}

	protected void buildIndent() {
		for (int i = 0; i < indent.intValue(); i++) {
			stringBuilder.append(indentText);
		}
	}

	abstract protected void buildDirectionSymbol();

	protected void buildMethodName() {
		stringBuilder.append(methodName);
	}

	protected void buildMethodOpenBracket() {
		stringBuilder.append("(");
	}

	protected void buildArguments() {
		if (!log.argumentsTemplate().isEmpty()) {
			if (Log.ARGUMENTS_DEFAULT_TEMPLATE.equals(log.argumentsTemplate())) {
				stringBuilder.append(logFormatter.toString(args));
			} else {
				stringBuilder.append(logFormatter.toString(log.argumentsTemplate(), args));
			}
		}
	}

	protected void buildMethodClosedBracket() {
		stringBuilder.append(")");
	}

	protected void buildResultDelimeter() {
		// there is no result by default
	}

	protected void buildResult() {
		// there is no result by default
	}

	protected StringBuilder getStringBuilder() {
		return stringBuilder;
	}

	protected Integer getIndent() {
		return indent;
	}

	protected String getMethodName() {
		return methodName;
	}

	protected Log getLog() {
		return log;
	}

	protected Object[] getArgs() {
		return args.clone();
	}

}