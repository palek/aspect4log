package net.sf.aspect4log.conf;


import org.junit.Assert;
import org.junit.Test;

public class ConfigurationUtilsTest {

	@Test
	public void readConfiguration() {

		Configuration configuration = ConfigurationUtils.readConfiguration();
		
		Assert.assertEquals("\t", configuration.getIndentText());
	}
}
