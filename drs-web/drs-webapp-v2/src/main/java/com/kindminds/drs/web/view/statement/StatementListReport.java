package com.kindminds.drs.web.view.statement;



import java.io.Serializable;
import java.util.List;

import com.kindminds.drs.Currency;


public class StatementListReport implements
		Serializable {

	private static final long serialVersionUID = 278652511097777938L;
	
	private Currency currency;
	private List<StatementListReportItem> items = null;
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public void setItems(List<StatementListReportItem> items) {
		this.items = items;
	}

	
	public String getCurrency() {
		return this.currency.name();
	}
	
	
	public List<StatementListReportItem> getItems() {
		return this.items;
	}
	




}
