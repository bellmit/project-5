package com.kindminds.drs.web.data.dto;

import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;

public class ImportDutyTransactionImpl implements ImportDutyTransaction{

	private String unsName;
	private String utcDate;	
	private String dstCountry;
	private String currency;	
	private String total;
	private Boolean isEditable;
	public List<ImportDutyTransactionLineItemImpl> lineItems = new ArrayList<ImportDutyTransactionLineItemImpl>();
		
	@Override
	public String getUnsName() {		
		return this.unsName;
	}

	public void setUnsName(String unsName) {
		this.unsName = unsName;		
	}
		
	@Override
	public String getUtcDate() {		
		return this.utcDate;
	}

	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;		
	}
	
	@Override
	public String getDstCountry() {		
		return this.dstCountry;
	}

	public void setDstCountry(String dstCountry) {
		this.dstCountry = dstCountry;		
	}
	
	@Override
	public String getCurrency() {		
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;		
	}
	
	@Override
	public String getTotal() {		
		return this.total;
	}

	public void setTotal(String total) {
		this.total = total;		
	}
			
	@Override
	public Boolean isEditable() {		
		return this.isEditable;
	}

	@Override
	public List<ImportDutyTransactionLineItem> getLineItems() {		
		List<ImportDutyTransactionLineItem> items = new ArrayList<ImportDutyTransactionLineItem>();
		for(ImportDutyTransactionLineItem item : this.lineItems){
			if(item.getSourceIvsName() != null){
			items.add(item);
			}
		}		
		return items;
	}
	
	public List<ImportDutyTransactionLineItemImpl> getLineItem(){
		return this.lineItems;				
	}
	
	public void setLineItem(List<ImportDutyTransactionLineItemImpl> lineItems){
		this.lineItems = lineItems;
		
	}
		
}