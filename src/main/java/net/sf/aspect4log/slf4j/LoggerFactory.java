package net.sf.aspect4log.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class LoggerFactory implements ILoggerFactory{

	@Override
	public Logger getLogger(String name) {
	    Logger logger =	 new IdentAwareLogger(org.slf4j.LoggerFactory.getLogger(name));
		return null;
	}
}
