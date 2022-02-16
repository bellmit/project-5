package com.kindminds.drs.web.data.dto;

import java.util.ArrayList;
import java.util.List;


import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;


import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;

public class UnifiedShipmentImpl implements ShipmentUns{
		
	private String name;
	private ShipmentType type;
	private String invoiceNumber;
	private String trackingNumber;
	private String fbaId;
	private Forwarder forwarder;
	private String sellerCompanyKcode;
	private String buyerCompanyKcode;
	private Currency currency;
	private ShipmentStatus status;
	private String exportDate;
	private String exportSrcCurrency;
	private String exportDstCurrency;	
	private String exportCurrencyExchangeRate;
	private String exportFxRateToEur;
	private String destinationReceivedDate;
	private String expectArrivalDate;	
	private String destinationCountry;
	private String actualDestination;
	private ShippingMethod shippingMethod;
	private String dateCreated;
	public List<UnifiedShipmentLineItemImpl> lineItems = new ArrayList<UnifiedShipmentLineItemImpl>();
	private String amountTotal;
	private String cifAmountTotal;
	
	public UnifiedShipmentImpl(){};
	
	public UnifiedShipmentImpl(ShipmentUns original){
		this.name = original.getName();
		this.type = original.getType();
		this.invoiceNumber = original.getInvoiceNumber(); 
		this.trackingNumber = original.getTrackingNumber();
		this.fbaId = original.getFbaId();
		this.forwarder = original.getForwarder();
		this.sellerCompanyKcode = original.getSellerCompanyKcode();
		this.buyerCompanyKcode = original.getBuyerCompanyKcode();
		this.currency = original.getCurrency();
		this.status = original.getStatus();
		this.exportDate = original.getExportDate();
		this.exportSrcCurrency = original.getExportSrcCurrency();
		this.exportDstCurrency = original.getExportDstCurrency();
		this.exportCurrencyExchangeRate = original.getExportCurrencyExchangeRate(); 
		this.exportFxRateToEur = original.getExportFxRateToEur();
		this.destinationReceivedDate = original.getDestinationReceivedDate();
		this.expectArrivalDate = original.getExpectArrivalDate();
		this.destinationCountry = original.getDestinationCountry();
		this.actualDestination = original.getActualDestination();
		this.shippingMethod = original.getShippingMethod();
		this.dateCreated = original.getDateCreated();
		this.amountTotal = original.getAmountTotal();   
		this.cifAmountTotal = original.getCifAmountTotal();
		for(ShipmentUnsLineItem origItem:original.getLineItems()){
			this.lineItems.add(new UnifiedShipmentLineItemImpl(origItem));			
		}	
	};
		
