package com.kindminds.drs.adapter.shopify.factory;

public class ShopifyPropertyFactoryBoolean extends ShopifyPropertyFactory<Boolean> {

	@Override
	public Boolean process(Object source) {
		return Boolean.valueOf(source.toString());
	}
	
}