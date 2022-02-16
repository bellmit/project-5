package com.kindminds.drs.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.util.TestUtil;


import java.util.ArrayList;
import java.util.List;

public class ShpIvsImpl implements ShipmentIvs {
	
	private String name;
	private ShipmentType type;
	private String expectedExportDate;	
	private String fcaDeliveryLocationId;	
	private String fcaDeliveryDate;
	private String invoiceNumber;
	private String sellerCompany;
	private String buyerCompany;
	private Currency currency;
	private String taxRate;
	private String subtotal;
	private String salesTax;
	private String total;
	private String paidTotal;
	private ShipmentStatus status;
	private String destinationCountry;
	private ShippingMethod shippingMethod;
	private String dateCreated;
	private String datePurchased;
	private String internalNote;	
	private List<ShipmentIvsLineItem> lineItems = new ArrayList<ShipmentIvsLineItem>();
	private String specialRequest;
	private String boxesNeedRepackaging = "0";
	private String repackagingFee = "0";
	private Boolean requiredPO = false;
	private String PONumber;
	
	public void setName(String name) {
		this.name = name;
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
	
	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}
	
	public void setSalesTax(String salesTax) {
		this.salesTax = salesTax;
	}
	
	public void setTotal(String total) {
		this.total = total;
	}
	
