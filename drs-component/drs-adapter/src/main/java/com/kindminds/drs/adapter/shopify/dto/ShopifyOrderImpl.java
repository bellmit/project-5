package com.kindminds.drs.adapter.shopify.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.adapter.ShopifyConstants;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrder;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderLineItem;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderShippingLine;
import com.kindminds.drs.util.DateHelper;

public class ShopifyOrderImpl implements ShopifyOrder {
	
	private String orderId;
	private String email;
	private Date closedAt;
	private Date createdAt;
	private Date updatedAt;
	private Integer number;
	private String note;
	private String token;
	private String gateway;
	private Boolean test;
	private BigDecimal totalPrice;
	private BigDecimal subtotalPrice;
	private BigDecimal totalWeight;
	private BigDecimal totalTax;
	private Boolean taxesIncluded;
	private Currency currency;
	private String financialStatus;
	private Boolean confirmed;
	private BigDecimal totalDiscounts;
	private BigDecimal totalLineItemsPrice;
	private String cartToken;
	private Boolean buyerAcceptsMarketing;
	private String name;
	private String referringSite;
	private String landingSite;
	private Date cancelledAt;
	private String cancelReason;
	private BigDecimal totalPriceUsd;
	private String checkoutToken;
	private String reference;
	private String userId;
	private String locationId;
	private String sourceIdentifier;
	private String sourceUrl;
	private Date processedAt;
	private String deviceId;
	private String phone;
	private String customerLocale;
	private String appId;
	private String browserIp;
	private String landingSiteRef;
	private Integer orderNumber;
	private String jsonDiscountApplications;
	private String jsonDiscountCodes;
	private String jsonNoteAttributes;
	private String jsonPaymentGatewayNames;
	private String processingMethod;
	private String checkoutId;
	private String sourceName;
	private String fulfillmentStatus;
	private String jsonTaxLines;
	private String tags;
	private String contactEmail;
	private String orderStatusUrl;
	private String presentmentCurrency;
	private String jsonTotalLineItemsPriceSet;
	private String jsonTotalDiscountsSet;
	private String jsonTotalShippingPriceSet;
	private String jsonSubtotalPriceSet;
	private String jsonTotalPriceSet;
	private String jsonTotalTaxSet;
	private BigDecimal totalTipReceived;
	private String adminGraphqlApiId;
	private List<ShopifyOrderLineItem> lineItems;
	private List<ShopifyOrderShippingLine> shippingLines;
	private String jsonBillingAddress;
	private String jsonShippingAddress;
	private String jsonFulfillments;
	private String jsonClientDetails;
	private String jsonRefunds;
	private String jsonPaymentDetails;
	private String jsonCustomer;
	
	public ShopifyOrderImpl(){}
	
