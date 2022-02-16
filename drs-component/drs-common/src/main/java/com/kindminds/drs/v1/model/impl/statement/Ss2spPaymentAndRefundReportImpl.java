package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ss2spPaymentAndRefundReportImpl implements Ss2spPaymentAndRefundReport{
	
	private String title;
	private StatementInfo info;
	private Currency currency;
	private List<Ss2spPaymentAndRefundReportItem> lineItems=null;
	
	public void setTitle(String value){ this.title=value; }
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	@Override
	public String getTitle() {
		return this.title;
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
	public String getRcvrKcode() {
		return this.info.getRcvrKcode();
	}
	
	@Override
	public Currency getCurrency() {
		Assert.notNull(this.currency);
		return this.currency;
	}
	@Override
	public List<Ss2spPaymentAndRefundReportItem> getLineItems() {
		return this.lineItems;
	}
	@Override
	public String getTotal() {
		Set<Currency> itemCurrencySet = this.getCurrencySet();
		if(itemCurrencySet.size()>1){
			return "N/A (Multi Currency)";
		}
		Currency c = (Currency)itemCurrencySet.toArray()[0];
		BigDecimal total=BigDecimal.ZERO;
		if(this.lineItems!=null){
			for(Ss2spPaymentAndRefundReportItem item:lineItems){
				total = total.add(item.getNumericTotalAmount());
			}
		}
		return total.setScale(c.getScale(), BigDecimal.ROUND_HALF_UP).toString();
	}
	public void setLineItems(List<Ss2spPaymentAndRefundReportItem> lineItems){
		this.lineItems=lineItems;
	}
	
	@Override
	public String toString() {
		return "Ss2spPaymentAndRefundReportImplForTest [getTotal()="
				+ getTotal() + ", getLineItems()=" + getLineItems() + "]";
	}
	
	public void setCurrency(Currency c){
		this.currency = c;
	}
	private Set<Currency> getCurrencySet(){
		Set<Currency> currencySet = new HashSet<Currency>();
		for(Ss2spPaymentAndRefundReportItem item:this.lineItems){
			currencySet.add(item.getCurrency());
		}
		return currencySet;
	}
	
}
