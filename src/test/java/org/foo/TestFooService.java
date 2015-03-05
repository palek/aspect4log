package org.foo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.foo.config.FooConfiguration;
import org.foo.service.FooService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FooConfiguration.class)
public class TestFooService {

	@Autowired
	private FooService fooService;

	
	@Test
	public void testArgsOutOfBounds() {
		fooService.doFoo();
	}
	
	@Test
	public void isFoo() {
		Assert.assertTrue(fooService.isFoo("foo"));
	}
	
	

	@Test
	public void notFoo() {
		Assert.assertFalse(fooService.isFoo("123"));
	}

	@Test
	public void npe() {
		try {
			Assert.assertFalse(fooService.isFoo(null));
			Assert.fail();
		} catch (NullPointerException e) {
			//expected
		}

	}

	@Test
	public void testOneIsFoo() {
		Assert.assertTrue(fooService.isAtLeastOneFoo("boo", "foo",new Object()));
		
		List<String> list = new ArrayList<String>();
		list.add("l1");
		list.add("l2");
		
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("key1", "value1");
		map.put(new Integer(1), new Thread());
		Assert.assertFalse(fooService.isAtLeastOneFoo(new ToStringBreaker(),"boo", new Object(),list,map));
	}

	@Test
	public void testSaveFoo() {
		fooService.saveFoo("foo");
		fooService.saveFoo("Foo");
		Assert.assertEquals(2,fooService.findFoo().size());
		try {
			fooService.saveFoo("moo");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
		Assert.assertEquals(2,fooService.findFoo().size());
	}
	
	@Test
	public void testMap(){
		fooService.getMap();
	}
	
}

class ToStringBreaker{

	@Override
	public String toString() {
		
		throw new RuntimeException("Exception in toString()");
	}
	
}
