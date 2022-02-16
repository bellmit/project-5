package com.kindminds.drs.persist.v1.model.mapping.logistics;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.util.NumberHelper;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;


public class ShipmentUnsImpl implements ShipmentUns {

	//@Id //@Column(name="name")
	private String name;
	//@Column(name="type")
	private String type;
	//@Column(name="invoice_number")
	private String invoiceNumber;
	//@Column(name="tracking_number")
	private String trackingNumber;	
	//@Column(name="fba_id")
	private String fbaId;	
	//@Column(name="forwarder")
	private String forwarder;		
	//@Column(name="seller_company_kcode")
	private String sellerCompanyKcode;
	//@Column(name="buyer_company_kcode")
	private String buyerCompanyKcode;
	//@Column(name="currency_name")
	private String currency;	
	//@Column(name="amount_total")
	private BigDecimal amountTotal;
	//@Column(name="cif_amount_total")
	private BigDecimal cifAmountTotal;
	//@Column(name="destination_country_code")
	private String destinationCountryCode;
	//@Column(name="actual_destination_code")
	private String actualDestination;
	//@Column(name="shipping_method")
	private String shippingMethod;
	//@Column(name="date_created")
	private Date dateCreated;
	//@Column(name="status")
	private String status;
	//@Column(name="export_date")
	private Date exportDate;		
	//@Column(name="export_src_currency_id")
	private Integer exportSrcCurrencyId;
	//@Column(name="export_dst_currency_id")
	private Integer exportDstCurrencyId;
	//@Column(name="export_currency_exchange_rate")
	private BigDecimal exportCurrencyExchangeRate;
	//@Column(name="export_currency_exchange_rate_to_eur")
	private BigDecimal exportCurrencyExchangeRateToEur;
	//@Column(name="arrival_date")
	private Date destinationReceivedDate;
	//@Column(name="expect_arrival_date")
	private Date expectArrivalDate;

	private List<ShipmentUnsLineItem> lineItems = null;

	public ShipmentUnsImpl(String name, String type, String invoiceNumber, String trackingNumber, String fbaId, String forwarder, String sellerCompanyKcode, String buyerCompanyKcode, String currency, BigDecimal amountTotal, BigDecimal cifAmountTotal, String destinationCountryCode, String actualDestination, String shippingMethod, Date dateCreated, String status, Date exportDate, Integer exportSrcCurrencyId, Integer exportDstCurrencyId, BigDecimal exportCurrencyExchangeRate, BigDecimal exportCurrencyExchangeRateToEur, Date destinationReceivedDate, Date expectArrivalDate) {
		this.name = name;
		this.type = type;
		this.invoiceNumber = invoiceNumber;
		this.trackingNumber = trackingNumber;
		this.fbaId = fbaId;
		this.forwarder = forwarder;
		this.sellerCompanyKcode = sellerCompanyKcode;
		this.buyerCompanyKcode = buyerCompanyKcode;
		this.currency = currency;
		this.amountTotal = amountTotal;
		this.cifAmountTotal = cifAmountTotal;
		this.destinationCountryCode = destinationCountryCode;
		this.actualDestination = actualDestination;
		this.shippingMethod = shippingMethod;
		this.dateCreated = dateCreated;
		this.status = status;
		this.exportDate = exportDate;
		this.exportSrcCurrencyId = exportSrcCurrencyId;
		this.exportDstCurrencyId = exportDstCurrencyId;
		this.exportCurrencyExchangeRate = exportCurrencyExchangeRate;
		this.exportCurrencyExchangeRateToEur = exportCurrencyExchangeRateToEur;
		this.destinationReceivedDate = destinationReceivedDate;
		this.expectArrivalDate = expectArrivalDate;
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
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}
	
	@Override
	public String getTrackingNumber() {
		return this.trackingNumber;
	}
	
	@Override
	public String getFbaId() {
		return this.fbaId;
	}
	
