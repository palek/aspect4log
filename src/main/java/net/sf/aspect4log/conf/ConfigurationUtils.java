package net.sf.aspect4log.conf;

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class ConfigurationUtils {
	public static final String DEFAULT_CONFIG_FILE = "aspect4log.xml";

	public static Configuration readConfiguration(URL url) throws ConfigurationException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (Configuration) jaxbUnmarshaller.unmarshal(url);
		} catch (Exception e) {
			throw new ConfigurationException(e.getMessage(), e);
		}
	}

	public static Configuration readConfiguration() throws ConfigurationException {
		URL url = ConfigurationUtils.class.getClassLoader().getResource(DEFAULT_CONFIG_FILE);
		if (url == null) {
			return new Configuration();
		}else{
			return readConfiguration(url);
		}
	}
}
