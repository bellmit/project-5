package com.kindminds.drs.adapter.shopify.dto;

import java.math.BigDecimal;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderShippingLine;

public class ShopifyOrderShippingLineImpl implements ShopifyOrderShippingLine {
	
	private String shippingLineId;
	private String title;
	private BigDecimal price;
	private String code;
	private String source;
	private String phone;
	private String requestedFulfillmentServiceId;
	private String deliveryCategory;
	private String carrierIdentifier;
	private BigDecimal discountedPrice;
	private String jsonPriceSet;
	private String jsonDiscountedPriceSet;
	private String jsonDiscountAllocations;
	private String jsonTaxLines;
	
	public ShopifyOrderShippingLineImpl() {}
	
	public ShopifyOrderShippingLineImpl(RawShopifyOrderShippingLine rawShopifyOrderShippingLine){
		this.shippingLineId = rawShopifyOrderShippingLine.getShippingLineId();
		this.title = rawShopifyOrderShippingLine.getTitle();
		this.price = new BigDecimal(rawShopifyOrderShippingLine.getPrice());
		this.code = rawShopifyOrderShippingLine.getCode();
		this.source = rawShopifyOrderShippingLine.getSource();
		this.phone = rawShopifyOrderShippingLine.getPhone();
		this.requestedFulfillmentServiceId = rawShopifyOrderShippingLine.getRequestedFulfillmentServiceId();
		this.deliveryCategory = rawShopifyOrderShippingLine.getDeliveryCategory();
		this.carrierIdentifier = rawShopifyOrderShippingLine.getCarrierIdentifier();
		this.discountedPrice = new BigDecimal(rawShopifyOrderShippingLine.getDiscountedPrice());
		this.jsonPriceSet = rawShopifyOrderShippingLine.getJsonPriceSet();
		this.jsonDiscountedPriceSet = rawShopifyOrderShippingLine.getJsonDiscountedPriceSet();
		this.jsonDiscountAllocations = rawShopifyOrderShippingLine.getJsonDiscountAllocations();
		this.jsonTaxLines = rawShopifyOrderShippingLine.getJsonTaxLines();
	}

	@Override
	public String toString() {
		return "ShopifyOrderShippingLineImpl [getShippingLineId()=" + getShippingLineId() + ", getTitle()=" + getTitle()
				+ ", getPrice()=" + getPrice() + ", getCode()=" + getCode() + ", getSource()=" + getSource()
				+ ", getPhone()=" + getPhone() + ", getRequestedFulfillmentServiceId()="
				+ getRequestedFulfillmentServiceId() + ", getDeliveryCategory()=" + getDeliveryCategory()
				+ ", getCarrierIdentifier()=" + getCarrierIdentifier() + ", getDiscountedPrice()="
				+ getDiscountedPrice() + ", getJsonPriceSet()=" + getJsonPriceSet() + ", getJsonDiscountedPriceSet()="
				+ getJsonDiscountedPriceSet() + ", getJsonDiscountAllocations()=" + getJsonDiscountAllocations()
				+ ", getJsonTaxLines()=" + getJsonTaxLines() + "]";
	}

	@Override
	public String getShippingLineId() {
		return this.shippingLineId;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public BigDecimal getPrice() {
		return this.price;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getSource() {
		return this.source;
	}

	@Override
	public String getPhone() {
		return this.phone;
	}

	@Override
	public String getRequestedFulfillmentServiceId() {
		return this.requestedFulfillmentServiceId;
	}

	@Override
	public String getDeliveryCategory() {
		return this.deliveryCategory;
	}

	@Override
	public String getCarrierIdentifier() {
		return this.carrierIdentifier;
	}

	@Override
	public BigDecimal getDiscountedPrice() {
		return this.discountedPrice;
	}

	@Override
	public String getJsonPriceSet() { return this.jsonPriceSet; }

	@Override
	public String getJsonDiscountedPriceSet() { return this.jsonDiscountedPriceSet; }

	@Override
	public String getJsonDiscountAllocations() {
		return this.jsonDiscountAllocations;
	}
	
	@Override
	public String getJsonTaxLines() {
		return this.jsonTaxLines;
	}

}
