package com.kindminds.drs.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;
import com.kindminds.drs.util.DateHelper;

import java.util.Date;
import java.util.List;

public class DomesticTransactionImpl implements DomesticTransaction {

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
	private List<DomesticTransactionLineItem> lineItems;

	public DomesticTransactionImpl(
			Integer id,
			String utcDate,
			String ssdcKcode,
			String ssdcName,
			String splrKcode,
			String splrName,
			String invoiceNumber,
			String taxPercentage,
			String currency,
			String amountSubtotal,
			String amountTax,
			String amountTotal,
			List<DomesticTransactionLineItem> lineItems){
		this.id=id;
		this.utcDate=utcDate;
		this.ssdcKcode=ssdcKcode;
		this.ssdcName=ssdcName;
		this.splrKcode=splrKcode;
		this.splrName=splrName;
		this.invoiceNumber = invoiceNumber;
		this.taxPercentage = taxPercentage;
		this.currency=Currency.valueOf(currency);
		this.amountSubtotal = amountSubtotal;
		this.amountTax = amountTax;
		this.amountTotal = amountTotal;
		this.lineItems=lineItems;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof DomesticTransaction){
			DomesticTransaction t = (DomesticTransaction)obj;
			return this.getId().equals(t.getId())
				&& this.getUtcDate().equals(t.getUtcDate())
				&& this.getSsdcKcode().equals(t.getSsdcKcode())
				&& this.getSsdcName().equals(t.getSsdcName())
				&& this.getSplrKcode().equals(t.getSplrKcode())
				&& this.getSplrName().equals(t.getSplrName())
				&& this.getInvoiceNumber().equals(t.getInvoiceNumber())
				&& this.getCurrency().equals(t.getCurrency())
				&& this.getAmountSubtotal().equals(t.getAmountSubtotal())
				&& this.getAmountTax().equals(t.getAmountTax())
				&& this.getAmountTotal().equals(t.getAmountTotal())
				&& this.isEditable().equals(t.isEditable())
				&& this.getLineItems().equals(t.getLineItems());
		}
		return false;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;
	}
	
	public void setSplrKcode(String splrKcode) {
		this.splrKcode = splrKcode;
	}
	
	public void setSplrName(String splrName) {
		this.splrName = splrName;
	}
	
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public void setTaxPercentage(String taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	
	public void setAmountSubtotal(String amountSubtotal) {
		this.amountSubtotal = amountSubtotal;
	}
	
	public void setAmountTax(String amountTax) {
		this.amountTax = amountTax;
	}
	
	public void setAmountTotal(String amountTotal) {
		this.amountTotal = amountTotal;
	}

	@Override
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	@Override
	public String toString() {
		return "DomesticTransactionImpl [getId()=" + getId() + ", getUtcDate()=" + getUtcDate() + ", getSsdcKcode()="
				+ getSsdcKcode() + ", getSsdcName()=" + getSsdcName() + ", getSplrKcode()=" + getSplrKcode()
				+ ", getSplrName()=" + getSplrName() + ", getInvoiceNumber()=" + getInvoiceNumber()
				+ ", getTaxPercentage()=" + getTaxPercentage() + ", getCurrency()=" + getCurrency()
				+ ", getAmountSubtotal()=" + getAmountSubtotal() + ", getAmountTax()=" + getAmountTax()
				+ ", getAmountTotal()=" + getAmountTotal() + ", isEditable()=" + isEditable() + ", getLineItems()="
				+ getLineItems() + "]";
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public String getUtcDate() {
		return this.utcDate;
	}

	@Override
	public Date getTransactionDate() {
		return DateHelper.toDate(utcDate, "yyyy-MM-dd");
	}

	@Override
	public String getSsdcKcode() {
		return this.ssdcKcode;
	}

	@Override
	public String getSsdcName() {
		return this.ssdcName;
	}

	@Override
	public String getSplrKcode() {
		return this.splrKcode;
	}

	@Override
	public String getSplrName() {
		return this.splrName;
	}
	
	@Override
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}
	
	@Override
	public String getTaxPercentage() {
		return this.taxPercentage;
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}
	
	@Override
	public String getAmountSubtotal() {
		return this.amountSubtotal;
	}
	
	@Override
	public String getAmountTax() {
		return this.amountTax;
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
	public List<DomesticTransactionLineItem> getLineItems() {
		return this.lineItems;
	}

}
