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
	
	public static final String UNDEFINDED_TO_STRING_VALUE = "¿";
	public static final String NULL_VALUE = "Ø";
	public static final String ERROR_VALUE = "É";

	private static Method OBJECT_EQUALS_METHOD_TMP = null;
	static {
		try {
			OBJECT_EQUALS_METHOD_TMP = Object.class.getMethod("toString");
		} catch (NoSuchMethodException | SecurityException e) {
			// that's impossible
			throw new ExceptionInInitializerError("toString mething is not accessible or exist in Object class");
		}
	}
	private static final Method OBJECT_EQUALS_METHOD = OBJECT_EQUALS_METHOD_TMP;


	public static String toString( Map<?,?> map) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Iterator<?> iterator =  map.entrySet().iterator(); iterator.hasNext(); ) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			toString(stringBuilder, entry.getKey());
			stringBuilder.append(MAP_KEY_VALUE_DELIMETER);
			toString(stringBuilder, entry.getValue());
			if (iterator.hasNext()) {
				stringBuilder.append(ELEMENTS_DELITMETER);
			}
		}
		return stringBuilder.toString();
	}
	
	public static String toString(Iterable<?> iterable) {
		// check if object overrides toString http://stackoverflow.com/questions/12133817/determining-if-a-method-overrides-another-at-runtime
		// type Ø for null. for not implemented toString ¿
		StringBuilder stringBuilder = new StringBuilder();
		;
		for (Iterator<?> iterator =  iterable.iterator(); iterator.hasNext(); ) {
			toString(stringBuilder, iterator.next());
			if (iterator.hasNext()) {
				stringBuilder.append(ELEMENTS_DELITMETER);
			}
		}
		return stringBuilder.toString();
	}
	
	public static String toString(Object[] array) {
		// check if object overrides toString http://stackoverflow.com/questions/12133817/determining-if-a-method-overrides-another-at-runtime
		// type Ø for null. for not implemented toString ¿
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			toString(stringBuilder, array[i]);
			if (i < array.length - 1) {
				stringBuilder.append(ELEMENTS_DELITMETER);
			}
		}
		return stringBuilder.toString();
	}
	
	public static StringBuilder toString(StringBuilder stringBuilder, Object o) {
		//TODO add Collection, Map to string implementations
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
			} else if (o instanceof Map<?,?>) {
				addIterrableBrackets(stringBuilder, toString((Map<?,?>) o));
			} else if (o instanceof Collection<?>) {
				addIterrableBrackets(stringBuilder, toString(((Iterable<?>) o)));
			}else {
				if (isToStringOverriden(o)) {
					stringBuilder.append(o);
				} else {
					stringBuilder.append(UNDEFINDED_TO_STRING_VALUE);
				}
			}
		} else {
			stringBuilder.append(NULL_VALUE);
		}
		return stringBuilder;
	}

	private static void addBrackets(StringBuilder stringBuilder, String beginBracket, String string, String endBracket ) {
		stringBuilder.append(beginBracket);
		stringBuilder.append(string);
		stringBuilder.append(endBracket);
	}
	
	private static void addArrayBrackets(StringBuilder stringBuilder, String string) {
		addBrackets(stringBuilder, ARRAY_BEGINS_BRACKET ,string, ARRAY_ENDS_BRACKET);
	}
	
	private static void addIterrableBrackets(StringBuilder stringBuilder, String string) {
		addBrackets(stringBuilder, ITERABLE_BEGINS_BRACKET ,string, ITERABLE_ENDS_BRACKET);
	}
	

	public static boolean isToStringOverriden(Object obj) {
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

	public static String toString(String template, Object object) {
		if(template.isEmpty()){
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

	private static String toString(Object bean, String property) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			toString(stringBuilder, PropertyUtils.getProperty(bean, property));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			stringBuilder.append(ERROR_VALUE);
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
