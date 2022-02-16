package com.kindminds.drs.web.data.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;
import com.kindminds.drs.util.DateHelper;

public class DomesticTransactionImpl implements  DomesticTransaction{

	private Integer id;
	private String utcDate;
	private String ssdcKcode;
	private String ssdcName;
	private String splrKcode;
	private String splrName;
	private String invoiceNumber;
	private String taxPercentage;
	private Currency currency;
	private String amountSubtotal;
	private String amountTax;
	private String amountTotal;
	private Boolean isEditable;
	private List<DomesticTransactionLineItemImpl> lineItems = new ArrayList<>();
	
	@Override
	public String toString() {
		return "DomesticTransactionImpl [getId()=" + getId() + ", getUtcDate()=" + getUtcDate() + ", getSsdcKcode()="
				+ getSsdcKcode() + ", getSsdcName()=" + getSsdcName() + ", getSplrKcode()=" + getSplrKcode()
				+ ", getSplrName()=" + getSplrName() + ", getInvoiceNumber()=" + getInvoiceNumber()
				+ ", getTaxPercentage()=" + getTaxPercentage() + ", getCurrency()=" + getCurrency()
				+ ", getAmountSubtotal()=" + getAmountSubtotal() + ", getAmountTax()=" + getAmountTax()
				+ ", getAmountTotal()=" + getAmountTotal() + ", isEditable()=" + isEditable() + ", getLineItems()="
				+ getLineItems() + ", getLineItem()=" + getLineItem() + "]";
	}

	public DomesticTransactionImpl(){}
	
	public DomesticTransactionImpl(DomesticTransaction original){
		this.id = original.getId();
		this.utcDate = original.getUtcDate();
		this.ssdcKcode  = original.getSsdcKcode();
		this.ssdcName = original.getSsdcName();
		this.splrKcode = original.getSplrKcode();
		this.splrName = original.getSplrName();
		this.invoiceNumber = original.getInvoiceNumber();
		this.taxPercentage = original.getTaxPercentage();
		this.currency = original.getCurrency();
		this.amountSubtotal = original.getAmountSubtotal();
		this.amountTax = original.getAmountTax();
		this.amountTotal = original.getAmountTotal();
		this.isEditable = original.isEditable();
		for(DomesticTransactionLineItem origItem:original.getLineItems()){
			this.lineItems.add(new DomesticTransactionLineItemImpl(origItem));			
		}		
	}
		
	@Override
	public Integer getId() {		
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;		
	}
	
	@Override
	public String getUtcDate() {
		return this.utcDate;
	}

	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;		
	}
	
	@Override
	public String getSsdcKcode() {
		return this.ssdcKcode;
	}

	public void setSsdcKcode(String ssdcKcode) {
		this.ssdcKcode  = ssdcKcode;		
	}
	
	@Override
	public String getSsdcName() {
		return this.ssdcName;
	}

	@Override
	public String getSplrKcode() {
		return this.splrKcode;
	}

	public void setSplrKcode(String splrKcode) {
		this.splrKcode = splrKcode;		
	}
	
	@Override
	public String getSplrName() {		
		return this.splrName;
	}

	@Override
	public String getInvoiceNumber() {		
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;		
	}
		
	@Override
	public String getTaxPercentage() {		
		return this.taxPercentage;
	}

	public void setTaxPercentage(String taxPercentage) {
		this.taxPercentage = taxPercentage; 		
	}
	
	@Override
	public Currency getCurrency() {	
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;		
	}
	
	@Override
	public String getAmountSubtotal() {		
		return this.amountSubtotal;
	}

	public void setAmountSubtotal(String amountSubtotal) {
		this.amountSubtotal = amountSubtotal;		
	}
	
	@Override
	public String getAmountTax() {		
		return this.amountTax;
	}

	public void setAmountTax(String amountTax) {
		this.amountTax = amountTax;		
	}
		
	@Override
	public String getAmountTotal() {		
		return this.amountTotal;
	}

	@Override
	public Boolean isEditable() {
		return this.isEditable;
	}

	@Override
	public Date getTransactionDate() {
		return null;
	}

	@Override
	public List<DomesticTransactionLineItem> getLineItems() {
		List<DomesticTransactionLineItem> items = new ArrayList<DomesticTransactionLineItem>();
		for(DomesticTransactionLineItem item : this.lineItems){
			if(item.getItemKey()!= null){				
				items.add(item);
			}				
		}		
		return items;
	}

	public List<DomesticTransactionLineItemImpl> getLineItem() {
		return this.lineItems;
	}
	
	public void setLineItem(List<DomesticTransactionLineItemImpl> lineItem) {
		this.lineItems = lineItem;		
	}

	@Override
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}
	
}