package com.kindminds.drs.adapter.shopify.factory;

import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.adapter.shopify.dto.RawShopifyOrderShippingLine;
import com.kindminds.drs.adapter.shopify.dto.ShopifyOrderShippingLineImpl;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderShippingLine;

public class ShopifyPropertyFactoryShippingLines extends ShopifyPropertyFactory<List<ShopifyOrderShippingLine>> {

	@Override @SuppressWarnings("unchecked")
	public List<ShopifyOrderShippingLine> process(Object s) {
		List<RawShopifyOrderShippingLine> sourceShippingLines = (List<RawShopifyOrderShippingLine>)s;
		List<ShopifyOrderShippingLine> targetShippingLines = new ArrayList<>();
		if(sourceShippingLines==null) return targetShippingLines;
		for(RawShopifyOrderShippingLine sourceShippingLine:sourceShippingLines){
			try {
				targetShippingLines.add(this.generateObject(sourceShippingLine,ShopifyOrderShippingLineImpl.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return targetShippingLines;
	}
	
	private <T,S> T generateObject(S sourceObject,Class<T> targetClass) throws Exception{
		return ShopifyObjectFactory.generateObject(sourceObject, targetClass);
	}
	
}