package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface RemittanceReport {
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getCurrency();
	public String getTotal();
	public List<RemittanceReportItem> getItems();
	public interface RemittanceReportItem{
		public String getDateSent();
		public String getDateRcvd();
		public String getAmountStr();
		public Currency getCurrency();
		public BigDecimal getAmount();
		public String getReference();
	}
}
