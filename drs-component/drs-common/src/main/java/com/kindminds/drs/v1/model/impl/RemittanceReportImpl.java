package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.RemittanceReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemittanceReportImpl implements RemittanceReport {
	
	private StatementInfo info;
	private List<RemittanceReportItem> items=null;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	@Override
	public String getDateStart() {
		return this.info.getDateStart();
	}
	@Override
	public String getDateEnd() {
		return this.info.getDateEnd();
	}
	@Override
	public String getIsurKcode() {
		return this.info.getIsurKcode();
	}
	@Override
	public String getCurrency() {
		Set<Currency> itemCurrencySet = this.getCurrencySet();
		if(itemCurrencySet.size()>1){
			return "N/A";
		}
		Currency c = (Currency)itemCurrencySet.toArray()[0];
		return c.name();
	}
	@Override
	public String getTotal() {
		Set<Currency> itemCurrencySet = this.getCurrencySet();
		if(itemCurrencySet.size()>1){
			return "(Multi Currency)";
		}
		Currency c = (Currency)itemCurrencySet.toArray()[0];
		BigDecimal total = BigDecimal.ZERO;
		if(this.items!=null){
			for(RemittanceReportItem item:this.items){
				total = total.add(item.getAmount());
			}
		}
		return total.setScale(c.getScale(), BigDecimal.ROUND_HALF_UP).toString();
	}
	@Override
	public List<RemittanceReportItem> getItems() {
		return this.items;
	}
	
	public void setItems(List<RemittanceReportItem> items){
		this.items = items;
	}
	private Set<Currency> getCurrencySet(){
		Set<Currency> currencySet = new HashSet<Currency>();
		for(RemittanceReportItem item:this.items){
			currencySet.add(item.getCurrency());
		}
		return currencySet;
	}
	@Override
	public String getRcvrKcode() {
		return this.info.getRcvrKcode();
	}

}
