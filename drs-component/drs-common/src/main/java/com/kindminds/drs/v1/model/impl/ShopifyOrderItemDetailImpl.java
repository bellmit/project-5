package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.sales.ShopifyOrderItemDetail;

import java.math.BigDecimal;

public class ShopifyOrderItemDetailImpl implements ShopifyOrderItemDetail {

	private String shippingMethod;
	private String shippingCountry;
	private String shippingState;
	private String shippingCity;
	private String shippingPostalCode;
	private String fulfillmentService;
	private String tax;
	private String totalDiscount;
		
	@Override
	public String toString() {
		return "ShopifyOrderItemDetailImpl [getShippingMethod()=" + getShippingMethod() + ", getShippingCountry()="
				+ getShippingCountry() + ", getShippingState()=" + getShippingState() + ", getShippingCity()="
				+ getShippingCity() + ", getShippingPostalCode()=" + getShippingPostalCode()
				+ ", getFulfillmentService()=" + getFulfillmentService() + ", getTax()=" + getTax()
				+ ", getTotalDiscount()=" + getTotalDiscount() + "]";
	}

	public ShopifyOrderItemDetailImpl(
			String shippingMethod,
			String shippingCountry,
			String shippingState,
			String shippingCity,
			String shippingPostalCode,
			String fulfillmentService,
			String tax,
			String totalDiscount
			){
		this.shippingMethod = shippingMethod;
		this.shippingCountry = shippingCountry;
		this.shippingState = shippingState;
		this.shippingCity = shippingCity;
		this.shippingPostalCode = shippingPostalCode;
		this.fulfillmentService = fulfillmentService;
		this.tax = tax;
		this.totalDiscount = totalDiscount;
	}
		
	@Override
	public String getShippingMethod() {
		return this.shippingMethod;
	}

	@Override
	public String getShippingCountry() {
		return this.shippingCountry;
	}

	@Override
	public String getShippingState() {
		return this.shippingState;
	}

	@Override
	public String getShippingCity() {
		return this.shippingCity;
	}

	@Override
	public String getShippingPostalCode() {		
		return this.shippingPostalCode;
	}

	@Override
	public String getFulfillmentService() {		
		return this.fulfillmentService;
	}
	
	@Override
	public String getTax() {
		return this.tax==null?"": new BigDecimal(this.tax).setScale(2).toPlainString();
	}

	@Override
	public String getTotalDiscount() {		
		return this.totalDiscount==null?"":new BigDecimal(this.totalDiscount).setScale(2).toPlainString();
	}
	
}