package com.kindminds.drs.api.data.access.usecase.inventory;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem;
import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport.InventoryPaymentReportQuantitySummaryLineItem;

public interface ViewInventoryPaymentReportDao {
	public Map<String,Integer> queryShipmentSkuToOriginalInvoiceQuantityMap(String shipmentName);

    @SuppressWarnings("unchecked")
    Map<String,Integer> queryShipmentSkuToOriginalInvoiceAmountMap(String shipmentName);

    public List<InventoryPaymentReportQuantitySummaryLineItem> queryInventoryPaymentReportQuantitySummaryLineItems(String shipmentName);

    @SuppressWarnings("unchecked")
    List<InventoryPaymentReportQuantitySummaryLineItem> queryInventoryPaymentReportAmountSummaryLineItems(String shipmentName);

    public List<InventoryPaymentReportAmountDetailLineItem> queryInventoryPaymentReportAmountDetailLineItems(String shipmentName);
}
