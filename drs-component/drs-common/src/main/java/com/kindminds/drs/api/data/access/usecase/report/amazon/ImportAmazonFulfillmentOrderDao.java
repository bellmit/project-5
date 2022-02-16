package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.Date;

import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;

public interface ImportAmazonFulfillmentOrderDao {
	Date queryInitialDate(String region);
	Date queryLastUpdateDate(String region);
	void updateLastUpdateDate(String region, Date lastUpdateDate);
	void setImportingStatus(String marketplace, String status);
	boolean isOrderExist(String sellerOrderId);
	boolean isSalesChannelNull(String amazonOrderId);
	void insert(AmazonOrder order);
	void update(AmazonOrder order);
	
	
}
