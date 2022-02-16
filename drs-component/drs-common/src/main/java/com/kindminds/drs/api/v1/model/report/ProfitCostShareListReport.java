package com.kindminds.drs.api.v1.model.report;

import java.util.List;

public interface ProfitCostShareListReport {
	
	public List<ProfitCostShareListReportItem> getPcsStatements();
	
	public interface ProfitCostShareListReportItem {
		public String getStatementId();
		public String getPeriodStartUtc();
		public String getPeriodEndUtc();
		public String getCurrency();
		public String getTotal();
		public String getBalance();
		public String getInvoiceFromSsdc();
		public String getInvoiceFromSupplier();
		public String getInvoiceNotes();
		public String getAmountTotal();
		public void setAmountTotal(String amountTotal);
		public String getAmountStr();
	}
	
}
