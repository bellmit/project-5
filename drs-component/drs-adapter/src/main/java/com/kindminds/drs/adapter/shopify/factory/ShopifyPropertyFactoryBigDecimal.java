package com.kindminds.drs.adapter.shopify.factory;

import java.math.BigDecimal;

public class ShopifyPropertyFactoryBigDecimal extends ShopifyPropertyFactory<BigDecimal> {

	@Override
	public BigDecimal process(Object source) {
		return new BigDecimal(source.toString());
	}
	
}