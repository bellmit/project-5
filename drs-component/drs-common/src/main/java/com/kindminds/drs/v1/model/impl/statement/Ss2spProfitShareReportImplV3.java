package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Ss2spProfitShareReportImplV3 implements Ss2spProfitShareReport {
	
	private static final int versionNumber = 3;
	
	private StatementInfo info;
	private Currency statementCurrency=null;
	private BigDecimal amountTax = null;
	private List<Ss2spProfitShareReportLineItem> lineItems=null;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setStatementCurrency(Currency statementCurrency) {
		this.statementCurrency = statementCurrency;
	}

	public void setItems(List<Ss2spProfitShareReportLineItem> items) {
		this.lineItems = items;
	}
	
	public void setAmountTax(BigDecimal amount){
		this.amountTax = amount;
	}

	@Override
	public int getVersionNumber() {
		return versionNumber;
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
	public String getAmountSubTotal() {
		if(lineItems==null) return "0.00";
		BigDecimal subTotal=BigDecimal.ZERO;
		for(Ss2spProfitShareReportLineItem item:this.lineItems){
			Ss2spProfitShareReportLineItemImpl origItem = (Ss2spProfitShareReportLineItemImpl)item;
			subTotal=subTotal.add(origItem.getNumericStatementAmount());
		}
		return subTotal.setScale(this.statementCurrency.getScale()).toString();
	}

	@Override
	public String getAmountTax() {
		return this.amountTax.setScale(this.statementCurrency.getScale(), RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getAmountTotal() {
		BigDecimal amountSubtotal = new BigDecimal(this.getAmountSubTotal());
		BigDecimal amountTax      = new BigDecimal(this.getAmountTax());
		return amountSubtotal.add(amountTax).setScale(this.statementCurrency.getScale(), RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getStatementCurrency() {
		return this.statementCurrency.name();
	}

	@Override
	public List<Ss2spProfitShareReportLineItem> getLineItems() {
		return this.lineItems;
	}

	@Override
	public String toString() {
		return "Ss2spProfitShareReportImplV3 [getVersionNumber()=" + getVersionNumber() + ", getDateStart()="
				+ getDateStart() + ", getDateEnd()=" + getDateEnd() + ", getIsurKcode()=" + getIsurKcode()
				+ ", getRcvrKcode()=" + getRcvrKcode() + ", getAmountSubTotal()=" + getAmountSubTotal()
				+ ", getAmountTax()=" + getAmountTax() + ", getAmountTotal()=" + getAmountTotal()
				+ ", getStatementCurrency()=" + getStatementCurrency() + ", getLineItems()=" + getLineItems() + "]";
	}
	
	

}
