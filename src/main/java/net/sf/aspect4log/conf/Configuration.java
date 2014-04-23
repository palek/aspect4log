package net.sf.aspect4log.conf;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.aspect4log.text.CustomisableMessageBuilderFactory;
import net.sf.aspect4log.text.MessageBuilderFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

@XmlRootElement(name="aspect4logConfiguration")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Configuration {

	/**
	 * 
	 * @return specifies if indent should be used for methods called inside marked method. The default value is true.
	 */

	private boolean useIndent = true;

	/**
	 * @return a java.lang.String that represents text of Indent. The default value is 1 tab symbol.
	 */

	private String indentText = "\t";

	private MessageBuilderFactory messageBuilderFactory = new CustomisableMessageBuilderFactory();

	public boolean isUseIndent() {
		return useIndent;
	}

	@XmlElement
	public void setUseIndent(boolean useIndent) {
		this.useIndent = useIndent;
	}

	public String getIndentText() {
		return indentText;
	}

	@XmlElement
	public void setIndentText(String indentText) {
		this.indentText = indentText;
	}

	@XmlTransient
	public void setMessageBuilderFactory(MessageBuilderFactory messageBuilderFactory) {
		this.messageBuilderFactory = messageBuilderFactory;
	}

	public MessageBuilderFactory getMessageBuilderFactory()  {
		return messageBuilderFactory;
	}

	@XmlElement
	public void setMessageBuilderFactoryConfiguration(MessageBuilderFactoryConfiguration messageBuilderFactoryConfiguration) throws InstantiationException, IllegalAccessException, InvocationTargetException, DOMException {
		messageBuilderFactory = messageBuilderFactoryConfiguration.getClazz().newInstance();
		for (Element element : messageBuilderFactoryConfiguration.getProperties()) {
			BeanUtils.setProperty(messageBuilderFactory, element.getTagName(), element.getTextContent());
		}
	}
}

@XmlAccessorType(XmlAccessType.FIELD)
class MessageBuilderFactoryConfiguration {
	@XmlAnyElement
	private List<Element> properties = new ArrayList<Element>();

	@XmlAttribute(name = "class")
	private Class<? extends MessageBuilderFactory> clazz;

	protected List<Element> getProperties() {
		return properties;
	}

	protected Class<? extends MessageBuilderFactory> getClazz() {
		return clazz;
	}

}