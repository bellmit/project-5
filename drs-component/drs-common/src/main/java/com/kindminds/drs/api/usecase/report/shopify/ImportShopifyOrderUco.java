package com.kindminds.drs.api.usecase.report.shopify;

public interface ImportShopifyOrderUco {
	void importOrders();
	// For Test
	void saveTestOrder();
	void updateTestOrder();
}
