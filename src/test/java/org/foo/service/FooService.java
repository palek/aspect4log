package org.foo.service;

import java.util.List;

import net.sf.aspect4log.Log;
import net.sf.aspect4log.LogLevel;

import org.foo.dao.FooDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log
public class FooService {

	@Autowired
	FooDao fooDao;

	@Log(successfulReturnLevel = LogLevel.DEBUG)
	public boolean isFoo(String foo) {
		return foo.equals("foo");
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

	@Log(argumentsTemplate="args were: ${args[0]}",exceptionTemplate="Here goes only ${exception.message}")
	public void saveFoo(String foo) {
		if (isFoo(foo)) {
			fooDao.saveFoo(foo);
		}else throw new IllegalArgumentException("Not FOO");
	}

}
