package com.kindminds.drs.web.data.dto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.Currency;


import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;



public class InventoryShipmentImpl {
	
	private String name;
	private ShipmentType type;
	private String expectedExportDate;
	private String fcaDeliveryLocationId;
	private String fcaDeliveryDate;	
	private String specialRequest;
	private String boxesNeedRepackaging;
	private String repackagingFee;
	private Boolean requiredPO;
	private String PONumber;	
	private String invoiceNumber;
	private String sellerCompanyKcode;
	private String buyerCompanyKcode;
	private Currency currency;
	private String salesTaxPercentage;
	private ShipmentStatus status;
	private String destinationCountry;
	private ShippingMethod shippingMethod;
	private String dateCreated;

	public List<IvsLineItem> lineItemList = new ArrayList<IvsLineItem>();
	public List<InventoryShipmentLineItemImpl> lineItems = new ArrayList<InventoryShipmentLineItemImpl>();

	private String subtotal;
	private String salesTax;
	private String total;
	private String paidTotal;
	private String unsName;
	private String datePurchased;
	private String internalNote;
	
	public InventoryShipmentImpl(){};
	
	public InventoryShipmentImpl(ShipmentIvs original) throws IOException{
		this.name = original.getName();
		this.type = original.getType();
		this.expectedExportDate = original.getExpectedExportDate();
		this.fcaDeliveryLocationId = original.getFcaDeliveryLocationId();
		this.fcaDeliveryDate = original.getFcaDeliveryDate();
		this.specialRequest = original.getSpecialRequest(); 
		this.boxesNeedRepackaging = original.getBoxesNeedRepackaging();
		this.repackagingFee = original.getRepackagingFee();
		this.requiredPO = original.getRequiredPO();
		this.PONumber = original.getPONumber();
		this.invoiceNumber = original.getInvoiceNumber();
		this.sellerCompanyKcode = original.getSellerCompanyKcode();
		this.buyerCompanyKcode = original.getBuyerCompanyKcode();
		this.currency = original.getCurrency();
		this.salesTaxPercentage = original.getSalesTaxPercentage();
		this.status = original.getStatus();
		this.destinationCountry = original.getDestinationCountry();
		this.shippingMethod = original.getShippingMethod();
		this.dateCreated = original.getDateCreated();
		this.subtotal = original.getSubtotal();
		this.salesTax = original.getSalesTax();
		this.total = original.getTotal();
		this.paidTotal = original.getPaidTotal();
		this.unsName = original.getUnsName();
		this.datePurchased = original.getDatePurchased();
		this.internalNote = original.getInternalNote();
		for(ShipmentIvs.ShipmentIvsLineItem origItem:original.getLineItems()){
			this.lineItems.add(new InventoryShipmentLineItemImpl(origItem));			
		}		
	};

