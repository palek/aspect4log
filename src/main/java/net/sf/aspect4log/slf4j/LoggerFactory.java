package net.sf.aspect4log.slf4j;

import org.slf4j.Logger;

public class LoggerFactory  {

	
	public static Logger getLogger(String name) {
		return new IdentAwareLogger(org.slf4j.LoggerFactory.getLogger(name));
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return new IdentAwareLogger(org.slf4j.LoggerFactory.getLogger(clazz.getName()));
	}
}
