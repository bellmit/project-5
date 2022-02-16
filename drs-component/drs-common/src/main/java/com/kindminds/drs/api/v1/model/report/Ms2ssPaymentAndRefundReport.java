package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface Ms2ssPaymentAndRefundReport {
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getTotal();
	public Currency getCurrency();
	public List<Ms2ssPaymentAndRefundReportItem> getLineItems();
	public interface Ms2ssPaymentAndRefundReportItem{
		public String getSku();
		public String getSkuName();
		public String getItemType();
		public BigDecimal getUnitAmount();
		public Integer getQuantity();
		public String getTotalAmount();
		public BigDecimal getNumericTotalAmount();
		public Currency getCurrency();
		public Integer getSettleableItemId();
	}
}
