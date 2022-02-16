package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.List;

public interface Ms2ssSettleableItemReport {

	public String getTitle();
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getCurrency();
	public String getTotal();
	public List<Ms2ssSettleableItemReportLineItem> getItemList();
	
	public interface Ms2ssSettleableItemReportLineItem {
		public String getTransactionTimeUtc();
		public String getSku();
		public String getSkuName();
		public String getName();
		public String getDescription();
		public String getSourceName();
		public String getCurrency();
		public String getAmount();
		public BigDecimal getNumericAmount();
	}
}