	public ShopifyOrderImpl(RawShopifyOrder rawOrder) {
		this.orderId = rawOrder.getOrderId();
		this.email = rawOrder.getEmail();
		this.closedAt = DateHelper.toDate(rawOrder.getClosedAt(), ShopifyConstants.dateFormat);
		this.createdAt = DateHelper.toDate(rawOrder.getCreatedAt(), ShopifyConstants.dateFormat);
		this.updatedAt = DateHelper.toDate(rawOrder.getUpdatedAt(), ShopifyConstants.dateFormat);
		this.number = Integer.valueOf(rawOrder.getNumber());
		this.note = rawOrder.getNote();
		this.token = rawOrder.getToken();
		this.gateway = rawOrder.getGateway();
		this.test = Boolean.valueOf(rawOrder.getTest());
		this.totalPrice = new BigDecimal(rawOrder.getTotalPrice());
		this.subtotalPrice = new BigDecimal(rawOrder.getSubtotalPrice());
		this.totalWeight = new BigDecimal(rawOrder.getTotalWeight());
		this.totalTax = new BigDecimal(rawOrder.getTotalTax());
		this.taxesIncluded = Boolean.valueOf(rawOrder.getTaxesIncluded());
		this.currency = Currency.valueOf(rawOrder.getCurrency());
		this.financialStatus = rawOrder.getFinancialStatus();
		this.confirmed = Boolean.valueOf(rawOrder.getConfirmed());
		this.totalDiscounts = new BigDecimal(rawOrder.getTotalDiscounts());
		this.totalLineItemsPrice = new BigDecimal(rawOrder.getTotalLineItemsPrice());
		this.cartToken = rawOrder.getCartToken();
		this.buyerAcceptsMarketing = Boolean.valueOf(rawOrder.getBuyerAcceptsMarketing());
		this.name = rawOrder.getName();
		this.referringSite = rawOrder.getReferringSite();
		this.landingSite = rawOrder.getLandingSite();
		this.cancelledAt = DateHelper.toDate(rawOrder.getCancelledAt(),ShopifyConstants.dateFormat);
		this.cancelReason = rawOrder.getCancelReason();
		this.totalPriceUsd = new BigDecimal(rawOrder.getTotalPriceUsd());
		this.checkoutToken = rawOrder.getCheckoutToken();
		this.reference = rawOrder.getReference();
		this.userId = rawOrder.getUserId();
		this.locationId = rawOrder.getLocationId();
		this.sourceIdentifier = rawOrder.getSourceIdentifier();
		this.sourceUrl = rawOrder.getSourceUrl();
		this.processedAt = DateHelper.toDate(rawOrder.getProcessedAt(),ShopifyConstants.dateFormat);
		this.deviceId = rawOrder.getDeviceId();
		this.phone = rawOrder.getPhone();
		this.browserIp = rawOrder.getBrowserIp();
		this.landingSiteRef = rawOrder.getLandingSiteRef();
		this.orderNumber = Integer.valueOf(rawOrder.getOrderNumber());
		this.jsonDiscountApplications = rawOrder.getJsonDiscountApplications();
		this.jsonDiscountCodes = rawOrder.getJsonDiscountCodes();
		this.jsonNoteAttributes = rawOrder.getJsonNoteAttributes();
		this.jsonPaymentGatewayNames = rawOrder.getJsonPaymentGatewayNames();
		this.processingMethod = rawOrder.getProcessingMethod();
		this.checkoutId = rawOrder.getCheckoutId();
		this.sourceName = rawOrder.getSourceName();
		this.fulfillmentStatus = rawOrder.getFulfillmentStatus();
		this.jsonTaxLines = rawOrder.getJsonTaxLines().toString();
		this.tags = rawOrder.getTags();
		this.contactEmail = rawOrder.getContactEmail();
		this.orderStatusUrl = rawOrder.getOrderStatusUrl();
		this.presentmentCurrency = rawOrder.getPresentmentCurrency();
		this.jsonTotalLineItemsPriceSet = rawOrder.getJsonTotalLineItemsPriceSet()==null?null:rawOrder.getJsonTotalLineItemsPriceSet();
		this.jsonTotalDiscountsSet = rawOrder.getJsonTotalDiscountsSet()==null?null:rawOrder.getJsonTotalDiscountsSet();
		this.jsonTotalShippingPriceSet = rawOrder.getJsonTotalShippingPriceSet()==null?null:rawOrder.getJsonTotalShippingPriceSet();
		this.jsonSubtotalPriceSet = rawOrder.getJsonSubtotalPriceSet()==null?null:rawOrder.getJsonSubtotalPriceSet();
		this.jsonTotalPriceSet = rawOrder.getJsonTotalPriceSet()==null?null:rawOrder.getJsonTotalPriceSet();
		this.jsonTotalTaxSet = rawOrder.getJsonTotalTaxSet()==null?null:rawOrder.getJsonTotalTaxSet();
		this.totalTipReceived = new BigDecimal(rawOrder.getTotalTipReceived());
		this.adminGraphqlApiId = rawOrder.getAdminGraphqlApiId();
		this.lineItems = this.constructLineItemList(rawOrder.getLineItems());
		this.shippingLines = this.constructShippingLineItemList(rawOrder.getShippingLines());
		this.jsonBillingAddress = rawOrder.getJsonBillingAddress()==null?null:rawOrder.getJsonBillingAddress();
		this.jsonShippingAddress = rawOrder.getJsonShippingAddress()==null?null:rawOrder.getJsonShippingAddress();
		this.jsonFulfillments = rawOrder.getJsonFulfillments()==null?null:rawOrder.getJsonFulfillments();
		this.jsonClientDetails = rawOrder.getJsonClientDetails()==null?null:rawOrder.getJsonClientDetails();
		this.jsonRefunds = rawOrder.getJsonRefunds()==null?null:rawOrder.getJsonRefunds();
		this.jsonPaymentDetails = rawOrder.getJsonPaymentDetails()==null?null:rawOrder.getJsonPaymentDetails();
		this.jsonCustomer = rawOrder.getJsonCustomer()==null?null:rawOrder.getJsonCustomer();
	}
	
