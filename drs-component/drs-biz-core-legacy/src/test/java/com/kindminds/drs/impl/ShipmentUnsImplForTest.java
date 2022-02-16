package com.kindminds.drs.impl;

import com.kindminds.drs.Currency;

import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.util.TestUtil;

import java.util.List;

public class ShipmentUnsImplForTest implements ShipmentUns {
	
	private String name;
	private ShipmentType type;
	private String invoiceNumber;
	private String trackingNumber;
	private String fbaId;
	private Forwarder forwarder;
	private String sellerCompany;
	private String buyerCompany;
	private Currency currency;
	private String amountTotal;
	private String cifAmountTotal = "0";
	private ShipmentStatus status;
	private String destinationCountry;
	private String actualDestination;
	private ShippingMethod shippingMethod;
	private String dateCreated;
	private String exportDate;	
	private String exportSrcCurrency;
	private String exportDstCurrency;
	private String exportCurrencyExchangeRate;
	private String exportCurrencyExchangeRateToEur=null;
	private String destinationReceivedDate;
	private String expectArrivalDate;
	private List<ShipmentUnsLineItem> lineItems = null;
	
	public ShipmentUnsImplForTest(
			String name,
			ShipmentType type,
			String invoiceNumber,
			String trackingNumber,
			String fbaId,
			Forwarder forwarder,
			String sellerCompany,
			String buyerCompany,
			Currency currency,
			String amountTotal,
			ShipmentStatus status,
			String destinationCountry,
			ShippingMethod shippingMethod,
			String dateCreated,
			String exportDate,
			String exportSrcCurrency,
			String exportDstCurrency,
			String exportCurrencyExchangeRate,
			String destinationReceivedDate,
			String expectArrivalDate,
			List<ShipmentUnsLineItem> lineItems) {
		super();
		this.name = name;
		this.type = type;
		this.invoiceNumber = invoiceNumber;
		this.trackingNumber=trackingNumber;
		this.fbaId = fbaId;
		this.forwarder = forwarder;
		this.sellerCompany = sellerCompany;
		this.buyerCompany = buyerCompany;
		this.currency = currency;
		this.amountTotal = amountTotal;
		this.status = status;
		this.destinationCountry = destinationCountry;
		this.shippingMethod = shippingMethod;
		this.dateCreated = dateCreated;
		this.exportDate = exportDate;		
		this.exportSrcCurrency = exportSrcCurrency;
		this.exportDstCurrency = exportDstCurrency;
		this.exportCurrencyExchangeRate = exportCurrencyExchangeRate;
		this.destinationReceivedDate = destinationReceivedDate;
		this.expectArrivalDate = expectArrivalDate;
		this.lineItems = lineItems;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public void setSellerCompany(String sellerCompany) {
		this.sellerCompany = sellerCompany;
	}
	
	public void setBuyerCompany(String buyerCompany) {
		this.buyerCompany = buyerCompany;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public void setAmountTotal(String amountTotal) {
		this.amountTotal = amountTotal;
	}
	
	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}
	
	public void addLineItem(ShipmentUnsLineItem item){
		this.lineItems.add(item);
	}
	
	public void setExportSrcCurrency(String exportSrcCurrency) {
		this.exportSrcCurrency = exportSrcCurrency;
	}
	
	public void setExportDstCurrency(String exportDstCurrency) {
		this.exportDstCurrency = exportDstCurrency;
	}
	
	public void setExportFxRateToEur(String exportCurrencyExchangeRateToEur) {
		this.exportCurrencyExchangeRateToEur = exportCurrencyExchangeRateToEur;
	}
	
	@Override
	public boolean equals( Object obj )  
	{
		if ( obj instanceof ShipmentUns ){
			ShipmentUns shp = ((ShipmentUns) obj);
			return this.getName().equals(shp.getName())
				&& TestUtil.nullableEquals(this.getType(),shp.getType())
				&& this.getInvoiceNumber().equals(shp.getInvoiceNumber())
				&& this.getTrackingNumber().equals(shp.getTrackingNumber())
				&& this.getFbaId().equals(shp.getFbaId())
				&& this.getForwarder().equals(shp.getForwarder())			
				&& this.getSellerCompanyKcode().equals(shp.getSellerCompanyKcode())
				&& this.getBuyerCompanyKcode().equals(shp.getBuyerCompanyKcode())
				&& this.getCurrency().equals(shp.getCurrency())
				&& this.getStatus().equals(shp.getStatus())
				&& this.getDestinationCountry().equals(shp.getDestinationCountry())
				&& this.getShippingMethod().equals(shp.getShippingMethod())
				&& this.getDateCreated().equals(shp.getDateCreated())
				&& this.getAmountTotal().equals(shp.getAmountTotal())
				&& TestUtil.nullableEquals(this.getExportDate(),shp.getExportDate())
				&& this.getExportSrcCurrency().equals(shp.getExportSrcCurrency())
				&& this.getExportDstCurrency().equals(shp.getExportDstCurrency())
				&& TestUtil.nullableEquals(this.getExportCurrencyExchangeRate(),shp.getExportCurrencyExchangeRate())
				&& TestUtil.nullableEquals(this.getExportFxRateToEur(),shp.getExportFxRateToEur())
				&& TestUtil.nullableEquals(this.getDestinationReceivedDate(),shp.getDestinationReceivedDate())
				&& TestUtil.nullableEquals(this.getLineItems(),shp.getLineItems())
				&& TestUtil.nullableEquals(this.getExpectArrivalDate(),shp.getExpectArrivalDate());			
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "ShipmentUnsImplForTest [getName()=" + getName() + ", getType()=" + getType() + ", getInvoiceNumber()="
				+ getInvoiceNumber() + ", getTrackingNumber()=" + getTrackingNumber() + ", getFbaId()=" + getFbaId()
				+ ", getForwarder()=" + getForwarder() + ", getSellerCompanyKcode()=" + getSellerCompanyKcode()
				+ ", getBuyerCompanyKcode()=" + getBuyerCompanyKcode() + ", getCurrency()=" + getCurrency()
				+ ", getStatus()=" + getStatus() + ", getDestinationCountry()=" + getDestinationCountry()
				+ ", getShippingMethod()=" + getShippingMethod() + ", getDateCreated()=" + getDateCreated()
				+ ", getAmountTotal()=" + getAmountTotal() + ", getExportDate()=" + getExportDate()
				+ ", getExportSrcCurrency()=" + getExportSrcCurrency() + ", getExportDstCurrency()="
				+ getExportDstCurrency() + ", getExportCurrencyExchangeRate()=" + getExportCurrencyExchangeRate()
				+ ", getExportFxRateToEur()=" + getExportFxRateToEur() + ", getDestinationReceivedDate()="
				+ getDestinationReceivedDate() + ", getExpectArrivalDate()=" + getExpectArrivalDate()
				+ ", getLineItems()=" + getLineItems() + "]";
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public ShipmentType getType() {
		return this.type;
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
		return this.forwarder;
	}
	
	@Override
	public String getSellerCompanyKcode() {
		return this.sellerCompany;
	}

	@Override
	public String getBuyerCompanyKcode() {
		return this.buyerCompany;
	}
	
	@Override
	public Currency getCurrency() {
		return this.currency;
	}
	
	@Override
	public ShipmentStatus getStatus() {
		return this.status;
	}
	
	@Override
	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	@Override
	public String getActualDestination() {
		return actualDestination;
	}

	@Override
	public ShippingMethod getShippingMethod() {
		return this.shippingMethod;
	}
	
	@Override
	public String getDateCreated() {
		return this.dateCreated;
	}
	
	@Override
	public String getAmountTotal() {
		return this.amountTotal;
	}
	
	@Override
	public String getCifAmountTotal() {
		return this.cifAmountTotal;
	}

	@Override
	public String getExportDate() {
		return this.exportDate;
	}
	
	@Override
	public String getExportSrcCurrency() {
		return this.exportSrcCurrency;
	}

	@Override
	public String getExportDstCurrency() {
		return this.exportDstCurrency;
	}

	@Override
	public String getExportCurrencyExchangeRate() {
		return this.exportCurrencyExchangeRate;
	}
	
	@Override
	public String getExportFxRateToEur() {
		return this.exportCurrencyExchangeRateToEur;
	}
	
	@Override
	public String getDestinationReceivedDate() {
		return this.destinationReceivedDate;
	}

	@Override
	public String getExpectArrivalDate() {
		return this.expectArrivalDate;
	}
	
	@Override
	public List<ShipmentUnsLineItem> getLineItems() {
		return this.lineItems;
	}

}