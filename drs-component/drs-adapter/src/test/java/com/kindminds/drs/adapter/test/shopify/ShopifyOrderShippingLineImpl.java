package com.kindminds.drs.adapter.test.shopify;

import java.math.BigDecimal;

import com.kindminds.drs.adapter.test.util.TestUtil;
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

	public ShopifyOrderShippingLineImpl(
			String shippingLineId,
			String title,
			String price,
			String code,
			String source,
			String phone,
			String requestedFulfillmentServiceId,
			String deliveryCategory,
			String carrierIdentifier,
			String jsonPriceSet,
			String jsonDiscountedPriceSet,
			String jsonDiscountAllocations,
			String jsonTaxLines){
		this.shippingLineId=shippingLineId;
		this.title=title;
		this.price=new BigDecimal(price);
		this.code=code;
		this.source=source;
		this.phone=phone;
		this.requestedFulfillmentServiceId=requestedFulfillmentServiceId;
		this.deliveryCategory=deliveryCategory;
		this.carrierIdentifier=carrierIdentifier;
		this.jsonPriceSet=jsonPriceSet;
		this.jsonDiscountedPriceSet=jsonDiscountedPriceSet;
		this.jsonDiscountAllocations=jsonDiscountAllocations;
		this.jsonTaxLines=jsonTaxLines;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ShopifyOrderShippingLine){
			ShopifyOrderShippingLine sosl = (ShopifyOrderShippingLine)obj;
			return this.getShippingLineId().equals(sosl.getShippingLineId())
				&& this.getTitle().equals(sosl.getTitle())
				&& this.getPrice().equals(sosl.getPrice())
				&& this.getCode().equals(sosl.getCode())
				&& this.getSource().equals(sosl.getSource())
				&& TestUtil.nullableEquals(this.getPhone(),sosl.getPhone())
				&& TestUtil.nullableEquals(this.getRequestedFulfillmentServiceId(),sosl.getRequestedFulfillmentServiceId())
				&& TestUtil.nullableEquals(this.getDeliveryCategory(),sosl.getDeliveryCategory())
				&& TestUtil.nullableEquals(this.getCarrierIdentifier(),sosl.getCarrierIdentifier())
				&& TestUtil.nullableEquals(this.getDiscountedPrice(),sosl.getDiscountedPrice())
				&& this.getJsonPriceSet().equals(sosl.getJsonPriceSet())
				&& this.getJsonDiscountedPriceSet().equals(sosl.getJsonDiscountedPriceSet())
				&& this.getJsonDiscountAllocations().equals(sosl.getJsonDiscountAllocations())
				&& this.getJsonTaxLines().equals(sosl.getJsonTaxLines());
		}
		return false;
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
