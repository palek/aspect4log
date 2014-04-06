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
package net.sf.aspect4log.aspect;

import java.util.concurrent.ConcurrentHashMap;

import net.sf.aspect4log.Log;
import net.sf.aspect4log.LogLevel;
import net.sf.aspect4log.text.MessageBuilder;
import net.sf.aspect4log.text.MessageBuilderFactory;
import net.sf.aspect4log.text.StringUtils;

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
	// TODO consider using Weak references here
	private final ConcurrentHashMap<Class<?>, Logger> classLoggerMap = new ConcurrentHashMap<>();
	private final ThreadLocal<Integer> thraedLocalIdent = new ThreadLocal<Integer>();

	@Around("execution(!@net.sf.aspect4log.Log *(@net.sf.aspect4log.Log *).*(..)) && @target(log)")
	public Object logAnnotedClassExecution(ProceedingJoinPoint pjp, Log log) throws Throwable {
		return log(pjp, log);
	}

	@Around("@annotation(log)")
	public Object logAnnotedMethodExecution(ProceedingJoinPoint pjp, Log log) throws Throwable {
		return log(pjp, log);
	}

	private Object log(ProceedingJoinPoint pjp, Log log) throws Throwable {
		Logger logger = getLogger(pjp);
		MessageBuilderFactory factory = log.messageBuilderFactory().newInstance();
		try {
			increaseIdent(log);
			setMDC(log, pjp.getArgs());
			MessageBuilder enterMessageBuilder = factory.createEnterMessageBuilder(thraedLocalIdent.get(), pjp.getSignature().getName(), log, pjp.getArgs());
			log(logger, log.enterLevel(), enterMessageBuilder);
			Object result = pjp.proceed();
			Class<?> returnClass = ((MethodSignature) pjp.getSignature()).getReturnType();
			boolean returnsNothing = "void".equals(returnClass.getCanonicalName());
			MessageBuilder successfulReturnMessageBuilder = factory.createSuccessfulReturnMessageBuilder(thraedLocalIdent.get(), pjp.getSignature().getName(), log, pjp.getArgs(), returnsNothing, result);
			log(logger, log.successfulReturnLevel(), successfulReturnMessageBuilder);
			return result;
		} catch (Throwable e) {
			MessageBuilder exceptionReturnMessageBuilder = factory.createExceptionReturnMessageBuilder(thraedLocalIdent.get(), pjp.getSignature().getName(), log, pjp.getArgs(), e);
			log(logger, log.exceptionReturnLevel(), exceptionReturnMessageBuilder);
			throw e;
		} finally {
			decreaseIdent(log);
			removeMDC(log);
		}
	}

	private void setMDC(Log log, Object[] args) {
		if (!log.mdcKey().isEmpty()) {
			MDC.put(log.mdcKey(), extractMdcValue(log.mdcTemplate(), args));
		}
	}

	private void removeMDC(Log log) {
		if (!log.mdcKey().isEmpty()) {
			MDC.remove(log.mdcKey());
		}
	}

	private String extractMdcValue(String mdcTemplate, Object[] args) {
		return StringUtils.toString(mdcTemplate, args);
	}

	private void increaseIdent(Log log) {
		if (log.useIdent()) {
			if (thraedLocalIdent.get() == null) {
				thraedLocalIdent.set(0);
			} else if (thraedLocalIdent.get() != null) {
				thraedLocalIdent.set(thraedLocalIdent.get() + 1);
			}
		}
	}

	private void decreaseIdent(Log log) {
		if (thraedLocalIdent.get() != null) {
			if (thraedLocalIdent.get() == 0) {
				thraedLocalIdent.remove();
			} else {
				thraedLocalIdent.set(thraedLocalIdent.get() - 1);
			}

		}
	}

	private void log(Logger logger, LogLevel level, MessageBuilder messageBuilder) {
		switch (level) {
		case TRACE:
			if (logger.isTraceEnabled()) {
				logger.trace(messageBuilder.build());
			}
			break;
		case DEBUG:
			if (logger.isDebugEnabled()) {
				logger.debug(messageBuilder.build());
			}
			break;
		case INFO:
			if (logger.isInfoEnabled()) {
				logger.info(messageBuilder.build());
			}
			break;
		case WARN:
			if (logger.isWarnEnabled()) {
				logger.warn(messageBuilder.build());
			}
			break;
		case ERROR:
			if (logger.isErrorEnabled()) {
				logger.error(messageBuilder.build());
			}
			break;
		case NONE:
			break;
		}
	}

	private Logger getLogger(ProceedingJoinPoint pjp) {
		Class<?> c = pjp.getTarget().getClass();
		if (!classLoggerMap.contains(c)) {
			classLoggerMap.putIfAbsent(c, LoggerFactory.getLogger(c));
		}
		return classLoggerMap.get(c);
	}
}
