package com.kindminds.drs.adapter.shopify.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class ShopifyPropertyFactory<T> {
	
	public static ShopifyPropertyFactory<?> createFactory(Field field, Method method) throws Exception {
		ShopifyPropertyType type = ShopifyPropertyType.getType(field);
		if(type==null) type = ShopifyPropertyType.getType(method);
		Class<?> factoryClass = Class.forName(type.getFactoryClassFullName());
		return (ShopifyPropertyFactory<?>)factoryClass.newInstance();
	}
	
	public T createPropertyObject(Object source) {
		if (source == null) return null;
		return process(source);
	}
	
	protected abstract T process(Object source);
	
}