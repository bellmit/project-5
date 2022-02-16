package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.List;

public interface Ss2spCustomerCareCostReport {
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getAmountTotal();
	public List<Ss2spCustomerCareCostReportItem> getItems();
	public interface Ss2spCustomerCareCostReportItem{
		public String getDateTimeUtc();
		public String getProductSku();
		public String getCaseId();
		public String getMessageLineSeq();
		public String getCurrency();
		public String getAmountStr();
		public BigDecimal getAmount();
	}
}
