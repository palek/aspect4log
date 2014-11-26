package net.sf.aspect4log.conf;


import net.sf.aspect4log.text.CustomisableMessageBuilderFactory;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationUtilsTest {

	@Test
	public void readConfiguration() {

		LogFormatConfiguration logFormatConfiguration = LogFormatConfigurationUtils.readConfiguration();
		
		Assert.assertEquals(CustomisableMessageBuilderFactory.class, logFormatConfiguration.getMessageBuilderFactory().getClass());
	}
}
