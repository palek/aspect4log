/*  This file is part of the aspect  project.
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
package net.sf.aspect4log.text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;

public class StringUtils {

	private static final String ELEMENTS_DELITMETER = ", ";
	private static final String MAP_KEY_VALUE_DELIMETER = ":";
	private static final String ARRAY_BEGINS_BRACKET = "{";
	private static final String ARRAY_ENDS_BRACKET = "}";

	private static final String ITERABLE_BEGINS_BRACKET = "<";
	private static final String ITERABLE_ENDS_BRACKET = ">";

	private static final String UNDEFINDED_TO_STRING_VALUE = "¿";
	private static final String NULL_VALUE = "Ø";
	private static final String ERROR_VALUE = "É";

	private String elementsDelitmeter = ELEMENTS_DELITMETER;
	private String mapKeyValueDelimeter = MAP_KEY_VALUE_DELIMETER;
	private String arrayBeginsBracket = ARRAY_BEGINS_BRACKET;
	private String arrayEndsBracket = ARRAY_ENDS_BRACKET;

	private String iterableBeginsBracket = ITERABLE_BEGINS_BRACKET;
	private String iterableEndsBracket = ITERABLE_ENDS_BRACKET;

	private String undefindedToStringValue = UNDEFINDED_TO_STRING_VALUE;
	private String nullValue = NULL_VALUE;
	private String errorValue = ERROR_VALUE;

	public String getElementsDelitmeter() {
		return elementsDelitmeter;
	}

	public void setElementsDelitmeter(String elementsDelitmeter) {
		this.elementsDelitmeter = elementsDelitmeter;
	}

	public String getMapKeyValueDelimeter() {
		return mapKeyValueDelimeter;
	}

	public void setMapKeyValueDelimeter(String mapKeyValueDelimeter) {
		this.mapKeyValueDelimeter = mapKeyValueDelimeter;
	}

	public String getArrayBeginsBracket() {
		return arrayBeginsBracket;
	}

	public void setArrayBeginsBracket(String arrayBeginsBracket) {
		this.arrayBeginsBracket = arrayBeginsBracket;
	}

	public String getArrayEndsBracket() {
		return arrayEndsBracket;
	}

	public void setArrayEndsBracket(String arrayEndsBracket) {
		this.arrayEndsBracket = arrayEndsBracket;
	}

	public String getIterableBeginsBracket() {
		return iterableBeginsBracket;
	}

	public void setIterableBeginsBracket(String iterableBeginsBracket) {
		this.iterableBeginsBracket = iterableBeginsBracket;
	}

	public String getIterableEndsBracket() {
		return iterableEndsBracket;
	}

	public void setIterableEndsBracket(String iterableEndsBracket) {
		this.iterableEndsBracket = iterableEndsBracket;
	}

	public String getUndefindedToStringValue() {
		return undefindedToStringValue;
	}

	public void setUndefindedToStringValue(String undefindedToStringValue) {
		this.undefindedToStringValue = undefindedToStringValue;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public String getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}

	private static Method OBJECT_EQUALS_METHOD_TMP = null;
	static {
		try {
			OBJECT_EQUALS_METHOD_TMP = Object.class.getMethod("toString");
		} catch (NoSuchMethodException | SecurityException e) {
			// that's impossible
			throw new ExceptionInInitializerError(e);
		}
	}
	private static final Method OBJECT_EQUALS_METHOD = OBJECT_EQUALS_METHOD_TMP;

	public String toString(Map<?, ?> map) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Iterator<?> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			toString(stringBuilder, entry.getKey());
			stringBuilder.append(mapKeyValueDelimeter);
			toString(stringBuilder, entry.getValue());
			if (iterator.hasNext()) {
				stringBuilder.append(elementsDelitmeter);
			}
		}
		return stringBuilder.toString();
	}

	public String toString(Iterable<?> iterable) {
		// check if object overrides toString http://stackoverflow.com/questions/12133817/determining-if-a-method-overrides-another-at-runtime
		// type Ø for null. for not implemented toString ¿
		StringBuilder stringBuilder = new StringBuilder();
		;
		for (Iterator<?> iterator = iterable.iterator(); iterator.hasNext();) {
			toString(stringBuilder, iterator.next());
			if (iterator.hasNext()) {
				stringBuilder.append(elementsDelitmeter);
			}
		}
		return stringBuilder.toString();
	}

	public String toString(Object[] array) {
		// check if object overrides toString http://stackoverflow.com/questions/12133817/determining-if-a-method-overrides-another-at-runtime
		// type Ø for null. for not implemented toString ¿
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			toString(stringBuilder, array[i]);
			if (i < array.length - 1) {
				stringBuilder.append(elementsDelitmeter);
			}
		}
		return stringBuilder.toString();
	}

	public StringBuilder toString(StringBuilder stringBuilder, Object o) {
		// TODO add Collection, Map to string implementations
		if (o != null) {
			if (o instanceof Object[]) {
				addArrayBrackets(stringBuilder, toString((Object[]) o));
			} else if (o instanceof boolean[]) {
				addArrayBrackets(stringBuilder, toString(ArrayUtils.toObject((boolean[]) o)));
			} else if (o instanceof char[]) {
				addArrayBrackets(stringBuilder, toString(ArrayUtils.toObject((char[]) o)));
			} else if (o instanceof byte[]) {
				addArrayBrackets(stringBuilder, toString(ArrayUtils.toObject((byte[]) o)));
			} else if (o instanceof short[]) {
				addArrayBrackets(stringBuilder, toString(ArrayUtils.toObject((short[]) o)));
			} else if (o instanceof int[]) {
				addArrayBrackets(stringBuilder, toString(ArrayUtils.toObject((int[]) o)));
			} else if (o instanceof long[]) {
				addArrayBrackets(stringBuilder, toString(ArrayUtils.toObject((long[]) o)));
			} else if (o instanceof float[]) {
				addArrayBrackets(stringBuilder, toString(ArrayUtils.toObject((float[]) o)));
			} else if (o instanceof double[]) {
				addArrayBrackets(stringBuilder, toString(ArrayUtils.toObject((double[]) o)));
			} else if (o instanceof Map<?, ?>) {
				addIterrableBrackets(stringBuilder, toString((Map<?, ?>) o));
			} else if (o instanceof Collection<?>) {
				addIterrableBrackets(stringBuilder, toString(((Iterable<?>) o)));
			} else {
				if (isToStringOverriden(o)) {
					stringBuilder.append(o);
				} else {
					stringBuilder.append(undefindedToStringValue);
				}
			}
		} else {
			stringBuilder.append(nullValue);
		}
		return stringBuilder;
	}

	private void addBrackets(StringBuilder stringBuilder, String beginBracket, String string, String endBracket) {
		stringBuilder.append(beginBracket);
		stringBuilder.append(string);
		stringBuilder.append(endBracket);
	}

	private void addArrayBrackets(StringBuilder stringBuilder, String string) {
		addBrackets(stringBuilder, arrayBeginsBracket, string, arrayEndsBracket);
	}

	private void addIterrableBrackets(StringBuilder stringBuilder, String string) {
		addBrackets(stringBuilder, iterableBeginsBracket, string, iterableEndsBracket);
	}

	public boolean isToStringOverriden(Object obj) {
		if (obj != null) {
			try {
				// TODO consider to cache toString methods for each class, cache should be based on Weak References
				return !obj.getClass().getMethod("toString").equals(OBJECT_EQUALS_METHOD);
			} catch (NoSuchMethodException | SecurityException e) {
				// that's impossible
				return false;
			}
		}
		return false;
	}

	// \$\{(.*)\};
	private static final Pattern VARIABLE_SEARCH_PATTERN = Pattern.compile("(?!\\$\\{\\$\\{)\\$\\{(.*?)\\}");

	public String toString(String template, Object object) {
		if (template.isEmpty()) {
			return "";
		}
		Matcher matcher = VARIABLE_SEARCH_PATTERN.matcher(template);
		StringBuilder stringBuilder = new StringBuilder(template);
		int shift = 0;
		EnclosingBean bean = new EnclosingBean(object);
		while (matcher.find()) {
			String property = matcher.group(1);
			String replacement = toString(bean, property);
			int start = matcher.start();
			int end = matcher.end();
			stringBuilder.replace(start + shift, end + shift, replacement);
			shift = shift + replacement.length() - end + start;
		}
		return stringBuilder.toString();
	}

	private String toString(Object bean, String property) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			toString(stringBuilder, PropertyUtils.getProperty(bean, property));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			stringBuilder.append(errorValue);
		}
		return stringBuilder.toString();

	}

	public static class EnclosingBean {
		private Object object;

		public EnclosingBean(Object object) {
			this.object = object;
		}

		public EnclosingBean() {
		}

		public Object getArgs() {
			return object;
		}

		public Object getResult() {
			return object;
		}

		public Object getException() {
			return object;
		}
	}

}
