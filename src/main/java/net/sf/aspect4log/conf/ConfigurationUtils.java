package net.sf.aspect4log.conf;

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class ConfigurationUtils {
	public static final String DEFAULT_CONFIG_FILE = "aspect4log.xml";

	public static Configuration readConfiguration(String resource) throws ConfigurationException {
		try {
			URL url = ConfigurationUtils.class.getClassLoader().getResource(resource);
			JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (Configuration) jaxbUnmarshaller.unmarshal(url);
		} catch (Exception e) {
			throw new ConfigurationException(e.getMessage(), e);
		}
	}

	public static Configuration readConfiguration() throws ConfigurationException {
		return readConfiguration(DEFAULT_CONFIG_FILE);
	}
}