	private List<ShopifyOrderLineItem> constructLineItemList(List<RawShopifyOrderLineItem> rawShopifyOrderLines){
		List<ShopifyOrderLineItem> lineItems = new ArrayList<ShopifyOrderLineItem>();
		for(RawShopifyOrderLineItem rawLine:rawShopifyOrderLines){
			lineItems.add(new ShopifyOrderLineItemImpl(rawLine));
		}
		return lineItems;
	}
	
	private List<ShopifyOrderShippingLine> constructShippingLineItemList(List<RawShopifyOrderShippingLine> rawShopifyOrderShippingLines){
		List<ShopifyOrderShippingLine> lineItems = new ArrayList<ShopifyOrderShippingLine>();
		for(RawShopifyOrderShippingLine rawLine:rawShopifyOrderShippingLines){
			lineItems.add(new ShopifyOrderShippingLineImpl(rawLine));
		}
		return lineItems;
	}

	@Override
	public String toString() {
		return "ShopifyOrderImpl [getOrderId()=" + getOrderId() + ", getEmail()=" + getEmail() + ", getClosedAt()="
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
				+ getContactEmail() + ", getOrderStatusUrl()=" + getOrderStatusUrl() + ", getPresentmentCurrency()"
				+ getPresentmentCurrency() + ", getJsonTotalLineItemsPriceSet()" + getJsonTotalLineItemsPriceSet()
				+ ", getJsonTotalDiscountsSet()" + getJsonTotalDiscountsSet() + ", getJsonTotalShippingPriceSet()" + getJsonTotalShippingPriceSet()
				+ ", getJsonSubtotalPriceSet()" + getJsonSubtotalPriceSet() + ", getJsonTotalPriceSet()" + getJsonTotalPriceSet()
				+ ", getJsonTotalTaxSet()" + getJsonTotalTaxSet() + ", getTotalTipReceived()="
				+ getTotalTipReceived() + ", getAdminGraphqlApiId()=" + getAdminGraphqlApiId() + ", getLineItems()="
				+ getLineItems() + ", getShippingLines()=" + getShippingLines() + ", getJsonBillingAddress()="
				+ getJsonBillingAddress() + ", getJsonShippingAddress()=" + getJsonShippingAddress()
				+ ", getJsonFulfillments()=" + getJsonFulfillments() + ", getJsonClientDetails()="
				+ getJsonClientDetails() + ", getJsonRefunds()=" + getJsonRefunds() + ", getJsonPaymentDetails()="
				+ getJsonPaymentDetails() + ", getJsonCustomer()=" + getJsonCustomer() + "]";
	}

