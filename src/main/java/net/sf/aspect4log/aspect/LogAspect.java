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
package net.sf.aspect4log.aspect;

import net.sf.aspect4log.LogException;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.LogLevel;
import net.sf.aspect4log.conf.LogFormatConfiguration;
import net.sf.aspect4log.conf.LogFormatConfigurationUtils;
import net.sf.aspect4log.text.MessageBuilder;
import net.sf.aspect4log.text.MessageBuilderFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * 
 * An aspect to handle the logging rules defined via {@link Log}.
 * 
 * @author Vitaliy S <a href="mailto:vitaliy.se@gmail.com">
 * 
 */
@Aspect
public class LogAspect {

	private final LogFormatConfiguration logFormatConfiguration;

	public LogFormatConfiguration getLogFormatConfiguration() {
		return logFormatConfiguration;
	}

	public LogAspect() {
		logFormatConfiguration=LogFormatConfigurationUtils.readConfiguration();
	}

	public LogAspect(LogFormatConfiguration logFormatConfiguration) {
		if(logFormatConfiguration==null){
			throw new NullPointerException();
		}
		this.logFormatConfiguration = logFormatConfiguration;
	}

	
	private final ThreadLocal<Integer> thraedLocalIndent = new ThreadLocal<Integer>();

	@Around("execution(!@net.sf.aspect4log.Log *(@net.sf.aspect4log.Log *).*(..)) && @target(log)")
	public Object logAnnotedClassExecution(ProceedingJoinPoint pjp, Log log) throws Throwable {
		return log(pjp, log);
	}

	@Around("@annotation(log)")
	public Object logAnnotedMethodExecution(ProceedingJoinPoint pjp, Log log) throws Throwable {
		return log(pjp, log);
	}

	public final Integer getThreadLocalIdent(){
		return thraedLocalIndent.get();
	}
	
	private Object log(ProceedingJoinPoint pjp, Log log) throws Throwable {
		Logger logger = LoggerFactory.getLogger(pjp.getTarget().getClass());
		MessageBuilderFactory factory = logFormatConfiguration.getMessageBuilderFactory();
		try {
			increaseIndent(log);
			setMDC(log, pjp.getArgs());
			MessageBuilder enterMessageBuilder = factory.createEnterMessageBuilder(getThreadLocalIdent(), logFormatConfiguration.getIndentText(), pjp.getSignature().getName(), log, pjp.getArgs());
			log(logger, log.enterLevel(), enterMessageBuilder, null);
			Object result = pjp.proceed();
			Class<?> returnClass = ((MethodSignature) pjp.getSignature()).getReturnType();
			boolean returnsNothing = Void.TYPE.equals(returnClass);
			MessageBuilder successfulReturnMessageBuilder = factory.createSuccessfulReturnMessageBuilder(getThreadLocalIdent(), logFormatConfiguration.getIndentText(), pjp.getSignature().getName(), log, pjp.getArgs(), returnsNothing, result);
			log(logger, log.exitLevel(), successfulReturnMessageBuilder, null);
			return result;
		} catch (Throwable e) {
			

			LogLevel logLevel = LogLevel.ERROR;
			Throwable throwable = e;
			String template = LogException.EXCEPTION_DEFAULT_TEMPLATE;
			exceptionExitSearchLoop:
			for (LogException logException : log.logExceptions()) {
				for (Class<? extends Throwable> t : logException.exceptions()) {
					if (t.isAssignableFrom(e.getClass())) {
						logLevel = logException.level();
						throwable = logException.printStackTrace() ? e : null;
						template = logException.exceptionTemplate();
						break exceptionExitSearchLoop;
					}
				}
			}
			MessageBuilder exceptionReturnMessageBuilder = factory.createExceptionReturnMessageBuilder(getThreadLocalIdent(), logFormatConfiguration.getIndentText(), pjp.getSignature().getName(), log, pjp.getArgs(), e,template);
			log(logger, logLevel, exceptionReturnMessageBuilder, throwable);
			throw e;
		} finally {
			decreaseIndent(log);
			removeMDC(log);
		}
	}

	private void setMDC(Log log, Object[] args) {
		if (!log.mdcKey().isEmpty()) {
			MessageBuilder mdcMessageBuilder = logFormatConfiguration.getMessageBuilderFactory().createMdcTemplate(log, args);
			MDC.put(log.mdcKey(), mdcMessageBuilder.build());
		}
	}

	private void removeMDC(Log log) {
		if (!log.mdcKey().isEmpty()) {
			MDC.remove(log.mdcKey());
		}
	}

	private void increaseIndent(Log log) {
		if (logFormatConfiguration.isUseIndent()) {
			if (thraedLocalIndent.get() == null) {
				thraedLocalIndent.set(0);
			} else if (thraedLocalIndent.get() != null) {
				thraedLocalIndent.set(thraedLocalIndent.get() + 1);
			}
		}
	}

	private void decreaseIndent(Log log) {
		if (thraedLocalIndent.get() != null) {
			if (thraedLocalIndent.get() == 0) {
				thraedLocalIndent.remove();
			} else {
				thraedLocalIndent.set(thraedLocalIndent.get() - 1);
			}

		}
	}

	private void log(Logger logger, LogLevel level, MessageBuilder messageBuilder, Throwable throable) {
		switch (level) {
		case TRACE:
			if (logger.isTraceEnabled()) {
				logger.trace(messageBuilder.build(), throable);
			}
			break;
		case DEBUG:
			if (logger.isDebugEnabled()) {
				logger.debug(messageBuilder.build(), throable);
			}
			break;
		case INFO:
			if (logger.isInfoEnabled()) {
				logger.info(messageBuilder.build(), throable);
			}
			break;
		case WARN:
			if (logger.isWarnEnabled()) {
				logger.warn(messageBuilder.build(), throable);
			}
			break;
		case ERROR:
			if (logger.isErrorEnabled()) {
				logger.error(messageBuilder.build(), throable);
			}
			break;
		}
	}
	
}