	@Override
	public Forwarder getForwarder() {
		if(this.forwarder==null) return null;
		return Forwarder.valueOf(this.forwarder);
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
	public ShipmentStatus getStatus() {
		return ShipmentStatus.valueOf(this.status);
	}
	
	@Override
	public String getDestinationCountry() {
		return this.destinationCountryCode;
	}

	@Override
	public String getActualDestination() {
		return this.actualDestination;
	}
	
	@Override
	public ShippingMethod getShippingMethod() {
		return ShippingMethod.valueOf(this.shippingMethod);
	}
	
	@Override
	public String getDateCreated() {
		return DateHelper.toString(this.dateCreated, "yyyy-MM-dd HH:mm +0000", "UTC");
	}
	
	@Override
	public String getAmountTotal() {
		return NumberHelper.toGeneralCommaSeparatedString(this.amountTotal,2);
	}

	@Override
	public String getCifAmountTotal() {
		return NumberHelper.toGeneralCommaSeparatedString(this.cifAmountTotal,2);
	}

	@Override
	public String getExportDate() {
		if(this.exportDate!=null) return DateHelper.toString(this.exportDate, "yyyy-MM-dd", "UTC");
		return null;
	}
	
	@Override
	public String getExportSrcCurrency() {

		return this.exportSrcCurrencyId != null ? Currency.fromKey(this.exportSrcCurrencyId).name() :"";
	}
	
	@Override
	public String getExportDstCurrency() {

		return this.exportDstCurrencyId != null ? Currency.fromKey(this.exportDstCurrencyId).name() : "";
	}
	
	@Override
	public String getExportCurrencyExchangeRate() {
		if(this.exportCurrencyExchangeRate!=null){
			return this.exportCurrencyExchangeRate.toString();
		}
		return null;
	}
	
	@Override
	public String getExportFxRateToEur() {
		if(this.exportCurrencyExchangeRateToEur!=null){
			return this.exportCurrencyExchangeRateToEur.stripTrailingZeros().toPlainString();
		}
		return null;
	}
	
	@Override
	public String getDestinationReceivedDate() {
		if(this.destinationReceivedDate==null) return null;
		return DateHelper.toString(this.destinationReceivedDate, "yyyy-MM-dd HH:mm +0000", "UTC");
	}

	@Override
	public String getExpectArrivalDate() {
		return DateHelper.toString(this.expectArrivalDate, "yyyy-MM-dd HH:mm +0000", "UTC");
	}
	
	@Override
	public List<ShipmentUnsLineItem> getLineItems() {
		return this.lineItems;
	}
	
	public void setLineItems(List<ShipmentUnsLineItem> lineItems){
		this.lineItems = lineItems;
	}
	
	@Override
	public String toString() {
		return "ShipmentUnsImpl [getName()=" + getName() + ", getType()=" + getType() + ", getInvoiceNumber()="
				+ getInvoiceNumber() + ", getTrackingNumber()=" + getTrackingNumber() + ", getFbaId()=" + getFbaId()
				+ ", getForwarder()=" + getForwarder() + ", getSellerCompanyKcode()=" + getSellerCompanyKcode()
				+ ", getBuyerCompanyKcode()=" + getBuyerCompanyKcode() + ", getCurrency()=" + getCurrency()
				+ ", getStatus()=" + getStatus() + ", getDestinationCountry()=" + getDestinationCountry()
				+ ", getActualDestination()=" + getActualDestination()
				+ ", getShippingMethod()=" + getShippingMethod() + ", getDateCreated()=" + getDateCreated()
				+ ", getAmountTotal()=" + getAmountTotal() + ", getExportDate()=" + getExportDate()
				+ ", getExportSrcCurrency()=" + getExportSrcCurrency() + ", getExportDstCurrency()="
				+ getExportDstCurrency() + ", getExportCurrencyExchangeRate()=" + getExportCurrencyExchangeRate()
				+ ", getExportFxRateToEur()=" + getExportFxRateToEur() + ", getDestinationReceivedDate()="
				+ getDestinationReceivedDate() + ", getExpectArrivalDate()=" + getExpectArrivalDate()
				+ ", getLineItems()=" + getLineItems() + "]";
	}
									
}