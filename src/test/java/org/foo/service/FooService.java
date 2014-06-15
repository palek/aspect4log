package org.foo.service;

import static net.sf.aspect4log.LogLevel.DEBUG;
import static net.sf.aspect4log.LogLevel.INFO;

import java.util.List;

import net.sf.aspect4log.Log;

import org.foo.dao.FooDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log
public class FooService {

	@Autowired
	private FooDao fooDao;

	@Log(enterLevel=INFO, exitLevel = DEBUG, argumentsTemplate="oo={${args[0]},${args[1]}}")
	public void doFoo(Object... oo){
		
	}
	
	@Log(enterLevel=INFO, exitLevel = DEBUG, argumentsTemplate="foo=${args[0]}")
	public boolean isFoo(String foo) {
		return foo.toLowerCase().startsWith("foo");
	}

	public boolean isAtLeastOneFoo(Object... foo) {
		for (Object f : foo) {
			if (f.equals("foo")) {
				return true;
			}
		}
		return false;
	}

	public List<String> findFoo() {
		return fooDao.findFoo();
	}

	@Log(argumentsTemplate="args were: ${args[0]}")
	public void saveFoo(String foo) {
		if (isFoo(foo)) {
			fooDao.saveFoo(foo);
		}else throw new IllegalArgumentException("Not FOO");
	}

}
