package net.sf.aspect4log.text;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void isToStringOverriden() {
		Assert.assertFalse(StringUtils.isToStringOverriden(new Object()));
		Assert.assertFalse(StringUtils.isToStringOverriden(new Object[] { 1 }));
		Assert.assertTrue(StringUtils.isToStringOverriden(new String("s")));
		Assert.assertFalse(StringUtils.isToStringOverriden(null));
		Assert.assertTrue(StringUtils.isToStringOverriden(new Integer(1)));

	}
	
	
	
	@Test
	public void testSubstitute(){
		Integer i = 5;
		String s = "s";
		int[] ii = { 1, 2 };
		Object[] args = { i, s, ii };
		
		System.out.println(StringUtils.toString("args are: ${args[1]}, ${args}",  args));
		System.out.println(StringUtils.toString("${${args[0]}}, ${args[2]}",  args));
	}

	
	
	

}

