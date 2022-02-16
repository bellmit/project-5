package com.kindminds.drs.adapter.shopify.factory;

import java.util.Date;

import com.kindminds.drs.util.DateHelper;

public class ShopifyPropertyFactoryDate extends ShopifyPropertyFactory<Date> {
	
	private final String dateFormat="yyyy-MM-dd'T'HH:mm:ssX";

	@Override
	public Date process(Object source) {
		
		return DateHelper.toDate(source.toString(),this.dateFormat);
	}
	
}