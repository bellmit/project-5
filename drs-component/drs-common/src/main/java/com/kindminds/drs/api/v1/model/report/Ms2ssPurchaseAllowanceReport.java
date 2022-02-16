package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface Ms2ssPurchaseAllowanceReport {
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getSubTotal();
	public Currency getCurrency();
	public List<Ms2ssPurchaseAllowanceReportItemGroupBySku> getItemsGroupBySku();
	public Map<String,String> getOtherItemAmounts();
	public interface Ms2ssPurchaseAllowanceReportItemGroupBySku{
		public String getSku();
		public String getSubtotalStr();
		public BigDecimal getSubtotal();
		public List<Ms2ssPurchaseAllowanceReportRawItem> getRawItems();
	}
	public interface Ms2ssPurchaseAllowanceReportRawItem{
		public String getSku();
		public String getItemName();
		public Currency getStatementCurrency();
		public String getStatementAmountStr();
		public Integer getSettleableItemId();
		public BigDecimal getStatementAmount();
	}
}
