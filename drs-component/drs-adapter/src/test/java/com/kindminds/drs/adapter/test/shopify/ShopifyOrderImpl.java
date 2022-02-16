package com.kindminds.drs.adapter.test.shopify;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kindminds.drs.Currency;
import com.kindminds.drs.adapter.test.util.TestUtil;
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

	public ShopifyOrderImpl(
			String orderId,
			String email,
			String closedAt,
			String createdAt,
			String updatedAt,
			Integer number,
			String note,
			String token,
			String gateway,
			Boolean test,
			String totalPrice,
			String subtotalPrice,
			String totalWeight,
			String totalTax,
			Boolean taxesIncluded,
			String currency,
			String financialStatus,
			Boolean confirmed,
			String totalDiscounts,
			String totalLineItemsPrice,
			String cartToken,
			Boolean buyerAcceptsMarketing,
			String name,
			String referringSite,
			String landingSite,
			String cancelledAt,
			String cancelReason,
			String totalPriceUsd,
			String checkoutToken,
			String reference,
			String userId,
			String locationId,
			String sourceIdentifier,
			String sourceUrl,
			String processedAt,
			String deviceId,
			String phone,
			String browserIp,
			String landingSiteRef,
			Integer orderNumber,
			String jsonDiscountApplications,
			String jsonDiscountCodes,
			String jsonNoteAttributes,
			String jsonPaymentGatewayNames,
			String processingMethod,
			String checkoutId,
			String sourceName,
			String fulfillmentStatus,
			String jsonTaxLines,
			String tags,
			String contactEmail,
			String orderStatusUrl,
			String presentmentCurrency,
			String jsonTotalLineItemsPriceSet,
			String jsonTotalDiscountsSet,
			String jsonTotalShippingPriceSet,
			String jsonSubtotalPriceSet,
			String jsonTotalPriceSet,
			String jsonTotalTaxSet,
			String adminGraphqlApiId,
			String totalTipReceived,
			List<ShopifyOrderLineItem> lineItems,
			List<ShopifyOrderShippingLine> shippingLines,
			String jsonBillingAddress,
			String jsonShippingAddress,
			String jsonFulfillments,
			String jsonClientDetails,
			String jsonRefunds,
			String jsonPaymentDetails,
			String jsonCustomer){
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ssX";
		this.orderId = orderId;
		this.email = email;
		this.closedAt = closedAt==null?null:DateHelper.toDate(closedAt, dateFormat);
		this.createdAt = closedAt==null?null:DateHelper.toDate(createdAt, dateFormat);
		this.updatedAt = closedAt==null?null:DateHelper.toDate(updatedAt, dateFormat);
		this.number = number;
		this.note = note;
		this.token = token;
		this.gateway = gateway;
		this.test = test;
		this.totalPrice = new BigDecimal(totalPrice);
		this.subtotalPrice = new BigDecimal(subtotalPrice);
		this.totalWeight = new BigDecimal(totalWeight);
		this.totalTax = new BigDecimal(totalTax);
		this.taxesIncluded = taxesIncluded;
		this.currency = Currency.valueOf(currency);
		this.financialStatus = financialStatus;
		this.confirmed = confirmed;
		this.totalDiscounts = new BigDecimal(totalDiscounts);
		this.totalLineItemsPrice = new BigDecimal(totalLineItemsPrice);
		this.cartToken = cartToken;
		this.buyerAcceptsMarketing = buyerAcceptsMarketing;
		this.name = name;
		this.referringSite = referringSite;
		this.landingSite = landingSite;
		this.cancelledAt = closedAt==null?null:DateHelper.toDate(cancelledAt,dateFormat);
		this.cancelReason = cancelReason;
		this.totalPriceUsd = new BigDecimal(totalPriceUsd);
		this.checkoutToken = checkoutToken;
		this.reference = reference;
		this.userId = userId;
		this.locationId = locationId;
		this.sourceIdentifier = sourceIdentifier;
		this.sourceUrl = sourceUrl;
		this.processedAt = closedAt==null?null:DateHelper.toDate(processedAt,dateFormat);
		this.deviceId = deviceId;
		this.phone = phone;
		this.browserIp = browserIp;
		this.landingSiteRef = landingSiteRef;
		this.orderNumber = orderNumber;
		this.jsonDiscountApplications = jsonDiscountApplications;
		this.jsonDiscountCodes = jsonDiscountCodes;
		this.jsonNoteAttributes = jsonNoteAttributes;
		this.jsonPaymentGatewayNames = jsonPaymentGatewayNames;
		this.processingMethod = processingMethod;
		this.checkoutId = checkoutId;
		this.sourceName = sourceName;
		this.fulfillmentStatus = fulfillmentStatus;
		this.jsonTaxLines = jsonTaxLines;
		this.tags = tags;
		this.contactEmail = contactEmail;
		this.orderStatusUrl = orderStatusUrl;
		this.presentmentCurrency = presentmentCurrency;
		this.jsonTotalLineItemsPriceSet = jsonTotalLineItemsPriceSet;
		this.jsonTotalDiscountsSet = jsonTotalDiscountsSet;
		this.jsonTotalShippingPriceSet = jsonTotalShippingPriceSet;
		this.jsonSubtotalPriceSet = jsonSubtotalPriceSet;
		this.jsonTotalPriceSet = jsonTotalPriceSet;
		this.jsonTotalTaxSet = jsonTotalTaxSet;
		this.totalTipReceived = new BigDecimal(totalTipReceived);
		this.adminGraphqlApiId = adminGraphqlApiId; 
		this.lineItems = lineItems;
		this.shippingLines = shippingLines;
		this.jsonBillingAddress = jsonBillingAddress;
		this.jsonShippingAddress = jsonShippingAddress;
		this.jsonFulfillments = jsonFulfillments;
		this.jsonClientDetails = jsonClientDetails;
		this.jsonRefunds = jsonRefunds;
		this.jsonPaymentDetails = jsonPaymentDetails;
		this.jsonCustomer = jsonCustomer;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShopifyOrder){
			ShopifyOrder so = (ShopifyOrder)obj;
			return this.getOrderId().equals(so.getOrderId())
				&& this.getEmail().equals(so.getEmail())
				&& this.getClosedAt().equals(so.getClosedAt())
				&& this.getCreatedAt().equals(so.getCreatedAt())
				&& this.getUpdatedAt().equals(so.getUpdatedAt())
				&& this.getNumber().equals(so.getNumber())
				&& TestUtil.nullableEquals(this.getNote(),so.getNote())
				&& this.getToken().equals(so.getToken())
				&& this.getGateway().equals(so.getGateway())
				&& this.getTest().equals(so.getTest())
				&& this.getTotalPrice().equals(so.getTotalPrice())
				&& this.getSubtotalPrice().equals(so.getSubtotalPrice())
				&& this.getTotalWeight().equals(so.getTotalWeight())
				&& this.getTotalTax().equals(so.getTotalTax())
				&& this.getTaxesIncluded().equals(so.getTaxesIncluded())
				&& this.getCurrency().equals(so.getCurrency())
				&& this.getFinancialStatus().equals(so.getFinancialStatus())
				&& this.getConfirmed().equals(so.getConfirmed())
				&& this.getTotalDiscounts().equals(so.getTotalDiscounts())
				&& this.getTotalLineItemsPrice().equals(so.getTotalLineItemsPrice())
				&& this.getCartToken().equals(so.getCartToken())
				&& this.getBuyerAcceptsMarketing().equals(so.getBuyerAcceptsMarketing())
				&& this.getName().equals(so.getName())
				&& this.getReferringSite().equals(so.getReferringSite())
				&& this.getLandingSite().equals(so.getLandingSite())
				&& TestUtil.nullableEquals(this.getCancelledAt(),so.getCancelledAt())
				&& TestUtil.nullableEquals(this.getCancelReason(),so.getCancelReason())
				&& this.getTotalPriceUsd().equals(so.getTotalPriceUsd())
				&& this.getCheckoutToken().equals(so.getCheckoutToken())
				&& TestUtil.nullableEquals(this.getReference(),so.getReference())
				&& TestUtil.nullableEquals(this.getUserId(),so.getUserId())
				&& TestUtil.nullableEquals(this.getLocationId(),so.getLocationId())
				&& TestUtil.nullableEquals(this.getSourceIdentifier(),so.getSourceIdentifier())
				&& TestUtil.nullableEquals(this.getSourceUrl(),so.getSourceUrl())
				&& this.getProcessedAt().equals(so.getProcessedAt())
				&& TestUtil.nullableEquals(this.getDeviceId(),so.getDeviceId())
				&& TestUtil.nullableEquals(this.getPhone(),so.getPhone())
				&& this.getCustomerLocale().equals(so.getCustomerLocale())
				&& this.getAppId().equals(so.getAppId())
				&& this.getBrowserIp().equals(so.getBrowserIp())
				&& TestUtil.nullableEquals(this.getLandingSiteRef(),so.getLandingSiteRef())
				&& this.getOrderNumber().equals(so.getOrderNumber())
				&& this.getJsonDiscountApplications().equals(so.getJsonDiscountApplications())
				&& this.getJsonDiscountCodes().equals(so.getJsonDiscountCodes())
				&& this.getJsonNoteAttributes().equals(so.getJsonNoteAttributes())
				&& this.getJsonPaymentGatewayNames().equals(so.getJsonPaymentGatewayNames())
				&& this.getProcessingMethod().equals(so.getProcessingMethod())
				&& this.getCheckoutId().equals(so.getCheckoutId())
				&& this.getSourceName().equals(so.getSourceName())
				&& this.getFulfillmentStatus().equals(so.getFulfillmentStatus())
				&& this.getJsonTaxLines().equals(so.getJsonTaxLines())
				&& TestUtil.nullableEquals(this.getTags(),so.getTags())
				&& this.getContactEmail().equals(so.getContactEmail())
				&& this.getOrderStatusUrl().equals(so.getOrderStatusUrl())
				&& this.getPresentmentCurrency().equals(so.getPresentmentCurrency())
				&& this.getJsonTotalLineItemsPriceSet().equals(so.getJsonTotalLineItemsPriceSet())
				&& this.getJsonTotalDiscountsSet().equals(so.getJsonTotalDiscountsSet())
				&& this.getJsonTotalShippingPriceSet().equals(so.getJsonTotalShippingPriceSet())
				&& this.getJsonSubtotalPriceSet().equals(so.getJsonSubtotalPriceSet())
				&& this.getJsonTotalPriceSet().equals(so.getJsonTotalPriceSet())
				&& this.getJsonTotalTaxSet().equals(so.getJsonTotalTaxSet())
				&& this.getAdminGraphqlApiId().equals(so.getAdminGraphqlApiId())
				&& this.getTotalTipReceived().equals(so.getTotalTipReceived())
				&& this.getLineItems().equals(so.getLineItems())
				&& this.getShippingLines().equals(so.getShippingLines())
				&& this.getJsonBillingAddress().equals(so.getJsonBillingAddress())
				&& this.getJsonShippingAddress().equals(so.getJsonShippingAddress())
				&& this.getJsonFulfillments().equals(so.getJsonFulfillments())
				&& this.getJsonClientDetails().equals(so.getJsonClientDetails())
				&& this.getJsonRefunds().equals(so.getJsonRefunds())
				&& this.getJsonPaymentDetails().equals(so.getJsonPaymentDetails())
				&& this.getJsonCustomer().equals(so.getJsonCustomer());
		}
		return false;
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