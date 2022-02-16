package com.kindminds.drs.adapter.shopify.dto;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RawShopifyOrder {
	
	private String id;
	private String email;
	private String closed_at;
	private String created_at;
	private String updated_at;
	private String number;
	private String note;
	private String token;
	private String gateway;
	private String test;
	private String total_price;
	private String subtotal_price;
	private String total_weight;
	private String total_tax;
	private String taxes_included;
	private String currency;
	private String financial_status;
	private String confirmed;
	private String total_discounts;
	private String total_line_items_price;
	private String cart_token;
	private String buyer_accepts_marketing;
	private String name;
	private String referring_site;
	private String landing_site;
	private String cancelled_at;
	private String cancel_reason;
	private String total_price_usd;
	private String checkout_token;
	private String reference;
	private String user_id;
	private String location_id;
	private String source_identifier;
	private String source_url;
	private String processed_at;
	private String device_id;
	private String phone;
	private String customer_locale;
	private String app_id;
	private String browser_ip;
	private String landing_site_ref;
	private String order_number;
	private JsonNode discount_applications;
	private JsonNode discount_codes;
	private JsonNode note_attributes;
	private JsonNode payment_gateway_names;
	private String processing_method;
	private String checkout_id;
	private String source_name;
	private String fulfillment_status;
	private JsonNode tax_lines;
	private String tags;
	private String contact_email;
	private String order_status_url;
	private String presentment_currency;
	private JsonNode total_line_items_price_set;
	private JsonNode total_discounts_set;
	private JsonNode total_shipping_price_set;
	private JsonNode subtotal_price_set;
	private JsonNode total_price_set;
	private JsonNode total_tax_set;
	private String total_tip_received;
	private String admin_graphql_api_id;
	private List<RawShopifyOrderLineItem> line_items;
	private List<RawShopifyOrderShippingLine> shipping_lines;
	private JsonNode billing_address;
	private JsonNode shipping_address;
	private JsonNode fulfillments;
	private JsonNode client_details;
	private JsonNode refunds;
	private JsonNode payment_details;
	private JsonNode customer;
	private String current_subtotal_price;
	private JsonNode current_subtotal_price_set;
	private JsonNode current_total_discounts;
	private JsonNode current_total_discounts_set;
	private JsonNode current_total_duties_set;
	private String current_total_price;
	private JsonNode current_total_price_set;
	private JsonNode current_total_tax;
	private JsonNode current_total_tax_set;
	private JsonNode original_total_duties_set;
	private JsonNode total_outstanding;


	@Override
	public String toString() {
		return "RawShopifyOrder [getOrderId()=" + getOrderId() + ", getEmail()=" + getEmail() + ", getClosedAt()="
				+ getClosedAt() + ", getCreatedAt()=" + getCreatedAt() + ", getUpdatedAt()=" + getUpdatedAt()
				+ ", getNumber()=" + getNumber() + ", getNote()=" + getNote() + ", getToken()=" + getToken()
				+ ", getGateway()=" + getGateway() + ", getTest()=" + getTest() + ", getTotalPrice()=" + getTotalPrice()
				+ ", getSubtotalPrice()=" + getSubtotalPrice() + ", getTotalWeight()=" + getTotalWeight()
				+ ", getTotalTax()=" + getTotalTax() + ", getTaxesIncluded()=" + getTaxesIncluded() + ", getCurrency()="
				+ getCurrency() + ", getFinancialStatus()=" + getFinancialStatus() + ", getConfirmed()="
				+ getConfirmed() + ", getTotalDiscounts()=" + getTotalDiscounts() + ", getTotalLineItemsPrice()="
				+ getTotalLineItemsPrice() + ", getCartToken()=" + getCartToken() + ", getBuyerAcceptsMarketing()="
				+ getBuyerAcceptsMarketing() + ", getName()=" + getName() + ", getReferringSite()=" + getReferringSite()
				+ ", getLandingSite()=" + getLandingSite() + ", getCancelledAt()=" + getCancelledAt()
				+ ", getCancelReason()=" + getCancelReason() + ", getTotalPriceUsd()=" + getTotalPriceUsd()
				+ ", getCheckoutToken()=" + getCheckoutToken() + ", getReference()=" + getReference() + ", getUserId()="
				+ getUserId() + ", getLocationId()=" + getLocationId() + ", getSourceIdentifier()="
				+ getSourceIdentifier() + ", getSourceUrl()=" + getSourceUrl() + ", getProcessedAt()="
				+ getProcessedAt() + ", getDeviceId()=" + getDeviceId() + ", getPhone()=" + getPhone()
				+ ", getCustomerLocale()=" + getCustomerLocale() + ", getAppId()=" + getAppId() + ", getBrowserIp()="
				+ getBrowserIp() + ", getLandingSiteRef()=" + getLandingSiteRef() + ", getOrderNumber()="
				+ getOrderNumber() + ", getJsonDiscountApplications()=" + getJsonDiscountApplications()
				+ ", getJsonDiscountCodes()=" + getJsonDiscountCodes() + ", getJsonNoteAttributes()="
				+ getJsonNoteAttributes() + ", getJsonPaymentGatewayNames()=" + getJsonPaymentGatewayNames()
				+ ", getProcessingMethod()=" + getProcessingMethod() + ", getCheckoutId()=" + getCheckoutId()
				+ ", getSourceName()=" + getSourceName() + ", getFulfillmentStatus()=" + getFulfillmentStatus()
				+ ", getJsonTaxLines()=" + getJsonTaxLines() + ", getTags()=" + getTags() + ", getContactEmail()="
				+ getContactEmail() + ", getOrderStatusUrl()=" + getOrderStatusUrl() + ", getPresentmentCurrency()="
				+ getPresentmentCurrency() + ", getJsonTotalLineItemsPriceSet()=" + getJsonTotalLineItemsPriceSet()
				+ ", getJsonTotalDiscountsSet()=" + getJsonTotalDiscountsSet() + ", getJsonTotalShippingPriceSet()=" + getJsonTotalShippingPriceSet()
				+ ", getJsonSubtotalPriceSet()=" + getJsonSubtotalPriceSet() + ", getJsonTotalPriceSet()=" + getJsonTotalPriceSet()
				+ ", getJsonTotalTaxSet()=" + getJsonTotalTaxSet() + ", getTotalTipReceived()="
				+ getTotalTipReceived() + ", getAdminGraphqlApiId()=" + getAdminGraphqlApiId() + ", getLineItems()="
				+ getLineItems() + ", getShippingLines()=" + getShippingLines() + ", getJsonBillingAddress()="
				+ getJsonBillingAddress() + ", getJsonShippingAddress()=" + getJsonShippingAddress()
				+ ", getJsonFulfillments()=" + getJsonFulfillments() + ", getJsonClientDetails()="
				+ getJsonClientDetails() + ", getJsonRefunds()=" + getJsonRefunds() + ", getJsonPaymentDetails()="
				+ getJsonPaymentDetails() + ", getJsonCustomer()=" + getJsonCustomer() + "]";
	}

	public String getOrderId(){
	    return this.id;
	}

	public String getEmail(){
	    return this.email;
	}

	public String getClosedAt(){
	    return this.closed_at;
	}

	public String getCreatedAt(){
	    return this.created_at;
	}

	public String getUpdatedAt(){
	    return this.updated_at;
	}

	public String getNumber(){
	    return this.number;
	}

	public String getNote(){
	    return this.note;
	}

	public String getToken(){
	    return this.token;
	}

	public String getGateway(){
	    return this.gateway;
	}

	public String getTest(){
	    return this.test;
	}

	public String getTotalPrice(){
	    return this.total_price;
	}

	public String getSubtotalPrice(){
	    return this.subtotal_price;
	}

	public String getTotalWeight(){
	    return this.total_weight;
	}

	public String getTotalTax(){
	    return this.total_tax;
	}

	public String getTaxesIncluded(){
	    return this.taxes_included;
	}

	public String getCurrency(){
	    return this.currency;
	}

	public String getFinancialStatus(){
	    return this.financial_status;
	}

	public String getConfirmed(){
	    return this.confirmed;
	}

	public String getTotalDiscounts(){
	    return this.total_discounts;
	}

	public String getTotalLineItemsPrice(){
	    return this.total_line_items_price;
	}

	public String getCartToken(){
	    return this.cart_token;
	}

	public String getBuyerAcceptsMarketing(){
	    return this.buyer_accepts_marketing;
	}

	public String getName(){
	    return this.name;
	}

	public String getReferringSite(){
	    return this.referring_site;
	}

	public String getLandingSite(){
	    return this.landing_site;
	}

	public String getCancelledAt(){
	    return this.cancelled_at;
	}

	public String getCancelReason(){
	    return this.cancel_reason;
	}

	public String getTotalPriceUsd(){
	    return this.total_price_usd;
	}

	public String getCheckoutToken(){
	    return this.checkout_token;
	}

	public String getReference(){
	    return this.reference;
	}

	public String getUserId(){
	    return this.user_id;
	}

	public String getLocationId(){
	    return this.location_id;
	}

	public String getSourceIdentifier(){
	    return this.source_identifier;
	}

	public String getSourceUrl(){
	    return this.source_url;
	}

	public String getProcessedAt(){
	    return this.processed_at;
	}

	public String getDeviceId(){
	    return this.device_id;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getCustomerLocale(){
		return this.customer_locale;
	}
	
	public String getAppId(){
		return this.app_id;
	}
	
	public String getBrowserIp(){
	    return this.browser_ip;
	}

	public String getLandingSiteRef(){
	    return this.landing_site_ref;
	}

	public String getOrderNumber(){
	    return this.order_number;
	}

	public String getJsonDiscountApplications(){
		return this.discount_applications==null?null:this.discount_applications.toString();		
	}
	
	public String getJsonDiscountCodes(){
	    return this.discount_codes==null?null:this.discount_codes.toString();
	}

	public String getJsonNoteAttributes(){
	    return this.note_attributes==null?null:this.note_attributes.toString();
	}

	public String getJsonPaymentGatewayNames(){
	    return this.payment_gateway_names==null?null:this.payment_gateway_names.toString();
	}

	public String getProcessingMethod(){
	    return this.processing_method;
	}

	public String getCheckoutId(){
	    return this.checkout_id;
	}

	public String getSourceName(){
	    return this.source_name;
	}

	public String getFulfillmentStatus(){
	    return this.fulfillment_status;
	}

	public String getJsonTaxLines(){
	    return this.tax_lines==null?null:this.tax_lines.toString();
	}

	public String getTags(){
	    return this.tags;
	}

	public String getContactEmail(){
	    return this.contact_email;
	}

	public String getOrderStatusUrl(){
	    return this.order_status_url;
	}

	public String getPresentmentCurrency(){ return this.presentment_currency; };

	public String getJsonTotalLineItemsPriceSet(){
		return this.total_line_items_price_set==null?null:this.total_line_items_price_set.toString();
	};

	public String getJsonTotalDiscountsSet(){
		return this.total_discounts_set==null?null:this.total_discounts_set.toString();
	};

	public String getJsonTotalShippingPriceSet(){
		return this.total_shipping_price_set==null?null:this.total_shipping_price_set.toString();
	};

	public String getJsonSubtotalPriceSet(){
		return this.subtotal_price_set==null?null:this.subtotal_price_set.toString();
	};

	public String getJsonTotalPriceSet(){
		return this.total_price_set==null?null:this.total_price_set.toString();
	};

	public String getJsonTotalTaxSet(){
		return this.total_tax_set==null?null:this.total_tax_set.toString();
	};

	public String getTotalTipReceived() { return this.total_tip_received; }

	public String getAdminGraphqlApiId(){
		return this.admin_graphql_api_id;		
	}
		
	public List<RawShopifyOrderLineItem> getLineItems(){
	    return this.line_items;
	}

	public List<RawShopifyOrderShippingLine> getShippingLines(){
	    return this.shipping_lines;
	}

	public String getJsonBillingAddress(){
	    return this.billing_address==null?null:this.billing_address.toString();
	}

	public String getJsonShippingAddress(){
	    return this.shipping_address==null?null:this.shipping_address.toString();
	}

	public String getJsonFulfillments(){
	    return this.fulfillments==null?null:this.fulfillments.toString();
	}

	public String getJsonClientDetails(){
	    return this.client_details==null?null:this.client_details.toString();
	}

	public String getJsonRefunds(){
	    return this.refunds==null?null:this.refunds.toString();
	}

	public String getJsonPaymentDetails(){
	    return this.payment_details==null?null:this.payment_details.toString();
	}

	public String getJsonCustomer(){
	    return this.customer==null?null:this.customer.toString();
	}

}
