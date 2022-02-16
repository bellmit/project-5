package com.kindminds.drs.adapter.shopify.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public enum ShopifyPropertyType {
	
	STRING(                    "String","com.kindminds.drs.adapter.shopify.factory.ShopifyPropertyFactoryString"), 
	INTEGER(                  "Integer","com.kindminds.drs.adapter.shopify.factory.ShopifyPropertyFactoryInteger"),
	CURRENCY(                "Currency","com.kindminds.drs.adapter.shopify.factory.ShopifyPropertyFactoryCurrency"), 
	BOOLEAN(                  "Boolean","com.kindminds.drs.adapter.shopify.factory.ShopifyPropertyFactoryBoolean"), 
	DATE(                        "Date","com.kindminds.drs.adapter.shopify.factory.ShopifyPropertyFactoryDate"), 
	BIGDECIMAL(            "BigDecimal","com.kindminds.drs.adapter.shopify.factory.ShopifyPropertyFactoryBigDecimal"), 
	GETLINEITEMS(        "getLineItems","com.kindminds.drs.adapter.shopify.factory.ShopifyPropertyFactoryLineItems"), 
	GETSHIPPINGLINES("getShippingLines","com.kindminds.drs.adapter.shopify.factory.ShopifyPropertyFactoryShippingLines");
	
	private String keyValue;
	private String factoryClassFullName;
	
	private static final Map<String, ShopifyPropertyType> keyValueToShopifyType = new HashMap<String, ShopifyPropertyType>();
	static { for(ShopifyPropertyType t:ShopifyPropertyType.values()) keyValueToShopifyType.put(t.keyValue,t);};
	
	ShopifyPropertyType(String keyValue,String factoryClass) {
		this.keyValue = keyValue;
		this.factoryClassFullName = factoryClass;
	}
	
	public static ShopifyPropertyType getType(Field field) {
		String fieldTypeSimpleName = field.getType().getSimpleName();
		return keyValueToShopifyType.get(fieldTypeSimpleName);
	}
	
	public static ShopifyPropertyType getType(Method method) {
		ShopifyPropertyType type = keyValueToShopifyType.get(method.getName());
		Assert.notNull(type);
		return type;
	}
	
	public String getFactoryClassFullName() {
		return factoryClassFullName;
	}
}