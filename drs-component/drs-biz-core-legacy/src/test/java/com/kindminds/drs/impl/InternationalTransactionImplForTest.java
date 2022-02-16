package com.kindminds.drs.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;

import java.util.List;

public class InternationalTransactionImplForTest implements InternationalTransaction {

	private Integer id;
	private String utcDate;
	private Integer cashFlowDirectionKey;
	private String msdcKcode;
	private String msdcName;
	private String ssdcKcode;
	private String ssdcName;
	private String splrKcode;
	private String splrName;
	private Currency currency;
	private String total;
	private String note;
	private Boolean isEditable;
	private List<InternationalTransactionLineItem> lineItems;

	public InternationalTransactionImplForTest(
			Integer id,
			String utcDate,
			Integer cashFlowDirectionKey,
			String msdcKcode,
			String msdcName,
			String ssdcKcode,
			String ssdcName,
			String splrKcode,
			String splrName,
			String currency,
			String total,
			String note,
			List<InternationalTransactionLineItem> lineItems){
		this.id=id;
		this.utcDate=utcDate;
		this.cashFlowDirectionKey=cashFlowDirectionKey;
		this.msdcKcode=msdcKcode;
		this.msdcName=msdcName;
		this.ssdcKcode=ssdcKcode;
		this.ssdcName=ssdcName;
		this.splrKcode=splrKcode;
		this.splrName=splrName;
		this.currency=Currency.valueOf(currency);
		this.total=total;
		this.note=note;
		this.lineItems=lineItems;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;
	}
	
	public void setMsdcKcode(String msdcKcode) {
		this.msdcKcode = msdcKcode;
	}
	
	public void setMsdcName(String msdcName) {
		this.msdcName = msdcName;
	}
	
	public void setSplrKcode(String splrKcode) {
		this.splrKcode = splrKcode;
	}
	
	public void setSplrName(String splrName) {
		this.splrName = splrName;
	}
	
	public void setTotal(String total) {
		this.total = total;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof InternationalTransaction){
			InternationalTransaction t = (InternationalTransaction)obj;
			return this.getId().equals(t.getId())
				&& this.getUtcDate().equals(t.getUtcDate())
				&& this.getCashFlowDirectionKey().equals(t.getCashFlowDirectionKey())
				&& this.getMsdcKcode().equals(t.getMsdcKcode())
				&& this.getMsdcName().equals(t.getMsdcName())
				&& this.getSsdcKcode().equals(t.getSsdcKcode())
				&& this.getSsdcName().equals(t.getSsdcName())
				&& this.getSplrKcode().equals(t.getSplrKcode())
				&& this.getSplrName().equals(t.getSplrName())
				&& this.getCurrency().equals(t.getCurrency())
				&& this.getTotal().equals(t.getTotal())
				&& this.getNote().equals(t.getNote())
				&& this.isEditable().equals(t.isEditable())
				&& this.getLineItems().equals(t.getLineItems());
		}
		return false;
	}

	@Override
	public String toString() {
		return "InternationalTransactionImpl [getId()=" + getId() + ", getUtcDate()=" + getUtcDate()
				+ ", getCashFlowDirectionKey()=" + getCashFlowDirectionKey() + ", getMsdcKcode()=" + getMsdcKcode()
				+ ", getMsdcName()=" + getMsdcName() + ", getSsdcKcode()=" + getSsdcKcode() + ", getSsdcName()="
				+ getSsdcName() + ", getSplrKcode()=" + getSplrKcode() + ", getSplrName()=" + getSplrName()
				+ ", getCurrency()=" + getCurrency() + ", getTotal()=" + getTotal() + ", getNote()=" + getNote()
				+ ", isEditable()=" + isEditable() + ", getLineItems()=" + getLineItems() + "]";
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
	public Integer getCashFlowDirectionKey(){
		return this.cashFlowDirectionKey;
	}

	@Override
	public String getMsdcKcode() {
		return this.msdcKcode;
	}

	@Override
	public String getMsdcName() {
		return this.msdcName;
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
	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public String getTotal() {
		return this.total;
	}

	@Override
	public String getNote() {
		return this.note;
	}

	@Override
	public Boolean isEditable() {
		return this.isEditable;
	}

	@Override
	public List<InternationalTransactionLineItem> getLineItems() {
		return this.lineItems;
	}

}
