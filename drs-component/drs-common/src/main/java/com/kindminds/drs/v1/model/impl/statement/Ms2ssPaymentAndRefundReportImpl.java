package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ms2ssPaymentAndRefundReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ms2ssPaymentAndRefundReportImpl implements Ms2ssPaymentAndRefundReport {

	private StatementInfo info;
	private Currency currency;
	private List<Ms2ssPaymentAndRefundReportItem> lineItems=null;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setCurrency(Currency c){
		this.currency = c;
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
	public List<Ms2ssPaymentAndRefundReportItem> getLineItems() {
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
			for(Ms2ssPaymentAndRefundReportItem item:lineItems){
				total = total.add(item.getNumericTotalAmount());
			}
		}
		return total.setScale(c.getScale(), BigDecimal.ROUND_HALF_UP).toString();
	}
	public void setLineItems(List<Ms2ssPaymentAndRefundReportItem> lineItems){
		this.lineItems=lineItems;
	}
	@Override
	public String toString() {
		return "Ss2spPaymentAndRefundReportImplForTest [getTotal()="
				+ getTotal() + ", getLineItems()=" + getLineItems() + "]";
	}
	
	private Set<Currency> getCurrencySet(){
		Set<Currency> currencySet = new HashSet<Currency>();
		for(Ms2ssPaymentAndRefundReportItem item:this.lineItems){
			currencySet.add(item.getCurrency());
		}
		return currencySet;
	}

}
