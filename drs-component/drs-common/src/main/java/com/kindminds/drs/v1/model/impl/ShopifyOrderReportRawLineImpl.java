package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderReportRawLine;
import com.kindminds.drs.util.DateHelper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class ShopifyOrderReportRawLineImpl implements ShopifyOrderReportRawLine {

	private String name;
	private String email;
	private String financialStatus;
	private Date paidAt;
	private String fulfillmentStatus;
	private Date fulfilledAt;
	private String acceptsMarketing;
	private String currency;
	private BigDecimal subtotal;
	private BigDecimal shipping;
	private BigDecimal taxes;
	private BigDecimal total;
	private String discountCode;
	private BigDecimal discountAmount;
	private String shippingMethod;
	private Date createdAt;
	private int lineitemQuantity;
	private String lineitemName;
	private BigDecimal lineitemPrice;
	private BigDecimal lineitemCompareAtPrice;
	private String lineitemSku;
	private boolean lineitemRequiresShipping;
	private boolean lineitemTaxable;
	private String lineitemFulfillmentStatus;
	private String billingName;
	private String billingStreet;
	private String billingAddress1;
	private String billingAddress2;
	private String billingCompany;
	private String billingCity;
	private String billingZip;
	private String billingProvince;
	private String billingCountry;
	private String billingPhone;
	private String shippingName;
	private String shippingStreet;
	private String shippingAddress1;
	private String shippingAddress2;
	private String shippingCompany;
	private String shippingCity;
	private String shippingZip;
	private String shippingProvince;
	private String shippingCountry;
	private String shippingPhone;
	private String notes;
	private String noteAttributes;
	private Date cancelledAt;
	private String paymentMethod;
	private String paymentReference;
	private BigDecimal refundedAmount;
	private String vendor;
	private String id;
	private String tags;
	private String riskLevel;
	private String source;
	private BigDecimal lineitemDiscount;
	private String tax1Name;
	private BigDecimal tax1Value;
	private String tax2Name;
	private BigDecimal tax2Value;
	private String tax3Name;
	private BigDecimal tax3Value;
	private String tax4Name;
	private BigDecimal tax4Value;
	private String tax5Name;
	private BigDecimal tax5Value;
	private String phone;
	private String duties;
	private String billingProvinceName;
	private String shippingProvinceName;

	public ShopifyOrderReportRawLineImpl(
			String name,
			String email,
			String financialStatus,
			String paidAt,
			String fulfillmentStatus,
			String fulfilledAt,
			String acceptsMarketing,
			String currency,
			String subtotal,
			String shipping,
			String taxes,
			String total,
			String discountCode,
			String discountAmount,
			String shippingMethod,
			String createdAt,
			String lineitemQuantity,
			String lineitemName,
			String lineitemPrice,
			String lineitemCompareAtPrice,
			String lineitemSku,
			String lineitemRequiresShipping,
			String lineitemTaxable,
			String lineitemFulfillmentStatus,
			String billingName,
			String billingStreet,
			String billingAddress1,
			String billingAddress2,
			String billingCompany,
			String billingCity,
			String billingZip,
			String billingProvince,
			String billingCountry,
			String billingPhone,
			String shippingName,
			String shippingStreet,
			String shippingAddress1,
			String shippingAddress2,
			String shippingCompany,
			String shippingCity,
			String shippingZip,
			String shippingProvince,
			String shippingCountry,
			String shippingPhone,
			String notes,
			String noteAttributes,
			String cancelledAt,
			String paymentMethod,
			String paymentReference,
			String refundedAmount,
			String vendor,
			String id,
			String tags,
			String riskLevel,
			String source,
			String lineitemDiscount,
			String tax1Name,
			String tax1Value,
			String tax2Name,
			String tax2Value,
			String tax3Name,
			String tax3Value,
			String tax4Name,
			String tax4Value,
			String tax5Name,
			String tax5Value,
			String phone,
			String duties,
			String billingProvinceName,
			String shippingProvinceName
	){
		this.name = name;
		this.email = this.replaceEmptyStringWithNull(email);
		this.financialStatus = this.replaceEmptyStringWithNull(financialStatus);
		this.paidAt = this.toDate(paidAt);
		this.fulfillmentStatus = this.replaceEmptyStringWithNull(fulfillmentStatus);
		this.fulfilledAt = this.toDate(fulfilledAt);
		this.acceptsMarketing = this.replaceEmptyStringWithNull(acceptsMarketing);
		this.currency = this.replaceEmptyStringWithNull(currency);
		this.subtotal = StringUtils.isEmpty(subtotal)?null:new BigDecimal(subtotal);
		this.shipping = StringUtils.isEmpty(shipping)?null:new BigDecimal(shipping);
		this.taxes = StringUtils.isEmpty(taxes)?null:new BigDecimal(taxes);
		this.total = StringUtils.isEmpty(total)?null:new BigDecimal(total);
		this.discountCode = this.replaceEmptyStringWithNull(discountCode);
		this.discountAmount = this.toBigDecimal(discountAmount);
		this.shippingMethod = this.replaceEmptyStringWithNull(shippingMethod);
		this.createdAt = this.toDate(createdAt);
		this.lineitemQuantity = Integer.parseInt(lineitemQuantity);
		this.lineitemName = lineitemName;
		this.lineitemPrice = new BigDecimal(lineitemPrice);
		this.lineitemCompareAtPrice = StringUtils.isEmpty(lineitemCompareAtPrice)?null:new BigDecimal(lineitemCompareAtPrice);
		this.lineitemSku = lineitemSku;
		this.lineitemRequiresShipping = Boolean.parseBoolean(lineitemRequiresShipping);
		this.lineitemTaxable = Boolean.parseBoolean(lineitemTaxable);
		this.lineitemFulfillmentStatus = lineitemFulfillmentStatus;
		this.billingName = this.replaceEmptyStringWithNull(billingName);
		this.billingStreet = this.replaceEmptyStringWithNull(billingStreet);
		this.billingAddress1 = this.replaceEmptyStringWithNull(billingAddress1);
		this.billingAddress2 = this.replaceEmptyStringWithNull(billingAddress2);
		this.billingCompany = this.replaceEmptyStringWithNull(billingCompany);
		this.billingCity = this.replaceEmptyStringWithNull(billingCity);
		this.billingZip = this.replaceEmptyStringWithNull(billingZip);
		this.billingProvince = this.replaceEmptyStringWithNull(billingProvince);
		this.billingCountry = this.replaceEmptyStringWithNull(billingCountry);
		this.billingPhone = this.replaceEmptyStringWithNull(billingPhone);
		this.shippingName = this.replaceEmptyStringWithNull(shippingName);
		this.shippingStreet = this.replaceEmptyStringWithNull(shippingStreet);
		this.shippingAddress1 = this.replaceEmptyStringWithNull(shippingAddress1);
		this.shippingAddress2 = this.replaceEmptyStringWithNull(shippingAddress2);
		this.shippingCompany = this.replaceEmptyStringWithNull(shippingCompany);
		this.shippingCity = this.replaceEmptyStringWithNull(shippingCity);
		this.shippingZip = this.replaceEmptyStringWithNull(shippingZip);
		this.shippingProvince = this.replaceEmptyStringWithNull(shippingProvince);
		this.shippingCountry = this.replaceEmptyStringWithNull(shippingCountry);
		this.shippingPhone = this.replaceEmptyStringWithNull(shippingPhone);
		this.notes = this.replaceEmptyStringWithNull(notes);
		this.noteAttributes = this.replaceEmptyStringWithNull(noteAttributes);
		this.cancelledAt = this.toDate(cancelledAt);
		this.paymentMethod = this.replaceEmptyStringWithNull(paymentMethod);
		this.paymentReference = this.replaceEmptyStringWithNull(paymentReference);
		this.refundedAmount = this.toBigDecimal(refundedAmount);
		this.vendor = vendor;
		this.id = this.replaceEmptyStringWithNull(id);
		this.tags = this.replaceEmptyStringWithNull(tags);
		this.riskLevel = this.replaceEmptyStringWithNull(riskLevel);
		this.source = this.replaceEmptyStringWithNull(source);
		this.lineitemDiscount = new BigDecimal(lineitemDiscount);
		this.tax1Name = this.replaceEmptyStringWithNull(tax1Name);
		this.tax1Value = this.toBigDecimal(tax1Value);
		this.tax2Name = this.replaceEmptyStringWithNull(tax2Name);
		this.tax2Value = this.toBigDecimal(tax2Value);
		this.tax3Name = this.replaceEmptyStringWithNull(tax3Name);
		this.tax3Value = this.toBigDecimal(tax3Value);
		this.tax4Name = this.replaceEmptyStringWithNull(tax4Name);
		this.tax4Value = this.toBigDecimal(tax4Value);
		this.tax5Name = this.replaceEmptyStringWithNull(tax5Name);
		this.tax5Value = this.toBigDecimal(tax5Value);
		this.phone = phone;
		this.duties = this.replaceEmptyStringWithNull(duties);
		this.billingProvinceName = this.replaceEmptyStringWithNull(billingProvinceName);
		this.shippingProvinceName = this.replaceEmptyStringWithNull(shippingProvinceName);
	}
	
	private String replaceEmptyStringWithNull(String str){ return StringUtils.isEmpty(str)?null:str; }
	private Date toDate(String dateStr){ return StringUtils.isEmpty(dateStr)?null:DateHelper.toDate(dateStr,"yyyy-MM-dd HH:mm:ss z"); }
	private BigDecimal toBigDecimal(String str){ return StringUtils.isEmpty(str)?null:new BigDecimal(str); }

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public String getFinancialStatus() {
		return this.financialStatus;
	}

	@Override
	public Date getPaidAt() {
		return this.paidAt;
	}

	@Override
	public String getFulfillmentStatus() {
		return this.fulfillmentStatus;
	}

	@Override
	public Date getFulfilledAt() {
		return this.fulfilledAt;
	}

	@Override
	public String getAcceptsMarketing() {
		return this.acceptsMarketing;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public BigDecimal getSubtotal() {
		return this.subtotal;
	}

	@Override
	public BigDecimal getShipping() {
		return this.shipping;
	}

	@Override
	public BigDecimal getTaxes() {
		return this.taxes;
	}

	@Override
	public BigDecimal getTotal() {
		return this.total;
	}

	@Override
	public String getDiscountCode() {
		return this.discountCode;
	}

	@Override
	public BigDecimal getDiscountAmount() {
		return this.discountAmount;
	}

	@Override
	public String getShippingMethod() {
		return this.shippingMethod;
	}

	@Override
	public Date getCreatedAt() {
		return this.createdAt;
	}

	@Override
	public int getLineitemQuantity() {
		return this.lineitemQuantity;
	}

	@Override
	public String getLineitemName() {
		return this.lineitemName;
	}

	@Override
	public BigDecimal getLineitemPrice() {
		return this.lineitemPrice;
	}

	@Override
	public BigDecimal getLineitemCompareAtPrice() {
		return this.lineitemCompareAtPrice;
	}

	@Override
	public String getLineitemSku() {
		return this.lineitemSku;
	}

	@Override
	public boolean getLineitemRequiresShipping() {
		return this.lineitemRequiresShipping;
	}

	@Override
	public boolean getLineitemTaxable() {
		return this.lineitemTaxable;
	}

	@Override
	public String getLineitemFulfillmentStatus() {
		return this.lineitemFulfillmentStatus;
	}

	@Override
	public String getBillingName() {
		return this.billingName;
	}

	@Override
	public String getBillingStreet() {
		return this.billingStreet;
	}

	@Override
	public String getBillingAddress1() {
		return this.billingAddress1;
	}

	@Override
	public String getBillingAddress2() {
		return this.billingAddress2;
	}

	@Override
	public String getBillingCompany() {
		return this.billingCompany;
	}

	@Override
	public String getBillingCity() {
		return this.billingCity;
	}

	@Override
	public String getBillingZip() {
		return this.billingZip;
	}

	@Override
	public String getBillingProvince() {
		return this.billingProvince;
	}

	@Override
	public String getBillingCountry() {
		return this.billingCountry;
	}

	@Override
	public String getBillingPhone() {
		return this.billingPhone;
	}

	@Override
	public String getShippingName() {
		return this.shippingName;
	}

	@Override
	public String getShippingStreet() {
		return this.shippingStreet;
	}

	@Override
	public String getShippingAddress1() {
		return this.shippingAddress1;
	}

	@Override
	public String getShippingAddress2() {
		return this.shippingAddress2;
	}

	@Override
	public String getShippingCompany() {
		return this.shippingCompany;
	}

	@Override
	public String getShippingCity() {
		return this.shippingCity;
	}

	@Override
	public String getShippingZip() {
		return this.shippingZip;
	}

	@Override
	public String getShippingProvince() {
		return this.shippingProvince;
	}

	@Override
	public String getShippingCountry() {
		return this.shippingCountry;
	}

	@Override
	public String getShippingPhone() {
		return this.shippingPhone;
	}

	@Override
	public String getNotes() {
		return this.notes;
	}

	@Override
	public String getNoteAttributes() {
		return this.noteAttributes;
	}

	@Override
	public Date getCancelledAt() {
		return this.cancelledAt;
	}

	@Override
	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	@Override
	public String getPaymentReference() {
		return this.paymentReference;
	}

	@Override
	public BigDecimal getRefundedAmount() {
		return this.refundedAmount;
	}

	@Override
	public String getVendor() {
		return this.vendor;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getTags() {
		return this.tags;
	}

	@Override
	public String getRiskLevel() {
		return this.riskLevel;
	}

	@Override
	public String getSource() {
		return this.source;
	}

	@Override
	public BigDecimal getLineitemDiscount() {
		return this.lineitemDiscount;
	}

	@Override
	public String getTax1Name() {
		return this.tax1Name;
	}

	@Override
	public BigDecimal getTax1Value() {
		return this.tax1Value;
	}

	@Override
	public String getTax2Name() {
		return this.tax2Name;
	}

	@Override
	public BigDecimal getTax2Value() {
		return this.tax2Value;
	}

	@Override
	public String getTax3Name() {
		return this.tax3Name;
	}

	@Override
	public BigDecimal getTax3Value() {
		return this.tax3Value;
	}

	@Override
	public String getTax4Name() {
		return this.tax4Name;
	}

	@Override
	public BigDecimal getTax4Value() {
		return this.tax4Value;
	}

	@Override
	public String getTax5Name() {
		return this.tax5Name;
	}

	@Override
	public BigDecimal getTax5Value() {
		return this.tax5Value;
	}
	
	@Override
	public String getPhone(){
		return this.phone;
	}

	@Override
	public String getDuties() {
		return duties;
	}

	@Override
	public String getBillingProvinceName() {
		return billingProvinceName;
	}

	@Override
	public String getShippingProvinceName() {
		return shippingProvinceName;
	}
}
