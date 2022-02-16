package com.kindminds.drs.web.view.settlement;

import java.io.Serializable;
import java.util.List;

import com.kindminds.drs.Currency;

public class InternationalTransaction implements Serializable{
	
	private static final long serialVersionUID = -4529009419780563640L;
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
	private Boolean editable;
	private List<InternationalTransactionLineItem> lineItems = null;
		
	@Override
	public String toString() {
		return "InternationalTransaction [getId()=" + getId() + ", getUtcDate()=" + getUtcDate()
				+ ", getCashFlowDirectionKey()=" + getCashFlowDirectionKey() + ", getMsdcKcode()=" + getMsdcKcode()
				+ ", getMsdcName()=" + getMsdcName() + ", getSsdcKcode()=" + getSsdcKcode() + ", getSsdcName()="
				+ getSsdcName() + ", getSplrKcode()=" + getSplrKcode() + ", getSplrName()=" + getSplrName()
				+ ", getCurrency()=" + getCurrency() + ", getTotal()=" + getTotal() + ", getNote()=" + getNote()
				+ ", getEditable()=" + getEditable() + ", getLineItems()=" + getLineItems() + "]";
	}
	
	public Integer getId(){
		return this.id;
	};
	
	public void setId(Integer id){
		this.id = id;
	}
		
	public String getUtcDate(){
		return this.utcDate;
	};
	
	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;		
	}
	
	public Integer getCashFlowDirectionKey(){
		return this.cashFlowDirectionKey;
	};
	
	public void setCashFlowDirectionKey(Integer cashFlowDirectionKey) {
		this.cashFlowDirectionKey = cashFlowDirectionKey;		
	}
	
	public String getMsdcKcode(){
		return this.msdcKcode;
	};

	public void setMsdcKcode(String msdcKcode) {
		this.msdcKcode = msdcKcode;		
	}
	
	public String getMsdcName(){
		return this.msdcName;
	};
	
	public void setMsdcName(String msdcName){
		this.msdcName = msdcName;
	}
		
	public String getSsdcKcode(){
		return this.ssdcKcode;
	};
	
	public void setSsdcKcode(String ssdcKcode) {
		this.ssdcKcode  = ssdcKcode;		
	}
		
	public String getSsdcName(){
		return this.ssdcName;
	};
	
	public void setSsdcName(String ssdcName){
		this.ssdcName = ssdcName;
	};
		
	public String getSplrKcode(){
		return this.splrKcode;
	};
	
	public void setSplrKcode(String splrKcode){
		this.splrKcode = splrKcode;
	};
		
	public String getSplrName(){
		return this.splrName;
	};
	
	public void setSplrName(String splrName){
		this.splrName = splrName;
	};
		
	public Currency getCurrency(){
		return this.currency;
	};
	
	public void setCurrency(Currency currency){
		this.currency = currency;
	};
		
	public String getTotal(){
		return this.total;
	};
	
	public void setTotal(String total){
		this.total = total;
	};
		
	public String getNote(){
		return this.note;
	};
	
	public void setNote(String note){
		this.note = note;
	};
		
	public Boolean getEditable(){
		return this.editable;
	};
	
	public void setEditable(Boolean editable){
		this.editable = editable;
	};
		
	public List<InternationalTransactionLineItem> getLineItems(){
		return this.lineItems;
	};
	
	public void setLineItems(List<InternationalTransactionLineItem> lineItems) {
		this.lineItems = lineItems;		
	}
		
}