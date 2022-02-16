package com.kindminds.drs.api.data.access.usecase.logistics;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ViewUnsRecognizeRevenueReportDao {

	List<Object[]> queryUnsSkuPaymentDetails(String statementName, String incoterm);
	List<Object[]> queryUnsSkuPriceInfo(String statementName,String incoterm);
	Map<String,String> queryUnsInvoiceNumber(Set<String> keySet);
	
	public interface PaymentDetail {
		Integer getQuantityPayment();
		Integer getQuantityRefund();
		BigDecimal getDdpSubtotal();
	}
	
	public interface PriceInfo {
		BigDecimal getDdpPrice();
		BigDecimal getCifPrice();
	}
	
}
