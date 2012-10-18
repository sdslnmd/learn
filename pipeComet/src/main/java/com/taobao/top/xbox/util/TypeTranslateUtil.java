package com.taobao.top.xbox.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TypeTranslateUtil {
	
	private final  static Log log  = LogFactory.getLog(TypeTranslateUtil.class);

	public static Object translate(Class<?> type, String valueStr) {

		if(type.equals(String.class)){
			return valueStr;
		}else if (type.isPrimitive()) {
			return translatePrimitive(type, valueStr);
		}else if (type.isArray()) {
			return translateArray(type,valueStr);
		}else{
			return translateNormal(type, valueStr);
		}

	}

	private static Object translatePrimitive(Class<?> type, String valueStr) {

		Class<?> newType = swPrimitiveType(type);
		Object object  = translateNormal(newType, valueStr);
		Method method = null;
		try {
			method = newType.getMethod(type.getName()+"Value");
			return method.invoke(object);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	private static Object translateArray(Class<?> type, String valueStr) {

		Class<?> componentType = type.getComponentType();
		String[] values = valueStr.split(",");
		Object objects =  Array.newInstance(componentType, values.length);
		for (int i = 0; i < values.length; i++) {
			Array.set(objects, i, translate(componentType,values[i]));
		}
		return objects;

	}

	private static Object translateNormal(Class<?> type, String valueStr) {
		Constructor<?> constructor=null;
		Object o = null;
		try {
			constructor = type.getConstructor(String.class);
			o = constructor.newInstance(valueStr);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		} 
		return o;
	}

	private static Class<?> swPrimitiveType(Class<?> type) {
		char upperChar = Character.toUpperCase(type.getName().charAt(0));
		String typeStr = upperChar + type.getName().substring(1);
		if (type.equals(int.class)) {
			typeStr = typeStr + "eger";
		}
		try {
			type = Class.forName("java.lang." + typeStr);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(),e);
		}
		return type;
	}

}