	@Override
	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;		
	}
	
	@Override
	public ShipmentType getType(){
		return this.type;
	}

	public void setType(ShipmentType type){
		this.type = type;		
	}
	
	@Override
	public String getInvoiceNumber(){
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber){
		this.invoiceNumber = invoiceNumber; 		
	}
	
	@Override
	public String getTrackingNumber() {
		return this.trackingNumber;
	}
	
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;		
	}
	
	@Override
	public String getFbaId() {
		return this.fbaId;
	}
	
	public void setFbaId(String fbaId) {
		this.fbaId = fbaId;		
	}
	
	@Override
	public Forwarder getForwarder() {
		return this.forwarder;
	}
	
	public void setForwarder(Forwarder forwarder) {
		this.forwarder = forwarder;
	}
	
	@Override
	public String getSellerCompanyKcode(){
		return this.sellerCompanyKcode;				
	}

	public void setSellerCompanyKcode(String sellerCompanyKcode){
		this.sellerCompanyKcode = sellerCompanyKcode;		
	}
	
	@Override
	public String getBuyerCompanyKcode(){
		return this.buyerCompanyKcode;  			
	}
	
	public void setBuyerCompanyKcode(String buyerCompanyKcode){
		this.buyerCompanyKcode = buyerCompanyKcode;		
	}
	
	@Override
	public Currency getCurrency(){
		return this.currency;
	}

	public void setCurrency(Currency currency){
		this.currency = currency;
	}
	
	@Override
	public ShipmentStatus getStatus(){
		return this.status;
	}

	public void setStatus(ShipmentStatus status){
		this.status = status;				
	}
		
	@Override
	public String getDestinationCountry(){
		return this.destinationCountry;		
	}

	public void setDestinationCountry(String destinationCountry){
		this.destinationCountry = destinationCountry;		
	}

	@Override
	public String getActualDestination() {
		return actualDestination;
	}

	public void setActualDestination(String actualDestination) {
		this.actualDestination = actualDestination;
	}
	
	@Override
	public ShippingMethod getShippingMethod(){
		return this.shippingMethod;		
	}

	public void setShippingMethod(ShippingMethod shippingMethod){
		this.shippingMethod = shippingMethod;		
	}
	
	@Override
	public String getDateCreated(){
		return this.dateCreated;
	}

	public void setDateCreated(String dateCreated){
		this.dateCreated = dateCreated;				
	}
	
	@Override
	public List<ShipmentUnsLineItem> getLineItems(){
		List<ShipmentUnsLineItem> items = new ArrayList<ShipmentUnsLineItem>();
		for (ShipmentUnsLineItem item : this.lineItems) {
			if(item.getSourceInventoryShipmentName() != null){
				items.add(item);
			}
		}
		return items;		
	}
	
	public List<UnifiedShipmentLineItemImpl> getLineItem(){
		return this.lineItems;				
	}
	
	public void setLineItem(List<UnifiedShipmentLineItemImpl> lineItem){
		this.lineItems = lineItem;				
	}
	
	@Override
	public String getAmountTotal(){
		return this.amountTotal;
	}
	
	public void setAmountTotal(String amountTotal){
		this.amountTotal = amountTotal;		
	}

	@Override
	public String getCifAmountTotal() {
		return cifAmountTotal;
	}

	public void setCifAmountTotal(String cifAmountTotal) {
		this.cifAmountTotal = cifAmountTotal;
	}

	@Override
	public String getExportSrcCurrency() {
		return this.exportSrcCurrency;
	}

	public void setExportSrcCurrency(String exportSrcCurrency) {
		this.exportSrcCurrency = exportSrcCurrency; 		
	}
	
	@Override
	public String getExportDstCurrency() {
		return this.exportDstCurrency;
	}
	
	public void setExportDstCurrency(String exportDstCurrency) {
		this.exportDstCurrency = exportDstCurrency;		
	}
	
	@Override
	public String getExportDate() {		
		return this.exportDate;
	}

	public void setExportDate(String exportDate) {
		this.exportDate = exportDate;				
	}	
	
	@Override
	public String getExportCurrencyExchangeRate() {
		return this.exportCurrencyExchangeRate;
	}
	
	public void setExportCurrencyExchangeRate(String exportCurrencyExchangeRate) {
		this.exportCurrencyExchangeRate = exportCurrencyExchangeRate;				
	}
	
	@Override
	public String getExportFxRateToEur() {
		return this.exportFxRateToEur;
	}
	
	public void setExportFxRateToEur(String exportFxRateToEur) {
		this.exportFxRateToEur = exportFxRateToEur;		
	}
	
	@Override
	public String getDestinationReceivedDate() {		
		return this.destinationReceivedDate;
	}
	
	public void setDestinationReceivedDate(String destinationReceivedDate) {
		this.destinationReceivedDate = destinationReceivedDate;		
	}
	
	@Override
	public String getExpectArrivalDate() {
		return this.expectArrivalDate;
	}
	
	public void setExpectArrivalDate(String expectArrivalDate) {
		this.expectArrivalDate = expectArrivalDate;
	}
			
}