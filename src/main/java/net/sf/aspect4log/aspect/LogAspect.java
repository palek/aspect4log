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

import net.sf.aspect4log.Log;
import net.sf.aspect4log.LogException;
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

	/*
	 * LogFormatConfiguration can be only one per application
	 */
	private static LogFormatConfiguration logFormatConfiguration;

	public static LogFormatConfiguration getLogFormatConfiguration() {
		return logFormatConfiguration;
	}

	public LogAspect() {
		logFormatConfiguration = LogFormatConfigurationUtils.readConfiguration();
	}

	public LogAspect(LogFormatConfiguration logFormatConfiguration) {
		if (logFormatConfiguration == null) {
			throw new NullPointerException();
		}
		LogAspect.logFormatConfiguration = logFormatConfiguration;
	}

	/*
	 * it is marked static, because if there is more than one instance of LogApsect we must have one ident system per thread
	 */
	private static final ThreadLocal<Integer> thraedLocalIndent = new ThreadLocal<Integer>();

	@Around("(execution(!@Log *(@Log *).*(..))|| execution(!@Log *.new(..)))  && @within(log)")
	public Object logAnnotedClassExecution(ProceedingJoinPoint pjp, Log log) throws Throwable {
		return log(pjp, log);
	}

	@Around("(execution(@Log *.new(..)) || execution(@Log * *.*(..)) ) && @annotation(log)")
	public Object logAnnotedMethodExecution(ProceedingJoinPoint pjp, Log log) throws Throwable {
		return log(pjp, log);
	}

	public static final Integer getThreadLocalIdent() {
		return thraedLocalIndent.get() == null ? 0 : thraedLocalIndent.get();
	}

	private Object log(ProceedingJoinPoint pjp, Log log) throws Throwable {

		MessageBuilderFactory factory = logFormatConfiguration.getMessageBuilderFactory();

		boolean isConstractorCall = "constructor-execution".equals(pjp.getStaticPart().getKind());

		
		String declaringClass = pjp.getSignature().getDeclaringTypeName();
		String methodName =  isConstractorCall ? pjp.getSignature().getDeclaringType().getSimpleName(): pjp.getSignature().getName();
		try {
			increaseIndent(log);
			setMDC(log, pjp.getArgs());

			MessageBuilder enterMessageBuilder = factory.createEnterMessageBuilder(methodName, log, pjp.getArgs());
			log(declaringClass, log.enterLevel(), enterMessageBuilder, null);
			Object result = pjp.proceed();
			Class<?> returnClass = null;
			boolean returnsNothing = true;
			if(isConstractorCall){
				returnsNothing=false;
			}else{
				returnClass = ((MethodSignature) pjp.getSignature()).getReturnType();
				returnsNothing = Void.TYPE.equals(returnClass);
			}
			MessageBuilder successfulReturnMessageBuilder = factory
					.createSuccessfulReturnMessageBuilder(methodName, log, pjp.getArgs(), returnsNothing, result);
			log(declaringClass, log.exitLevel(), successfulReturnMessageBuilder, null);
			return result;
		} catch (Throwable e) {
			LogLevel logLevel = LogLevel.ERROR;
			Throwable throwable = e;
			String template = LogException.EXCEPTION_DEFAULT_TEMPLATE;
			exceptionExitSearchLoop: for (LogException logException : log.logExceptions()) {
				for (Class<? extends Throwable> t : logException.exceptions()) {
					if (t.isAssignableFrom(e.getClass())) {
						logLevel = logException.level();
						throwable = logException.printStackTrace() ? e : null;
						template = logException.exceptionTemplate();
						break exceptionExitSearchLoop;
					}
				}
			}
			MessageBuilder exceptionReturnMessageBuilder = factory.createExceptionReturnMessageBuilder(methodName, log, pjp.getArgs(), e, template);
			log(declaringClass, logLevel, exceptionReturnMessageBuilder, throwable);
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
		if (thraedLocalIndent.get() == null) {
			thraedLocalIndent.set(0);
		} else if (thraedLocalIndent.get() != null) {
			thraedLocalIndent.set(thraedLocalIndent.get() + 1);
		}
	}

	private void decreaseIndent(Log log) {
		if (thraedLocalIndent.get() == 0) {
			thraedLocalIndent.remove();
		} else {
			thraedLocalIndent.set(thraedLocalIndent.get() - 1);
		}
	}

	private void log(String clazz, LogLevel level, MessageBuilder messageBuilder, Throwable throable) {
		Logger logger = LoggerFactory.getLogger(clazz);
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
