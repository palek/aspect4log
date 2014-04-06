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

public abstract class BaseCustomisableMessageBulder implements MessageBuilder {
	private final StringBuilder stringBuilder = new StringBuilder();
	private final Integer ident;
	private final String methodName;
	private final Log log;
	private final Object[] args;

	public BaseCustomisableMessageBulder(Integer ident, String methodName, Log log, Object[] args) {
		this.ident = ident;
		this.methodName = methodName;
		this.log = log;
		this.args = args.clone();
	}

	@Override
	public final String build() {
		buildDirectionSymbol();
		buildIdent();
		buildMethodName();
		buildMethodOpenBracket();
		buildArguments();
		buildMethodClosedBracket();
		buildResultDelimeter();
		buildResult();
		return stringBuilder.toString();
	}

	protected void buildIdent() {
		if (log.useIdent()) {
			for (int i = 0; i < ident.intValue(); i++) {
				stringBuilder.append(log.identText());
			}
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
				stringBuilder.append(StringUtils.toString(args));
			} else {
				stringBuilder.append(StringUtils.toString(log.argumentsTemplate(), args));
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

	protected Integer getIdent() {
		return ident;
	}

	protected String getMethodName() {
		return methodName;
	}

	protected Log getLog() {
		return log;
	}

	protected Object[] getArgs() {
		return args;
	}

}