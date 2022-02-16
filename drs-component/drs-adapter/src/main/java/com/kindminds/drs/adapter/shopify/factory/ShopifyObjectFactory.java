package com.kindminds.drs.adapter.shopify.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.util.Assert;

public class ShopifyObjectFactory {
	
	public static <T,S> T generateObject(S sourceObject,Class<T> targetClass) throws Exception{
		T targetObject = targetClass.newInstance();
		for (Method sourceMethod:sourceObject.getClass().getDeclaredMethods()){
			if(sourceMethod.getName().equals("toString")) continue;
			String targetFieldName = getTargetFieldName(sourceMethod.getName());
			Field targetField = targetClass.getDeclaredField(targetFieldName);
			targetField.setAccessible(true);
			targetField.set(targetObject,ShopifyPropertyFactory.createFactory(targetField, sourceMethod).createPropertyObject(sourceMethod.invoke(sourceObject)));
		}
		return targetObject;
	}
	
	private static String getTargetFieldName(String sourceGetterName){
		Assert.isTrue(sourceGetterName.startsWith("get"));
		String targetFieldName = sourceGetterName.substring(3);
		return Character.toLowerCase(targetFieldName.charAt(0)) + targetFieldName.substring(1);
	}
	
}
