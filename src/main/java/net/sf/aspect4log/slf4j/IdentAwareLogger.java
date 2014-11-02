package net.sf.aspect4log.slf4j;

import net.sf.aspect4log.aspect.LogAspect;

import org.slf4j.Logger;
import org.slf4j.Marker;

public class IdentAwareLogger implements Logger {

	private final Logger logger;
	

	public IdentAwareLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void debug(String s) {
		logger.debug(s);
	}

	@Override
	public void debug(String s, Object o) {
		logger.debug(s, o);
	}

	@Override
	public void debug(String s, Object... o) {
		logger.debug(s, o);
	}

	@Override
	public void debug(String s, Throwable o) {
		logger.debug(s, o);
	}

	@Override
	public void debug(Marker s, String o) {
		logger.debug(s, o);
	}

	@Override
	public void debug(String s, Object o, Object oo) {
		logger.debug(s, o, oo);
	}

	@Override
	public void debug(Marker s, String o, Object oo) {
		logger.debug(s, o, oo);
	}

	@Override
	public void debug(Marker s, String o, Object... oo) {
		logger.debug(s, o, oo);
	}

	@Override
	public void debug(Marker s, String o, Throwable oo) {
		logger.debug(s, o, oo);
	}

	@Override
	public void debug(Marker s, String o, Object a, Object oo) {
		logger.debug(s, o, a, oo);
	}

	@Override
	public void error(String s) {
		logger.error(s);
	}

	@Override
	public void error(String s, Object o) {
		logger.error(s, o);
	}

	@Override
	public void error(String s, Object... o) {
		logger.error(s, o);
	}

	@Override
	public void error(String s, Throwable o) {
		logger.error(s, o);
	}

	@Override
	public void error(Marker s, String o) {
		logger.error(s, o);
	}

	@Override
	public void error(String s, Object o, Object oo) {
		logger.error(s, o, oo);
	}

	@Override
	public void error(Marker s, String o, Object oo) {
		logger.error(s, o, oo);
	}

	@Override
	public void error(Marker s, String o, Object... oo) {
		logger.error(s, o, oo);
	}

	@Override
	public void error(Marker s, String o, Throwable oo) {
		logger.error(s, o, oo);
	}

	@Override
	public void error(Marker s, String o, Object a, Object oo) {
		logger.error(s, o, a, oo);
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	@Override
	public void info(String s) {
		logger.info(s);
	}

	@Override
	public void info(String s, Object o) {
		logger.info(s, o);
	}

	@Override
	public void info(String s, Object... o) {
		logger.info(s, o);
	}

	@Override
	public void info(String s, Throwable o) {
		logger.info(s, o);
	}

	@Override
	public void info(Marker s, String o) {
		logger.info(s, o);
	}

	@Override
	public void info(String s, Object o, Object oo) {
		logger.info(s, o, oo);
	}

	@Override
	public void info(Marker s, String o, Object oo) {
		logger.info(s, o, oo);
	}

	@Override
	public void info(Marker s, String o, Object... oo) {
		logger.info(s, o, oo);
	}

	@Override
	public void info(Marker s, String o, Throwable oo) {
		logger.info(s, o, oo);
	}

	@Override
	public void info(Marker s, String o, Object a, Object oo) {
		logger.info(s, o, a, oo);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isDebugEnabled(Marker arg0) {
		return logger.isDebugEnabled(arg0);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled(Marker arg0) {
		return logger.isErrorEnabled(arg0);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(Marker arg0) {
		return logger.isInfoEnabled(arg0);
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public boolean isTraceEnabled(Marker arg0) {
		return logger.isTraceEnabled(arg0);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public boolean isWarnEnabled(Marker arg0) {
		return logger.isWarnEnabled(arg0);
	}

	@Override
	public void trace(String s) {
		logger.trace(s);
	}

	@Override
	public void trace(String s, Object o) {
		logger.trace(s, o);
	}

	@Override
	public void trace(String s, Object... o) {
		logger.trace(s, o);
	}

	@Override
	public void trace(String s, Throwable o) {
		logger.trace(s, o);
	}

	@Override
	public void trace(Marker s, String o) {
		logger.trace(s, o);
	}

	@Override
	public void trace(String s, Object o, Object oo) {
		logger.trace(s, o, oo);
	}

	@Override
	public void trace(Marker s, String o, Object oo) {
		logger.trace(s, o, oo);
	}

	@Override
	public void trace(Marker s, String o, Object... oo) {
		logger.trace(s, o, oo);
	}

	@Override
	public void trace(Marker s, String o, Throwable oo) {
		logger.trace(s, o, oo);
	}

	@Override
	public void trace(Marker s, String o, Object a, Object oo) {
		logger.trace(s, o, a, oo);
	}

	@Override
	public void warn(String s) {
		logger.warn(s);
	}

	@Override
	public void warn(String s, Object o) {
		logger.warn(s, o);
	}

	@Override
	public void warn(String s, Object... o) {
		logger.warn(s, o);
	}

	@Override
	public void warn(String s, Throwable o) {
		logger.warn(s, o);
	}

	@Override
	public void warn(Marker s, String o) {
		logger.warn(s, o);
	}

	@Override
	public void warn(String s, Object o, Object oo) {
		logger.warn(s, o, oo);
	}

	@Override
	public void warn(Marker s, String o, Object oo) {
		logger.warn(s, o, oo);
	}

	@Override
	public void warn(Marker s, String o, Object... oo) {
		logger.warn(s, o, oo);
	}

	@Override
	public void warn(Marker s, String o, Throwable oo) {
		logger.warn(s, o, oo);
	}

	@Override
	public void warn(Marker s, String o, Object a, Object oo) {
		logger.warn(s, o, a, oo);
	}

}