	//todo arthur
	/*
	@Override
	public String toString() {
		return "InventoryShipmentImpl [getName()=" + getName() + ", getType()=" + getType()
				+ ", getExpectedExportDate()=" + getExpectedExportDate() + ", getFcaDeliveryLocationId()="
				+ getFcaDeliveryLocationId() + ", getFcaDeliveryDate()=" + getFcaDeliveryDate()
				+ ", getSpecialRequest()=" + getSpecialRequest() + ", getBoxesNeedRepackaging()="
				+ getBoxesNeedRepackaging() + ", getRepackagingFee()=" + getRepackagingFee() + ", getRequiredPO()="
				+ getRequiredPO() + ", getPONumber()=" + getPONumber() + ", getInvoiceNumber()=" + getInvoiceNumber()
				+ ", getSellerCompanyKcode()=" + getSellerCompanyKcode() + ", getBuyerCompanyKcode()="
				+ getBuyerCompanyKcode() + ", getCurrency()=" + getCurrency() + ", getSalesTaxPercentage()="
				+ getSalesTaxPercentage() + ", getStatus()=" + getStatus() + ", getDestinationCountry()="
				+ getDestinationCountry() + ", getShippingMethod()=" + getShippingMethod() + ", getDateCreated()="
				+ getDateCreated() + ", getLineItems()=" + getLineItems()
				+ ", getSubtotal()=" + getSubtotal() + ", getSalesTax()=" + getSalesTax() + ", getTotal()=" + getTotal()
				+ ", getPaidTotal()=" + getPaidTotal() + ", getUnsName()=" + getUnsName() + ", getDatePurchased()="
				+ getDatePurchased() + ", getInternalNote()=" + getInternalNote() + "]";
	}
	*/
		

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;		
	}
	

	public ShipmentType getType(){
		return this.type;
	}

	public void setType(ShipmentType type){
		this.type = type;		
	}
	

	public String getExpectedExportDate() {		
		return this.expectedExportDate;
	}

	public void setExpectedExportDate(String expectedExportDate) {
		this.expectedExportDate = expectedExportDate;		
	}
	

	public String getFcaDeliveryLocationId() {		
		return this.fcaDeliveryLocationId;
	}

	public void setFcaDeliveryLocationId(String fcaDeliveryLocationId) {
		this.fcaDeliveryLocationId = fcaDeliveryLocationId; 		
	}
		

	public String getFcaDeliveryDate() {		
		return this.fcaDeliveryDate;
	}
	
	public void setFcaDeliveryDate(String fcaDeliveryDate) {
		this.fcaDeliveryDate = fcaDeliveryDate; 		
	}
	

	public String getSpecialRequest() {
		return this.specialRequest;
	}
	
	public void setSpecialRequest(String specialRequest) {
		this.specialRequest = specialRequest; 
	}
		

	public String getBoxesNeedRepackaging() {
		return this.boxesNeedRepackaging;
	}

	public void setBoxesNeedRepackaging(String boxesNeedRepackaging) {
		this.boxesNeedRepackaging = boxesNeedRepackaging; 
	}
		

	public String getRepackagingFee() {
		return this.repackagingFee;
	}
	
	public void setRepackagingFee(String repackagingFee) {
		this.repackagingFee = repackagingFee; 
	}
		

	public Boolean getRequiredPO() {
		return this.requiredPO;
	}
	
	public void setRequiredPO(Boolean requiredPO) {
		this.requiredPO = requiredPO; 
	}
		

	public String getPONumber() {
		return this.PONumber;
	}
	
	public void setPONumber(String PONumber) {
		this.PONumber = PONumber; 
	}
			

	public String getInvoiceNumber(){
		return this.invoiceNumber;
	}
	
	public void setInvoiceNumber(String invoiceNumber){
		this.invoiceNumber = invoiceNumber; 		
	}
		

	public String getSellerCompanyKcode(){
		return this.sellerCompanyKcode;				
	}

	public void setSellerCompanyKcode(String sellerCompanyKcode){
		this.sellerCompanyKcode = sellerCompanyKcode;		
	}
	

	public String getBuyerCompanyKcode(){
		return this.buyerCompanyKcode;  			
	}
	
	public void setBuyerCompanyKcode(String buyerCompanyKcode){
		this.buyerCompanyKcode = buyerCompanyKcode;		
	}
	

	public Currency getCurrency(){
		return this.currency;
	}

	public void setCurrency(Currency currency){
		this.currency = currency;
	}


	public String getSalesTaxPercentage(){
		return this.salesTaxPercentage;
	}

	public void setSalesTaxPercentage(String salesTaxPercentage){
		this.salesTaxPercentage = salesTaxPercentage;		
	}
	

	public ShipmentStatus getStatus(){
		return this.status;
	}

	public void setStatus(ShipmentStatus status){
		this.status = status;				
	}
		

	public String getDestinationCountry(){
		return this.destinationCountry;		
	}

	public void setDestinationCountry(String destinationCountry){
		this.destinationCountry = destinationCountry;		
	}
	

	public ShippingMethod getShippingMethod(){
		return this.shippingMethod;		
	}

	public void setShippingMethod(ShippingMethod shippingMethod){
		this.shippingMethod = shippingMethod;		
	}
	

	public String getDateCreated(){
		return this.dateCreated;
	}

	public void setDateCreated(String dateCreated){
		this.dateCreated = dateCreated;				
	}
	

	//todo arthur
	/*
	public List<IvsLineItem> getLineItems(){
		if (lineItemList.isEmpty()) {
			for (IvsLineItem item : this.lineItems) {
				if(item.getSkuCode() != null){
					lineItemList.add(item);
				}
			}
		}
		return this.lineItemList;		
	}*/
	
	public List<InventoryShipmentLineItemImpl> getLineItem(){
		return this.lineItems;				
	}
	
	public void setLineItem(List<InventoryShipmentLineItemImpl> lineItem){
		this.lineItems = lineItem;
	}
	

	public String getSubtotal(){
		return this.subtotal;
	}

	public void setSubtotal(String subtotal){
		this.subtotal = subtotal;		
	}
	

	public String getSalesTax(){
		return this.salesTax;
	}

	public void setSalesTax(String salesTax){
		this.salesTax = salesTax;		
	}
	

	public String getTotal(){
		return this.total;
	}
	
	public void setTotal(String total){
		this.total = total;		
	}


	public String getPaidTotal() {
		return this.paidTotal;
	}
	

	public String getUnsName() {
		return this.unsName;
	}


	public String getDatePurchased() {
		return this.datePurchased;
	}

	public void setDatePurchased(String datePurchased) {
		this.datePurchased = datePurchased;		
	}
		
	
	public String getInternalNote() {
		return this.internalNote;
	}

	public void setInternalNote(String internalNote) {
		this.internalNote = internalNote;		
	}


}