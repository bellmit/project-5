package com.kindminds.drs.api.adapter;

import java.util.List;

import com.kindminds.drs.api.v1.model.shopify.ShopifyOrder;

public interface ShopifyAdapter {
	
	public enum ShopifyOrderStatus{
		OPEN("open"),
		CLOSED("closed"),
		CANCELLED("cancelled"),
		ANY("any");
		private String value;
		private ShopifyOrderStatus(String statusValue) {
			this.value = statusValue;
		}
		public String getValue() {
			return value;
		}
	}
	
	ShopifyOrder getOrder();
	List<ShopifyOrder> requestOrders(String updatedAtMin,String updatedAtMax,ShopifyOrderStatus status) throws Exception;
	// For Test
	ShopifyOrder getTestOrder();
}
