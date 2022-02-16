package com.kindminds.drs.api.v1.model.report;

import java.util.List;

public interface InventoryPaymentReport {
	
	String getShipmentName();
	String getCurrency();
	String getAmountSubtotal();
	List<InventoryPaymentReportQuantitySummaryLineItem> getQuantitySummaryLineItems();
	List<InventoryPaymentReportAmountDetailLineItem> getAmountDetailLineItems();
	
	public interface InventoryPaymentReportQuantitySummaryLineItem{
		String getSku();
		Integer getQuantityOriginalInvoice();

		Integer getAmountOriginalInvoice();

		Integer getQuantityReturned();

		Integer getAmountReturned();

		Integer getQuantityActualPurchase();

		Integer getAmountActualPurchase();

		Integer getQuantityMarketSideFulfilled();

		Integer getAmountMarketSideFulfilled();

		Integer getQuantitySoldBack();

		Integer getAmountSoldBack();

		Integer getQuantitySoldBackRecovery();

		Integer getAmountSoldBackRecovery();

		Integer getQuantityOther();

		Integer getAmountOther();

		Integer getQuantityPaid();

		Integer getAmountPaid();

		Integer getQuantityRefunded();

		Integer getAmountRefunded();

		Integer getQuantityNetPaid();

		Integer getAmountNetPaid();

		Integer getQuantityOutstanding();

		Integer getAmountOutstanding();

		void setQuantityPaid(Integer quantityPaid);
		void setQuantityRefund(Integer quantityRefunded);

		void setAmountRefund(Integer amountRefund);

		void setAmountPaid(Integer amountPaid);
	}
	
	public interface InventoryPaymentReportAmountDetailLineItem{
		String getSku();
		String getStatementName();
		String getItemName();
		String getQuantity();
		String getAmount();
	}
}
