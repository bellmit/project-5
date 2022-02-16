package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface Ss2spServiceExpenseReport {
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public Currency getCurrency();
	public String getSubtotal();
	public String getTax();
	public String getTotal();
	public List<Ss2spServiceExpenseReportItem> getItems();
	public interface Ss2spServiceExpenseReportItem{
		public String getSku();
		public String getSkuName();
		public String getItemName();
		public String getNote();
		public Integer getQuantity();
		public String getAmountPerUnit();
		public String getAmountTotalStr();
		public BigDecimal getAmountTotal();
	}
}
