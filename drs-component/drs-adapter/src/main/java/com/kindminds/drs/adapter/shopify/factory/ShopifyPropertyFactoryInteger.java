package com.kindminds.drs.adapter.shopify.factory;

public class ShopifyPropertyFactoryInteger extends ShopifyPropertyFactory<Integer> {

	@Override
	public Integer process(Object source) {
		return Integer.valueOf(source.toString());
	}
	
}