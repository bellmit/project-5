package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;

public interface Ss2spPaymentAndRefundReportItemToExport {
	
	public String getStatementName();
	public String getDateStart();
	public String getDateEnd();
	public String getShipmentName();
	public String getTransactionTimeUtc();		
	public String getSku();
	public String getSkuName();
	public String getItemType();
	public String getSourceName();
	public String getCurrency();
	public BigDecimal getUnitAmount();
	public Integer getQuantity();
	public BigDecimal getTotalAmount();
		
}