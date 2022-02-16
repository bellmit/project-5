package com.kindminds.drs.api.data.access.usecase.report.shopify;

import java.util.Date;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrder;

public interface ImportShopifyOrderDao {
	boolean isExist(String orderId);
	void setImportingStatus(Marketplace marketplace,String serviceName,String status);
	void insert(ShopifyOrder order);
	void update(ShopifyOrder order);
	void updateLastUpdateDate(Marketplace marketplace,String serviceName,Date date);
	Date queryInitialDate(Marketplace marketplace,String serviceName);
	Date queryLastUpdateDate(Marketplace marketplace,String serviceName);
}
