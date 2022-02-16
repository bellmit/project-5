package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.Currency;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomesticTransactionImpl implements DomesticTransaction {
	
	private int id;
	private Date transactionDate;
	private String ssdcKcode;
	private String ssdcName;
	private String splrKcode;
	private String splrName;
	private String invoiceNumber;
	private BigDecimal taxRate;
	private Integer currencyId;
	private BigDecimal amountSubtotal;
	private BigDecimal amountTax;
	private BigDecimal amountTotal;
	private Boolean isEditable;
	private List<DomesticTransactionLineItem> lineItems;

	public void setId(int id) {
		this.id = id;
	}
	
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setSsdcKcode(String ssdcKcode) {
		this.ssdcKcode = ssdcKcode;
	}

	public void setSsdcName(String ssdcName) {
		this.ssdcName = ssdcName;
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
	
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	
	public void setAmountSubtotal(BigDecimal amountSubtotal) {
		this.amountSubtotal = amountSubtotal;
	}
	
	public void setAmountTax(BigDecimal amountTax) {
		this.amountTax = amountTax;
	}
	
	public void setAmountTotal(BigDecimal amountTotal) {
		this.amountTotal = amountTotal;
	}

	@Override
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public void setLineItems(List<Object []> columnsList) {

		List<DomesticTransactionLineItem> result = new ArrayList<>();
		for(Object[] columns:columnsList){
			DomesticTransactionLineItemImpl item = new DomesticTransactionLineItemImpl();
			item.setLineSeq((Integer)columns[0]);
			item.setItemKey((Integer)columns[1]);
			item.setItemName((String)columns[2]);
			item.setNote((String)columns[3]);
			item.setAmount((BigDecimal)columns[4]);
			result.add(item);
		}


		this.lineItems = result;
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
		return DateHelper.toString(this.transactionDate,"yyyy-MM-dd","UTC");
	}

	@Override
	public Date getTransactionDate() {
		return this.transactionDate;
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
	public String getTaxPercentage(){
		if(this.taxRate==null) return null;
		BigDecimal taxPercentage = this.taxRate.multiply(new BigDecimal("100"));
		return taxPercentage.stripTrailingZeros().toPlainString();
	}

	@Override
	public Currency getCurrency() {
		return Currency.fromKey(this.currencyId);
	}
	
	@Override
	public String getAmountSubtotal() {
		return this.amountSubtotal==null?null:this.amountSubtotal.setScale(this.getCurrency().getScale()).toPlainString();
	}
	
	@Override
	public String getAmountTax() {
		return this.amountTax==null?null:this.amountTax.setScale(this.getCurrency().getScale()).toPlainString();
	}

	@Override
	public String getAmountTotal() {
		return this.amountTotal.setScale(this.getCurrency().getScale()).toPlainString();
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
