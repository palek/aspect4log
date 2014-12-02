/*  This file is part of the aspect4log  project.
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License
 as published by the Free Software Foundation; version 2.1
 of the License.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.

 Copyright 2007-2014 Semochkin Vitaliy Evgenevich aka Yilativs
  
 */
package net.sf.aspect4log.conf;

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class LogFormatConfigurationUtils {
	public static final String CONFIG_FILE = "aspect4log.xml";
	public static final String TEST_CONFIG_FILE = "aspect4log-test.xml";

	public static LogFormatConfiguration readConfiguration(URL url) throws LogFormatConfigurationException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(LogFormatConfiguration.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (LogFormatConfiguration) jaxbUnmarshaller.unmarshal(url);
		} catch (Exception e) {
			throw new LogFormatConfigurationException(e.getMessage(), e);
		}
	}

	public static LogFormatConfiguration readConfiguration() throws LogFormatConfigurationException {
		URL url = LogFormatConfigurationUtils.class.getClassLoader().getResource(TEST_CONFIG_FILE);
		if (url == null) {
			url = LogFormatConfigurationUtils.class.getClassLoader().getResource(CONFIG_FILE);
		}
		if (url == null) {
			return new LogFormatConfiguration();
		}else{
			return readConfiguration(url);
		}
	}
}
