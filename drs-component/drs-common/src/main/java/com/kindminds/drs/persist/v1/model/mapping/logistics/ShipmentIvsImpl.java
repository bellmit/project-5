package com.kindminds.drs.persist.v1.model.mapping.logistics;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShipmentIvsImpl implements Serializable , ShipmentIvs {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="name")
	private String name;
	//@Column(name="type")
	private String type;
	//@Column(name="expected_export_date")
	private String expectedExportDate;
	//@Column(name="shipment_fca_location_id")
	private String fcaDeliveryLocationId;
	//@Column(name="fca_delivery_date")
	private String fcaDeliveryDate;	
	//@Column(name="invoice_number")
	private String invoiceNumber;
	//@Column(name="seller_company_kcode")
	private String sellerCompanyKcode;
	//@Column(name="buyer_company_kcode")
	private String buyerCompanyKcode;
	//@Column(name="currency_name")
	private String currency;
	//@Column(name="sales_tax_rate")
	private BigDecimal salesTaxRate;
	//@Column(name="subtotal")
	private BigDecimal subtotal;
	//@Column(name="amount_tax")
	private BigDecimal amountTax;
	//@Column(name="amount_total")
	private BigDecimal amountTotal;
	//@Column(name="destination_country_code")
	private String destinationCountryCode;
	//@Column(name="shipping_method")
	private String shippingMethod;
	//@Column(name="date_created")
	private String dateCreated;
	//@Column(name="status")
	private String status;	
	//@Column(name="date_purchased")
	private String datePurchased;	
	//@Column(name="internal_note")
	private String internalNote;
	//@Column(name="special_request")
	private String specialRequest;
	//@Column(name="num_of_repackaging")
	private Integer boxesNeedRepackaging;
	//@Column(name="repackaging_fee")
	private BigDecimal repackagingFee;
	//@Column(name="required_po")
	private Boolean requiredPO;
	//@Column(name="po_number")
	private String PONumber;

	private BigDecimal paidTotal;

	private String unsName;

	private List<ShipmentIvsLineItem> lineItems=null;

	public ShipmentIvsImpl(int id, String name, String type, String expectedExportDate, String fcaDeliveryLocationId,
						   String fcaDeliveryDate, String invoiceNumber, String sellerCompanyKcode, String buyerCompanyKcode,
						   String currency, BigDecimal salesTaxRate, BigDecimal subtotal, BigDecimal amountTax,
						   BigDecimal amountTotal, String destinationCountryCode, String shippingMethod, String dateCreated,
						   String status, String datePurchased, String internalNote, String specialRequest, Integer boxesNeedRepackaging,
						   BigDecimal repackagingFee, Boolean requiredPO, String PONumber) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.expectedExportDate = expectedExportDate;
		this.fcaDeliveryLocationId = fcaDeliveryLocationId;
		this.fcaDeliveryDate = fcaDeliveryDate;
		this.invoiceNumber = invoiceNumber;
		this.sellerCompanyKcode = sellerCompanyKcode;
		this.buyerCompanyKcode = buyerCompanyKcode;
		this.currency = currency;
		this.salesTaxRate = salesTaxRate;
		this.subtotal = subtotal;
		this.amountTax = amountTax;
		this.amountTotal = amountTotal;
		this.destinationCountryCode = destinationCountryCode;
		this.shippingMethod = shippingMethod;
		this.dateCreated = dateCreated;
		this.status = status;
		this.datePurchased = datePurchased;
		this.internalNote = internalNote;
		this.specialRequest = specialRequest;
		this.boxesNeedRepackaging = boxesNeedRepackaging;
		this.repackagingFee = repackagingFee;
		this.requiredPO = requiredPO;
		this.PONumber = PONumber;

	}

	public void setPaymentTotal(BigDecimal total){ this.paidTotal=total==null?BigDecimal.ZERO:total; }
	
	public void setUnsName(String unsName) { this.unsName = unsName; }
	
	public void setLineItems(List<ShipmentIvsLineItem> lineItems){ this.lineItems=lineItems; }

	public ShipmentIvsImpl() {

	}

	//For CalculateShippingCost Testing
	public ShipmentIvsImpl(String name, String fcaDeliveryLocationId,
						   String destinationCountryCode, String shippingMethod,
						   List<ShipmentIvsLineItem> lineItems) {
		this.name = name;
		this.fcaDeliveryLocationId = fcaDeliveryLocationId;
		this.destinationCountryCode = destinationCountryCode;
		this.shippingMethod = shippingMethod;
		this.lineItems = lineItems;
		this.currency = "TWD";
		this.salesTaxRate = BigDecimal.ZERO;
		this.status = "SHPT_DRAFT";
		this.subtotal = BigDecimal.ZERO;
		this.amountTax = BigDecimal.ZERO;
		this.amountTotal = BigDecimal.ZERO;
	}

	public ShipmentIvsImpl(String name, String type, String expectedExportDate,
						   String fcaDeliveryLocationId, String fcaDeliveryDate,
						   String invoiceNumber, String sellerCompanyKcode,
						   String buyerCompanyKcode, String currency,
						   String salesTaxRate, String subtotal,
						   String amountTax, String amountTotal,
						   String destinationCountryCode, String shippingMethod,
						   String dateCreated, String status, String datePurchased,
						   String internalNote, String specialRequest,
						   Integer boxesNeedRepackaging, String repackagingFee,
						   Boolean requiredPO, String PONumber) {
		this.name = name;
		this.type = type;
		this.expectedExportDate = expectedExportDate;
		this.fcaDeliveryLocationId = fcaDeliveryLocationId;
		this.fcaDeliveryDate = fcaDeliveryDate;
		this.invoiceNumber = invoiceNumber;
		this.sellerCompanyKcode = sellerCompanyKcode;
		this.buyerCompanyKcode = buyerCompanyKcode;
		this.currency = currency;
		this.salesTaxRate = new BigDecimal(salesTaxRate);
		this.subtotal = new BigDecimal(subtotal);
		this.amountTax = new BigDecimal(amountTax);
		this.amountTotal = new BigDecimal(amountTotal);
		this.destinationCountryCode = destinationCountryCode;
		this.shippingMethod = shippingMethod;
		this.dateCreated = dateCreated;
		this.status = status;
		this.datePurchased = datePurchased;
		this.internalNote = internalNote;
		this.specialRequest = specialRequest;
		this.boxesNeedRepackaging = boxesNeedRepackaging;
		this.repackagingFee = new BigDecimal(repackagingFee);
		this.requiredPO = requiredPO;
		this.PONumber = PONumber;
		lineItems = new ArrayList<>();
	}


	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public ShipmentType getType() {
		return ShipmentType.fromValue(this.type);
	}
	
	@Override
	public String getExpectedExportDate() {		
		return this.expectedExportDate;
	}
	
	@Override
	public String getFcaDeliveryLocationId() {		
		return this.fcaDeliveryLocationId;
	}
	
	@Override
	public String getFcaDeliveryDate() {		
		return this.fcaDeliveryDate;
	}
	
	@Override
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}
	
	@Override
	public String getSellerCompanyKcode() {
		return this.sellerCompanyKcode;
	}
	
	@Override
	public String getBuyerCompanyKcode() {
		return this.buyerCompanyKcode;
	}
	
	@Override
	public Currency getCurrency() {
		return Currency.valueOf(this.currency);
	}
	
	@Override
	public String getSalesTaxPercentage() {
		return this.salesTaxRate == null ? "" :
				this.salesTaxRate.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
	}
	
	@Override
	public ShipmentStatus getStatus() {
		return ShipmentStatus.valueOf(this.status);
	}
	
	@Override
	public String getDestinationCountry() {
		return this.destinationCountryCode;
	}
	
	@Override
	public ShippingMethod getShippingMethod() {
		return ShippingMethod.valueOf(this.shippingMethod);
	}
	
	@Override
	public String getDateCreated() {
		return this.dateCreated;
	}
	
	@Override
	public String getSubtotal() {

		return this.subtotal == null ? "": this.subtotal.stripTrailingZeros().toPlainString();
	}
	
	@Override
	public String getSalesTax() {
		return this.amountTax == null ? "" : this.amountTax.setScale(this.getCurrency().getScale(), RoundingMode.HALF_UP).toString();
	}
	
	@Override
	public String getTotal() {
		return this.amountTotal== null ? "" : this.amountTotal.setScale(this.getCurrency().getScale(), RoundingMode.HALF_UP).toString();
	}
	
	@Override
	public String getPaidTotal() {
		if(this.paidTotal==null) return null;
		Currency c = Currency.valueOf(this.currency);
		return this.paidTotal.setScale(c.getScale()).toPlainString();
	}

	@Override
	public String getSpecialRequest() {
		return specialRequest;
	}

	@Override
	public String getBoxesNeedRepackaging() {
		return boxesNeedRepackaging==null?
				"0" : Integer.toString(boxesNeedRepackaging);
	}

	@Override
	public String getRepackagingFee() {
		return repackagingFee==null?
				"0" : repackagingFee.stripTrailingZeros().toPlainString();
	}

	@Override
	public Boolean getRequiredPO() {
		if (requiredPO == null) return false;
		return requiredPO;
	}

	@Override
	public String getPONumber() {
		return PONumber;
	}

	@Override
	public String getUnsName() {
		return this.unsName;
	}

	@Override
	public String getDatePurchased() {
		return this.datePurchased;
	}

	@Override
	public String getInternalNote() {
		return this.internalNote;
	}
	
	@Override
	public List<ShipmentIvsLineItem> getLineItems() {
		return this.lineItems;
	}

	@Override
	public String toString() {
		return "ShipmentIvsImpl [getName()=" + getName()
				+ ", getType()=" + getType()
				+ ", getExpectedExportDate()=" + getExpectedExportDate()
				+ ", getFcaDeliveryLocationId()=" + getFcaDeliveryLocationId()
				+ ", getFcaDeliveryDate()=" + getFcaDeliveryDate()
				+ ", getInvoiceNumber()=" + getInvoiceNumber()
				+ ", getSellerCompanyKcode()=" + getSellerCompanyKcode()
				+ ", getBuyerCompanyKcode()=" + getBuyerCompanyKcode()
				+ ", getCurrency()=" + getCurrency()
				+ ", getSalesTaxPercentage()=" + getSalesTaxPercentage()
				+ ", getStatus()=" + getStatus()
				+ ", getDestinationCountry()=" + getDestinationCountry()
				+ ", getShippingMethod()=" + getShippingMethod()
				+ ", getDateCreated()=" + getDateCreated()
				+ ", getSubtotal()=" + getSubtotal()
				+ ", getSalesTax()=" + getSalesTax()
				+ ", getTotal()=" + getTotal()
//				+ ", getPaidTotal()=" + getPaidTotal()
//				+ ", getUnsName()=" + getUnsName()
				+ ", getDatePurchased()=" + getDatePurchased()
				+ ", getInternalNote()=" + getInternalNote()
				+ ", getSpecialRequest()=" + getSpecialRequest()
				+ ", getBoxesNeedRepackaging()=" + getBoxesNeedRepackaging()
				+ ", getRepackagingFee()=" + getRepackagingFee()
				+ ", getRequiredPO()=" + getRequiredPO()
				+ ", getPONumber()=" + getPONumber()
				+ ", getLineItems()=" + getLineItems() + "]";
	}


}