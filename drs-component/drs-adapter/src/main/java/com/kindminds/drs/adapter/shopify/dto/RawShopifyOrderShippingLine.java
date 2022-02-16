package com.kindminds.drs.adapter.shopify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RawShopifyOrderShippingLine {
	
	private String id;
	private String title;
	private String price;
	private String code;
	private String source;
	private String phone;
	private String requested_fulfillment_service_id;
	private String delivery_category;
	private String carrier_identifier;
	private String discounted_price;
	private JsonNode price_set;
	private JsonNode discounted_price_set;
	private JsonNode discount_allocations;
	private JsonNode tax_lines;
		
	@Override
	public String toString() {
		return "RawShopifyOrderShippingLine [getShippingLineId()=" + getShippingLineId() + ", getTitle()=" + getTitle()
				+ ", getPrice()=" + getPrice() + ", getCode()=" + getCode() + ", getSource()=" + getSource()
				+ ", getPhone()=" + getPhone() + ", getRequestedFulfillmentServiceId()="
				+ getRequestedFulfillmentServiceId() + ", getDeliveryCategory()=" + getDeliveryCategory()
				+ ", getCarrierIdentifier()=" + getCarrierIdentifier() + ", getDiscountedPrice()="
				+ getDiscountedPrice() + ", getJsonPriceSet()=" + getJsonPriceSet() + ", getJsonDiscountedPriceSet()="
				+ getJsonDiscountedPriceSet() + ", getJsonDiscountAllocations()=" + getJsonDiscountAllocations()
				+ ", getJsonTaxLines()=" + getJsonTaxLines() + "]";
	}

	public String getShippingLineId(){
	    return this.id;
	}

	public String getTitle(){
	    return this.title;
	}

	public String getPrice(){
	    return this.price;
	}

	public String getCode(){
	    return this.code;
	}

	public String getSource(){
	    return this.source;
	}

	public String getPhone(){
	    return this.phone;
	}

	public String getRequestedFulfillmentServiceId(){
	    return this.requested_fulfillment_service_id;
	}

	public String getDeliveryCategory(){
	    return this.delivery_category;
	}

	public String getCarrierIdentifier(){
	    return this.carrier_identifier;
	}
	
	public String getDiscountedPrice() {
		return this.discounted_price;
	}

	public String getJsonPriceSet(){
		return this.price_set==null?null:this.price_set.toString();
	}

	public String getJsonDiscountedPriceSet() {
		return this.discounted_price_set==null?null:this.discounted_price_set.toString();
	}

	public String getJsonDiscountAllocations() {
		return this.discount_allocations==null?null:this.discount_allocations.toString();
	} 
		
	public String getJsonTaxLines(){
	    return this.tax_lines==null?null:this.tax_lines.toString();
	}

}
