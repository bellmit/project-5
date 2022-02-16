package com.kindminds.drs.adapter.shopify.factory;

public class ShopifyPropertyFactoryString extends ShopifyPropertyFactory<String> {

	@Override
	public String process(Object source) {
		return source.toString();
	}
	
}