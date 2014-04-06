package org.foo.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.aspect4log.Log;

import org.springframework.stereotype.Repository;

@Repository
@Log
public class FooDao {

	public void saveFoo(String foo) {

	}

	public List<String> findFoo() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("foo");
		list.add("Foo");
		list.add("Fu");
		return list;
	}
}
