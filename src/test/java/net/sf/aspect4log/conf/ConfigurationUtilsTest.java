package net.sf.aspect4log.conf;


import net.sf.aspect4log.text.CustomisableMessageBuilderFactory;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationUtilsTest {

	@Test
	public void readConfiguration() {

		Configuration configuration = ConfigurationUtils.readConfiguration();
		
		Assert.assertEquals("\t", configuration.getIndentText());
		Assert.assertEquals(CustomisableMessageBuilderFactory.class, configuration.getMessageBuilderFactory().getClass());
	}
}
