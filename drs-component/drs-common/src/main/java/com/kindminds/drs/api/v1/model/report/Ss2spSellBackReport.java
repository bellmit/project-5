package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface Ss2spSellBackReport {
	
	public String getTitle();
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getTotal();
	public String getTotalTaxAmount();
	public String getTotalWithTax();
	public boolean getIncludedTax();
	public Currency getCurrency();
	public List<Ss2spSellBackReportLineItem> getLineItems();
	
	public interface Ss2spSellBackReportLineItem{
		public String getIvsName();
		public String getSku();
		public String getSkuName();
		public String getItemType();
		public String getUnitAmount();
		public Integer getQuantity();
		public String getSubtotal();
		public String getCurrency();
		public BigDecimal getTaxAmount();


	}
}