	public void setPaidTotal(String paidTotal){
		this.paidTotal = paidTotal;
	}

	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}
	
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public void setSpecialRequest(String specialRequest) {
		this.specialRequest = specialRequest;
	}

	public void setBoxesNeedRepackaging(String boxesNeedRepackaging) {
		this.boxesNeedRepackaging = boxesNeedRepackaging;
	}

	public void setRepackagingFee(String repackagingFee) {
		this.repackagingFee = repackagingFee;
	}

	public void setRequiredPO(Boolean requiredPO) {
		this.requiredPO = requiredPO;
	}

	public void setPONumber(String pONumber) {
		PONumber = pONumber;
	}


	public ShpIvsImpl(ShipmentIvs shipment) {
		this.name = shipment.getName();
		this.type = shipment.getType();
		this.expectedExportDate = shipment.getExpectedExportDate();
		this.fcaDeliveryLocationId = shipment.getFcaDeliveryLocationId();
		this.fcaDeliveryDate = shipment.getFcaDeliveryDate();
		this.invoiceNumber = shipment.getInvoiceNumber();
		this.sellerCompany = shipment.getSellerCompanyKcode();
		this.buyerCompany = shipment.getBuyerCompanyKcode();
		this.currency = shipment.getCurrency();
		this.taxRate = shipment.getSalesTaxPercentage();
		this.subtotal = shipment.getSubtotal();
		this.salesTax = shipment.getSalesTax();
		this.total = shipment.getTotal();
		this.status = shipment.getStatus();
		this.destinationCountry = shipment.getDestinationCountry();
		this.shippingMethod = shipment.getShippingMethod();
		this.dateCreated = shipment.getDateCreated();
		this.datePurchased = shipment.getDatePurchased();
		this.internalNote = shipment.getInternalNote();
		this.lineItems = shipment.getLineItems();
		this.specialRequest = shipment.getSpecialRequest();
		this.boxesNeedRepackaging = shipment.getBoxesNeedRepackaging();
		this.repackagingFee = shipment.getRepackagingFee();
		this.requiredPO = shipment.getRequiredPO();
		this.PONumber = shipment.getPONumber();
	}

	public ShpIvsImpl(
			String name,
			ShipmentType type,
			String expectedExportDate,
			String fcaDeliveryLocationId,
			String fcaDeliveryDate,
			String invoiceNumber,
			String sellerCompany,
			String buyerCompany,
			String currency,
			String salesTaxRate,
			String subtotal,
			String salesTax,
			String total,
			String status,
			String destinationCountry,
			String shippingMethod,
			String dateCreated,
			String datePurchased,
			String internalNote,
			List<ShipmentIvsLineItem> lineItems,
			String specialRequest,
			String boxesNeedRepackaging,
			String repackagingFee,
			Boolean requiredPO,
			String PONumber) {
		this.name = name;
		this.type = type;
		this.expectedExportDate = expectedExportDate;
		this.fcaDeliveryLocationId = fcaDeliveryLocationId;
		this.fcaDeliveryDate = fcaDeliveryDate;
		this.invoiceNumber = invoiceNumber;
		this.sellerCompany = sellerCompany;
		this.buyerCompany = buyerCompany;
		this.currency = currency==null?null:Currency.valueOf(currency);
		this.taxRate = salesTaxRate;
		this.subtotal = subtotal;
		this.salesTax = salesTax;
		this.total = total;
		this.status = status==null?null:ShipmentStatus.valueOf(status);
		this.destinationCountry = destinationCountry;
		this.shippingMethod = ShippingMethod.valueOf(shippingMethod);
		this.dateCreated = dateCreated;
		this.datePurchased = datePurchased;
		this.internalNote = internalNote;
		this.lineItems = lineItems;
		this.specialRequest = specialRequest;
		this.boxesNeedRepackaging = boxesNeedRepackaging;
		this.repackagingFee = repackagingFee;
		this.requiredPO = requiredPO;
		this.PONumber = PONumber;
	}

	public ShpIvsImpl(
			String name,
			ShipmentType type,
			String expectedExportDate,
			String fcaDeliveryLocationId,
			String fcaDeliveryDate,
			String invoiceNumber,
			String sellerCompany,
			String buyerCompany,
			String currency,
			String salesTaxRate,
			String subtotal,
			String salesTax,
			String total,
			String paidTotal,
			String status,
			String destinationCountry,
			String shippingMethod, 
			String dateCreated,
			String datePurchased,
			String internalNote,			
			List<ShipmentIvsLineItem> lineItems) {
		this.name = name;
		this.type = type;
		this.expectedExportDate = expectedExportDate;
		this.fcaDeliveryLocationId = fcaDeliveryLocationId;
		this.fcaDeliveryDate = fcaDeliveryDate;
		this.invoiceNumber = invoiceNumber;
		this.sellerCompany = sellerCompany;
		this.buyerCompany = buyerCompany;
		this.currency = currency==null?null:Currency.valueOf(currency);
		this.taxRate = salesTaxRate;
		this.subtotal = subtotal;
		this.salesTax = salesTax;
		this.total = total;
		this.paidTotal = paidTotal;
		this.status = status==null?null:ShipmentStatus.valueOf(status);
		this.destinationCountry = destinationCountry;
		this.shippingMethod = ShippingMethod.valueOf(shippingMethod);
		this.dateCreated = dateCreated;
		this.datePurchased = datePurchased;
		this.internalNote = internalNote;
		this.lineItems = lineItems;
	}
	
	@Override
	public boolean equals( Object obj ){
		if ( obj instanceof Ivs){
			Ivs shp = ((Ivs) obj);
			return this.getName().equals(shp.getName())
				&& TestUtil.nullableEquals(this.getType(),shp.getType())
				&& TestUtil.nullableEquals(this.getExpectedExportDate(), shp.getExpectedExportDate())
				&& TestUtil.nullableEquals(this.getFcaDeliveryLocationId(), shp.getFcaDeliveryLocationId())
				&& TestUtil.nullableEquals(this.getFcaDeliveryDate(), shp.getFcaDeliveryDate())
				&& TestUtil.nullableEquals(this.getInvoiceNumber(),shp.getInvoiceNumber())
				&& this.getSellerCompanyKcode().equals(shp.getSellerCompanyKcode())
				&& this.getBuyerCompanyKcode().equals(shp.getBuyerCompanyKcode())
				&& this.getCurrency().equals(shp.getCurrency())
				&& TestUtil.nullableEquals(this.getSalesTaxPercentage(),shp.getSalesTaxPercentage())
				&& this.getStatus().equals(shp.getStatus())
				&& this.getDestinationCountry().equals(shp.getDestinationCountry())
				&& this.getShippingMethod().equals(shp.getShippingMethod())
				&& this.getDateCreated().equals(shp.getDateCreated())
				&& TestUtil.nullableEquals(this.getSubtotal(),shp.getSubtotal())
				&& TestUtil.nullableEquals(this.getSalesTax(),shp.getSalesTax())
				&& this.getTotal().equals(shp.getTotal())
				&& TestUtil.nullableEquals(this.getDatePurchased(),shp.getDatePurchased())
				&& TestUtil.nullableEquals(this.getInternalNote(),shp.getInternalNote())
				&& TestUtil.nullableEquals(this.getLineItems(),shp.getLineItems())
				&& TestUtil.nullableEquals(this.getSpecialRequest(),shp.getSpecialRequest())
				&& TestUtil.nullableEquals(this.getBoxesNeedRepackaging(),shp.getBoxesNeedRepackaging())
				&& TestUtil.nullableEquals(this.getRepackagingFee(),shp.getRepackagingFee())
				&& this.getRequiredPO().equals(shp.getRequiredPO())
				&& TestUtil.nullableEquals(this.getPONumber(),shp.getPoNumber());
		}
	    return false;
	}

	@Override
	public String toString() {
		return "ShpIvsImpl [getName()=" + getName() + ", getType()=" + getType() + ", getExpectedExportDate()="
				+ getExpectedExportDate() + ", getFcaDeliveryLocationId()=" + getFcaDeliveryLocationId()
				+ ", getFcaDeliveryDate()=" + getFcaDeliveryDate() + ", getInvoiceNumber()=" + getInvoiceNumber()
				+ ", getSellerCompanyKcode()=" + getSellerCompanyKcode() + ", getBuyerCompanyKcode()="
				+ getBuyerCompanyKcode() + ", getCurrency()=" + getCurrency() + ", getSalesTaxPercentage()="
				+ getSalesTaxPercentage() + ", getStatus()=" + getStatus() + ", getDestinationCountry()="
				+ getDestinationCountry() + ", getShippingMethod()=" + getShippingMethod() + ", getDateCreated()="
				+ getDateCreated() + ", getSubtotal()=" + getSubtotal() + ", getSalesTax()=" + getSalesTax()
				+ ", getTotal()=" + getTotal() + ", getDatePurchased()=" + getDatePurchased() + ", getInternalNote()="
				+ getInternalNote() + ", getSpecialRequest()=" + getSpecialRequest() + ", getBoxesNeedRepackaging()=" + getBoxesNeedRepackaging()
				+ ", getRepackagingFee()=" + getRepackagingFee() + ", getRequiredPO()=" + getRequiredPO()
				+ ", getPONumber()=" + getPONumber() + ", getLineItems()=" + getLineItems() + "]";
	}

	public void addLineItem(ShipmentIvsLineItem item){
		this.lineItems.add(item);
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
	public String getSalesTaxPercentage() {
		return this.taxRate;
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
	public ShippingMethod getShippingMethod() {
		return this.shippingMethod;
	}
	
	@Override
	public String getDateCreated() {
		return this.dateCreated;
	}
	
	@Override
	public String getSubtotal() {
		return this.subtotal;
	}
	
	@Override
	public String getSalesTax() {
		return this.salesTax;
	}
	
	@Override
	public String getTotal() {
		return this.total;
	}
	
	@Override
	public String getPaidTotal() {
		return this.paidTotal;
	}
	
	@Override
	public String getUnsName() {
		return null;
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

	public String getSpecialRequest() {
		return specialRequest;
	}

	public String getBoxesNeedRepackaging() {
		return boxesNeedRepackaging;
	}

	public String getRepackagingFee() {
		return repackagingFee;
	}

	public Boolean getRequiredPO() {
		return requiredPO;
	}

	public String getPONumber() {
		return PONumber;
	}

}