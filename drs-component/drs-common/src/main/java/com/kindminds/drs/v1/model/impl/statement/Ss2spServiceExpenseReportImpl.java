package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spServiceExpenseReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Ss2spServiceExpenseReportImpl implements Ss2spServiceExpenseReport {
	
	private StatementInfo info;
	private Currency currency=null;
	private BigDecimal taxRate=null;
	private List<Ss2spServiceExpenseReportItem> items=null;
	
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
	public String getRcvrKcode() {
		return this.info.getRcvrKcode();
	}

	@Override
	public Currency getCurrency() {
		Assert.notNull(this.currency,"NULL CURRENCY");
		return this.currency;
	}

	@Override
	public String getSubtotal() {
		BigDecimal subtotal = BigDecimal.ZERO;
		for(Ss2spServiceExpenseReportItem item:this.getItems()){
			subtotal = subtotal.add(item.getAmountTotal());
		}
		return subtotal.setScale(2,RoundingMode.HALF_UP).toString();
	}

	@Override
	public String getTax() {
		BigDecimal subtotal = BigDecimal.ZERO;
		for(Ss2spServiceExpenseReportItem item:this.getItems()){
			subtotal = subtotal.add(item.getAmountTotal());
		}
		return subtotal.multiply(this.taxRate).setScale(this.getCurrency().getScale(), RoundingMode.HALF_UP).toString();
	}

	@Override
	public String getTotal() {
		BigDecimal subtotal = BigDecimal.ZERO;
		for(Ss2spServiceExpenseReportItem item:this.getItems()){
			subtotal = subtotal.add(item.getAmountTotal());
		}
		BigDecimal taxAmount = subtotal.multiply(this.taxRate).setScale(this.getCurrency().getScale(), RoundingMode.HALF_UP);
		return subtotal.add(taxAmount).setScale(this.getCurrency().getScale(), RoundingMode.HALF_UP).toString();
	}
	
	@Override
	public List<Ss2spServiceExpenseReportItem> getItems() {
		return this.items;
	}
	
	public void setCurrency(Currency c){
		this.currency = c;
	}
	
	public void setTaxRate(BigDecimal rate){
		this.taxRate = rate;
	}
	
	public void setItems(List<Ss2spServiceExpenseReportItem> items){
		this.items = items;
	}

	
}
