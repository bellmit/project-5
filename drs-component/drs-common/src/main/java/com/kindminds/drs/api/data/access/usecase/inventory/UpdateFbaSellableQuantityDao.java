package com.kindminds.drs.api.data.access.usecase.inventory;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.kindminds.drs.api.v1.model.amazon.AmazonSkuInventorySupply;

public interface UpdateFbaSellableQuantityDao {
	void setImportingStatus(String serviceName,String marketplace,String status);
	Date queryInitialDate(String serviceName,String marketplace);
	Date queryLastUpdateDate(String serviceName,String marketplace);
	void updateLastUpdateDate(String serviceName,String marketplace,XMLGregorianCalendar date);
	boolean selectExist(int marketplaceKey,String marketplaceSku);
	void insertAmazonInventorySupply(int marketplaceId,AmazonSkuInventorySupply amazonSkuInventorySupply,XMLGregorianCalendar date);
	void updateAmazonInventorySupply(int marketplaceId,AmazonSkuInventorySupply amazonSkuInventorySupply,XMLGregorianCalendar date);
}