	@Override
	public String getOrderId() {
		return this.orderId;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public Date getClosedAt() {
		return this.closedAt;
	}

	@Override
	public Date getCreatedAt() {
		return this.createdAt;
	}

	@Override
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	@Override
	public Integer getNumber() {
		return this.number;
	}

	@Override
	public String getNote() {
		return this.note;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public String getGateway() {
		return this.gateway;
	}

	@Override
	public Boolean getTest() {
		return this.test;
	}

	@Override
	public BigDecimal getTotalPrice() {
		return this.totalPrice;
	}

	@Override
	public BigDecimal getSubtotalPrice() {
		return this.subtotalPrice;
	}

	@Override
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

	@Override
	public BigDecimal getTotalTax() {
		return this.totalTax;
	}

	@Override
	public Boolean getTaxesIncluded() {
		return this.taxesIncluded;
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public String getFinancialStatus() {
		return this.financialStatus;
	}

	@Override
	public Boolean getConfirmed() {
		return this.confirmed;
	}

	@Override
	public BigDecimal getTotalDiscounts() {
		return this.totalDiscounts;
	}

	@Override
	public BigDecimal getTotalLineItemsPrice() {
		return this.totalLineItemsPrice;
	}

	@Override
	public String getCartToken() {
		return this.cartToken;
	}

	@Override
	public Boolean getBuyerAcceptsMarketing() {
		return this.buyerAcceptsMarketing;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getReferringSite() {
		return this.referringSite;
	}

	@Override
	public String getLandingSite() {
		return this.landingSite;
	}

	@Override
	public Date getCancelledAt() {
		return this.cancelledAt;
	}

	@Override
	public String getCancelReason() {
		return this.cancelReason;
	}

	@Override
	public BigDecimal getTotalPriceUsd() {
		return this.totalPriceUsd;
	}

	@Override
	public String getCheckoutToken() {
		return this.checkoutToken;
	}

	@Override
	public String getReference() {
		return this.reference;
	}

	@Override
	public String getUserId() {
		return this.userId;
	}

	@Override
	public String getLocationId() {
		return this.locationId;
	}

	@Override
	public String getSourceIdentifier() {
		return this.sourceIdentifier;
	}

	@Override
	public String getSourceUrl() {
		return this.sourceUrl;
	}

	@Override
	public Date getProcessedAt() {
		return this.processedAt;
	}

	@Override
	public String getDeviceId() {
		return this.deviceId;
	}
	
	@Override
	public String getPhone() {
		return this.phone;
	}
	
	@Override
	public String getCustomerLocale(){
		return this.customerLocale;
	}
	
	@Override
	public String getAppId(){
		return this.appId;
	}
	
	@Override
	public String getBrowserIp() {
		return this.browserIp;
	}

	@Override
	public String getLandingSiteRef() {
		return this.landingSiteRef;
	}

	@Override
	public Integer getOrderNumber() {
		return this.orderNumber;
	}

	@Override
	public String getJsonDiscountApplications() {
		return this.jsonDiscountApplications;
	}
	
	@Override
	public String getJsonDiscountCodes() {
		return this.jsonDiscountCodes;
	}

	@Override
	public String getJsonNoteAttributes() {
		return this.jsonNoteAttributes;
	}

	@Override
	public String getJsonPaymentGatewayNames() {
		return this.jsonPaymentGatewayNames;
	}

	@Override
	public String getProcessingMethod() {
		return this.processingMethod;
	}

	@Override
	public String getCheckoutId() {
		return this.checkoutId;
	}

	@Override
	public String getSourceName() {
		return this.sourceName;
	}

	@Override
	public String getFulfillmentStatus() {
		return this.fulfillmentStatus;
	}

	@Override
	public String getJsonTaxLines() {
		return this.jsonTaxLines;
	}

	@Override
	public String getTags() {
		return this.tags;
	}

	@Override
	public String getContactEmail() {
		return this.contactEmail;
	}

	@Override
	public String getOrderStatusUrl() {
		return this.orderStatusUrl;
	}

	@Override
	public String getPresentmentCurrency() { return this.presentmentCurrency; };

	@Override
	public String getJsonTotalLineItemsPriceSet() { return this.jsonTotalLineItemsPriceSet; };

	@Override
	public String getJsonTotalDiscountsSet() { return this.jsonTotalDiscountsSet; };

	@Override
	public String getJsonTotalShippingPriceSet() { return this.jsonTotalShippingPriceSet; };

	@Override
	public String getJsonSubtotalPriceSet() { return this.jsonSubtotalPriceSet; };

	@Override
	public String getJsonTotalPriceSet() { return this.jsonTotalPriceSet; };

	@Override
	public String getJsonTotalTaxSet() { return this.jsonTotalTaxSet; };

	@Override
	public BigDecimal getTotalTipReceived() { return this.totalTipReceived; }

	@Override
	public String getAdminGraphqlApiId() {
		return this.adminGraphqlApiId;
	}
	
	@Override
	public List<ShopifyOrderLineItem> getLineItems() {
		return this.lineItems;
	}

	@Override
	public List<ShopifyOrderShippingLine> getShippingLines() {
		return this.shippingLines;
	}

	@Override
	public String getJsonBillingAddress() {
		return this.jsonBillingAddress;
	}

	@Override
	public String getJsonShippingAddress() {
		return this.jsonShippingAddress;
	}

	@Override
	public String getJsonFulfillments() {
		return this.jsonFulfillments;
	}

	@Override
	public String getJsonClientDetails() {
		return this.jsonClientDetails;
	}

	@Override
	public String getJsonRefunds() {
		return this.jsonRefunds;
	}

	@Override
	public String getJsonPaymentDetails() {
		return this.jsonPaymentDetails;
	}

	@Override
	public String getJsonCustomer() {
		return this.jsonCustomer;
	}

}
