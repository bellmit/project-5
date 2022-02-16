package com.kindminds.drs.api.usecase.logistics;

import java.math.BigDecimal;
import java.util.List;

public interface ViewUnsRecognizeRevenueReportUco {
	
	String getTsvReport(String ms2ssStatementName);
	
	public interface UnsGroup{
		void setInvoiceNumber(String invoiceNumber);
		String getUnsName();
		String getInvoiceNumber();
		String getTotal();
		List<UnsGroupDetail> getUnsGroupDetails();
	}
	
	public interface UnsGroupDetail{
		String getSku();
		String getDdpPrice();
		String getCifPrice();
		String getQuantityPayment();
		String getQuantityRefund();
		String getQuantityEffective();
		String getSubtotalText();
		BigDecimal getSubtotal();
	}
	
}
