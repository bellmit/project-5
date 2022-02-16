package com.kindminds.drs.adapter.shopify.factory;

import com.kindminds.drs.Currency;

public class ShopifyPropertyFactoryCurrency extends ShopifyPropertyFactory<Currency> {

	@Override
	public Currency process(Object source) {
		return Currency.valueOf(source.toString());
	}
	
}