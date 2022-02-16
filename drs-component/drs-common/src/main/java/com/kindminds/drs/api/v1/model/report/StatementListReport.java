package com.kindminds.drs.api.v1.model.report;

import java.util.List;

public interface StatementListReport {
	
	public String getCurrency();
	public List<StatementListReportItem> getItems();
	
	public interface StatementListReportItem {
		public String getStatementId();
		public String getPeriodStartUtc();
		public String getPeriodEndUtc();
		public String getCurrency();
		public String getTotal();
		public String getBalance();
		public String getInvoiceFromSsdc();
		public String getInvoiceFromSupplier();
	}
	
}
