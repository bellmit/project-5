package com.kindminds.drs.adapter.shopify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RawShopifyOrderLineItem {
	
	private String id;
	private String variant_id;
	private String title;
	private String quantity;
	private String price;
	private String grams;
	private String sku;
	private String variant_title;
	private String vendor;
	private String fulfillment_service;
	private String product_id;
	private String requires_shipping;
	private String taxable;
	private String gift_card;
	private String name;
	private String variant_inventory_management;
	private JsonNode properties;
	private String product_exists;
	private String fulfillable_quantity;
	private String total_discount;
	private String fulfillment_status;
	private JsonNode price_set;
	private JsonNode total_discount_set;
	private JsonNode discount_allocations;
	private String admin_graphql_api_id;
	private JsonNode tax_lines;
	private JsonNode origin_location;
	private JsonNode destination_location;
		
	@Override
	public String toString() {
		return "RawShopifyOrderLineItem [getLineItemId()=" + getLineItemId() + ", getVariantId()=" + getVariantId()
				+ ", getTitle()=" + getTitle() + ", getQuantity()=" + getQuantity() + ", getPrice()=" + getPrice()
				+ ", getGrams()=" + getGrams() + ", getSku()=" + getSku() + ", getVariantTitle()=" + getVariantTitle()
				+ ", getVendor()=" + getVendor() + ", getFulfillmentService()=" + getFulfillmentService()
				+ ", getProductId()=" + getProductId() + ", getRequiresShipping()=" + getRequiresShipping()
				+ ", getTaxable()=" + getTaxable() + ", getGiftCard()=" + getGiftCard() + ", getName()=" + getName()
				+ ", getVariantInventoryManagement()=" + getVariantInventoryManagement() + ", getJsonProperties()="
				+ getJsonProperties() + ", getProductExists()=" + getProductExists() + ", getFulfillableQuantity()="
				+ getFulfillableQuantity() + ", getTotalDiscount()=" + getTotalDiscount() + ", getFulfillmentStatus()="
				+ getFulfillmentStatus() + ", getJsonPriceSet()=" + getJsonPriceSet() + ", getJsonTotalDiscountSet()="
				+ getJsonTotalDiscountSet() + ", getJsonDiscountAllocations()=" + getJsonDiscountAllocations()
				+ ", getAdminGraphqlApiId()=" + getAdminGraphqlApiId() + ", getJsonTaxLines()=" + getJsonTaxLines()
				+ ", getJsonOriginLocation()=" + getJsonOriginLocation() + ", getJsonDestinationLocation()="
				+ getJsonDestinationLocation() + "]";
	}

	public String getLineItemId(){
		return this.id;
	}

	public String getVariantId(){
		return this.variant_id;
	}

	public String getTitle(){
		return this.title;
	}

	public String getQuantity(){
		return this.quantity;
	}

	public String getPrice(){
		return this.price;
	}

	public String getGrams(){
		return this.grams;
	}

	public String getSku(){
		return this.sku;
	}

	public String getVariantTitle(){
		return this.variant_title;
	}

	public String getVendor(){
		return this.vendor;
	}

	public String getFulfillmentService(){
		return this.fulfillment_service;
	}

	public String getProductId(){
		return this.product_id;
	}

	public String getRequiresShipping(){
		return this.requires_shipping;
	}

	public String getTaxable(){
		return this.taxable;
	}

	public String getGiftCard(){
		return this.gift_card;
	}

	public String getName(){
		return this.name;
	}

	public String getVariantInventoryManagement(){
		return this.variant_inventory_management;
	}

	public String getJsonProperties(){
		return this.properties==null?null:this.properties.toString();
	}

	public String getProductExists(){
		return this.product_exists;
	}

	public String getFulfillableQuantity(){
		return this.fulfillable_quantity;
	}

	public String getTotalDiscount(){
		return this.total_discount;
	}

	public String getFulfillmentStatus(){
		return this.fulfillment_status;
	}

	public String getJsonPriceSet() {
		return this.price_set==null?null:this.price_set.toString();
	}

	public String getJsonTotalDiscountSet() {
		return this.total_discount_set==null?null:this.total_discount_set.toString();
	}

	public String getJsonDiscountAllocations(){
		return this.discount_allocations==null?null:this.discount_allocations.toString();				
	}
	
	public String getAdminGraphqlApiId(){
		return this.admin_graphql_api_id;				
	}
		
	public String getJsonTaxLines(){
		return this.tax_lines==null?null:this.tax_lines.toString();
	}

	public String getJsonOriginLocation(){
		return this.origin_location==null?null:this.origin_location.toString();
	}

	public String getJsonDestinationLocation(){
		return this.destination_location==null?null:this.destination_location.toString();
	}

}