package com.kindminds.drs.web.data.dto;



import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;

import java.util.ArrayList;
import java.util.List;




public class InternationalTransactionImpl implements InternationalTransaction {
		
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
	private List<InternationalTransactionLineItemImpl> lineItems = new ArrayList<>();
	
	public InternationalTransactionImpl(){};
	
	public InternationalTransactionImpl(InternationalTransaction original){
		this.id = original.getId();
		this.utcDate = original.getUtcDate();
		this.cashFlowDirectionKey = original.getCashFlowDirectionKey();
		this.msdcKcode = original.getMsdcKcode();
		this.msdcName = original.getMsdcName();
		this.ssdcKcode = original.getSsdcKcode();
		this.ssdcName = original.getSsdcName();
		this.splrKcode = original.getSplrKcode();
		this.splrName = original.getSplrName();
		this.currency = original.getCurrency();
		this.total = original.getTotal();
		this.note = original.getNote();
		this.isEditable = original.isEditable();
		for(InternationalTransactionLineItem origItem:original.getLineItems()){
			this.lineItems.add(new InternationalTransactionLineItemImpl(origItem));
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
	public Integer getCashFlowDirectionKey() {		
		return this.cashFlowDirectionKey;
	}
	
	public void setCashFlowDirectionKey(Integer cashFlowDirectionKey) {
		this.cashFlowDirectionKey = cashFlowDirectionKey;		
	}
		
	@Override
	public String getMsdcKcode() {
		return this.msdcKcode;
	}
	
	public void setMsdcKcode(String msdcKcode) {
		this.msdcKcode = msdcKcode;		
	}
	
	@Override
	public String getMsdcName() {
		return this.msdcName;
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
	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
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
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;		
	}
		
	@Override
	public Boolean isEditable() {
		return this.isEditable;
	}
	
	@Override
	public List<InternationalTransactionLineItem> getLineItems() {
		List<InternationalTransactionLineItem> items = new ArrayList<InternationalTransactionLineItem>();
		for (InternationalTransactionLineItem item : this.lineItems) {
			if(item.getItemKey()!= null){				
				items.add(item);
			}				
		}
		return items;
	}
	
	public List<InternationalTransactionLineItemImpl> getLineItem() {
		return this.lineItems;
	}
	
	public void setLineItem(List<InternationalTransactionLineItemImpl> lineItem) {
		this.lineItems = lineItem;		
	}
	
}