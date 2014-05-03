package net.sf.aspect4log.text;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void isToStringOverriden() {
		Assert.assertFalse(new StringUtils().isToStringOverriden(new Object().getClass()));
		Assert.assertFalse(new StringUtils().isToStringOverriden(new Object[] { 1 }.getClass()));
		Assert.assertTrue(new StringUtils().isToStringOverriden(new String("s").getClass()));
		Assert.assertTrue(new StringUtils().isToStringOverriden(new Integer(1).getClass()));
	}
	
	@Test
	public void testSubstitute(){
		Integer i = 5;
		String s = "s";
		int[] ii = { 1, 2 };
		Object[] args = { i, s, ii };
		System.out.println(new StringUtils().toString("args are: ${args[1]}, ${args}",  args));
		System.out.println(new StringUtils().toString("${${args[0]}}, ${args[2]}",  args));
	}

}

