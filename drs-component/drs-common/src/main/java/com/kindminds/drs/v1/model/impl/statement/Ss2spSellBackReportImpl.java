package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;

import com.kindminds.drs.api.v1.model.report.Ss2spSellBackReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.DateHelper;


import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Ss2spSellBackReportImpl implements Ss2spSellBackReport {

	private String title;
	private StatementInfo info;
	private Currency currency;
	private List<Ss2spSellBackReport.Ss2spSellBackReportLineItem> lineItems=null;
	
	public void setTitle(String value){ this.title=value; }
	public void setInfo(StatementInfo info) {
		this.info = info;
	}

	public void setCurrency(Currency c){
		this.currency = c;
		this.calcTotal();
	}


	public void setLineItems(List<Ss2spSellBackReportLineItem> lineItems){
		this.lineItems=lineItems;
	}

	public void setIncludedTax(Date periodEnd){
		Date baseDate = DateHelper.toDate("2021/03/01","yyyy/MM/dd");
		if(periodEnd !=null){
			if(periodEnd.after(baseDate))includedTax= true;
		}
	}


	
	@Override
	public String toString() {
		return "Ss2spSellBackReportImpl [getTitle()=" + getTitle() + ", getDateStart()=" + getDateStart()
				+ ", getDateEnd()=" + getDateEnd() + ", getIsurKcode()=" + getIsurKcode() + ", getRcvrKcode()="
				+ getRcvrKcode() + ", getCurrency()=" + getCurrency() + ", getLineItems()=" + getLineItems()
				+ ", getTotal()=" + getTotal() + "]";
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
	public List<Ss2spSellBackReportLineItem> getLineItems() {
		return this.lineItems;
	}
	

	private void calcTotal() {

		total = BigDecimal.ZERO;
		this.totalTaxAmout = BigDecimal.ZERO;
		this.totalWithTax = BigDecimal.ZERO;
		if(this.lineItems!=null) {
			for (Ss2spSellBackReportLineItem item : lineItems) {

				Ss2spSellBackReportLineItemImpl origItem = (Ss2spSellBackReportLineItemImpl) item;
				if(this.includedTax){
					this.totalTaxAmout = this.totalTaxAmout.add(origItem.getTaxAmount());
					origItem.doExclTaxUnitAMount();
				}

				total = total.add(origItem.getNumericTotalAmount());

			}
		}

		total =  total.setScale(this.currency.getScale(), BigDecimal.ROUND_HALF_UP);
		if(this.includedTax){
			this.totalTaxAmout = this.totalTaxAmout.setScale(this.currency.getScale(), BigDecimal.ROUND_HALF_UP);
			this.totalWithTax = total.add(this.totalTaxAmout);
		}

	}

	private BigDecimal total=BigDecimal.ZERO;
	@Override
	public String getTotal() {
		return total.toString();
	}

	private BigDecimal totalTaxAmout = BigDecimal.ZERO;
	@Override
	public String getTotalTaxAmount() {
		return this.totalTaxAmout.toString();
	}

	private BigDecimal totalWithTax = BigDecimal.ZERO;
	@Override
	public String getTotalWithTax() {
		if(this.includedTax & this.totalWithTax == BigDecimal.ZERO) this.getTotal();
		return this.totalWithTax.setScale(this.currency.getScale(), BigDecimal.ROUND_HALF_UP).toString();
	}

	private boolean includedTax = false;
	@Override
	public boolean getIncludedTax() {
		return this.includedTax;
	}


}
