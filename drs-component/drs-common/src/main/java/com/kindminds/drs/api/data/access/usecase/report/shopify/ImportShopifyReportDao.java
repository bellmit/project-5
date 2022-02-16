package com.kindminds.drs.api.data.access.usecase.report.shopify;

import java.util.List;

import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderReportRawLine;
import com.kindminds.drs.api.v1.model.shopify.ShopifyPaymentTransactionReportRawLine;
import com.kindminds.drs.api.v1.model.shopify.ShopifySalesReportRawLine;

public interface ImportShopifyReportDao {
	public int deleteOrderReportLineItems();
	public int insertOrderReportLineItems(List<ShopifyOrderReportRawLine> lineItems);
	public int deletePaymentTransactionReportLineItems();
	public int insertPaymentTransactionReportLineItems(List<ShopifyPaymentTransactionReportRawLine> lineItems);
	public int deleteSalesReportLineItems();
	public int insertSalesReportLineItems(List<ShopifySalesReportRawLine> lineItems);
}
