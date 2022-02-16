package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.List;

public interface Ss2spSettleableItemReport {
	public String getSettleableItemName();
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getCurrency();
	public String getTotal();
	public List<Ss2spSettleableItemReportLineItem> getItemList();
	
	public interface Ss2spSettleableItemReportLineItem {
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