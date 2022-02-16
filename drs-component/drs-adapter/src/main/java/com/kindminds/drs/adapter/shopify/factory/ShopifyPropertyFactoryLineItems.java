package com.kindminds.drs.adapter.shopify.factory;

import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.adapter.shopify.dto.RawShopifyOrderLineItem;
import com.kindminds.drs.adapter.shopify.dto.ShopifyOrderLineItemImpl;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderLineItem;

public class ShopifyPropertyFactoryLineItems extends ShopifyPropertyFactory<List<ShopifyOrderLineItem>> {

	@Override @SuppressWarnings("unchecked")
	public List<ShopifyOrderLineItem> process(Object source) {
		List<RawShopifyOrderLineItem> sourceLineItems = (List<RawShopifyOrderLineItem>)source;
		List<ShopifyOrderLineItem> targetLineItems = new ArrayList<>();
		if(sourceLineItems==null) return targetLineItems;
		for(RawShopifyOrderLineItem rl:sourceLineItems){
			try {
				targetLineItems.add(this.generateObject(rl,ShopifyOrderLineItemImpl.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return targetLineItems;
	}
	
	private <T,S> T generateObject(S sourceObject,Class<T> targetClass) throws Exception{
		return ShopifyObjectFactory.generateObject(sourceObject, targetClass);
	}
	
}