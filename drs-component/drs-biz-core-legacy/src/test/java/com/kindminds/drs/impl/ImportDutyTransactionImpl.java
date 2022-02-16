package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;

import java.util.List;

public class ImportDutyTransactionImpl implements ImportDutyTransaction {
	
	private String unsName;
	private String utcDate;
	private String country;
	private String currency;
	private String total;
	private Boolean isEditable;
	private List<ImportDutyTransactionLineItem> lineItems;

	public ImportDutyTransactionImpl(
			String unsName,
			String utcDate,
			String country,
			String currency,
			String total,
			Boolean isEditable,
			List<ImportDutyTransactionLineItem> lineItems){
		this.unsName = unsName;
		this.utcDate = utcDate;
		this.country = country;
		this.currency = currency;
		this.total = total;
		this.isEditable = isEditable;
		this.lineItems = lineItems;
	}
	
	@Override
	public String toString() {
		return "ImportDutyImpl [getUnsName()=" + getUnsName() + ", getUtcDate()=" + getUtcDate() + ", getDstCountry()="
				+ getDstCountry() + ", getCurrency()=" + getCurrency() + ", getTotal()=" + getTotal()
				+ ", isEditable()=" + isEditable() + ", getLineItems()=" + getLineItems() + "]";
	}

	@Override 
	public boolean equals(Object obj){
		if(obj instanceof ImportDutyTransaction){
			ImportDutyTransaction duty = (ImportDutyTransaction)obj;
			return this.getUnsName().equals(duty.getUnsName())
				&& this.getUtcDate().equals(duty.getUtcDate())
				&& this.getDstCountry().equals(duty.getDstCountry())
				&& this.getCurrency().equals(duty.getCurrency())
				&& this.getTotal().equals(duty.getTotal())
				&& this.getLineItems().equals(duty.getLineItems());
		}
		return false;
	}

	@Override
	public String getUnsName() {
		return this.unsName;
	}

	@Override
	public String getUtcDate() {
		return this.utcDate;
	}

	@Override
	public String getDstCountry() {
		return this.country;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getTotal() {
		return this.total;
	}

	@Override
	public Boolean isEditable() {
		return this.isEditable;
	}

	@Override
	public List<ImportDutyTransactionLineItem> getLineItems() {
		return this.lineItems;
	}

}
