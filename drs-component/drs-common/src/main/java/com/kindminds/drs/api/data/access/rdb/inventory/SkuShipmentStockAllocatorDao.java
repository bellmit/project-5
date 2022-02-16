package com.kindminds.drs.api.data.access.rdb.inventory;

import java.util.List;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;

public interface SkuShipmentStockAllocatorDao {

	List<Object []> queryShipmentStockAvailableInfo(Country unsDestinationCountry,String drsSku);
	Object [] queryUnsInfo(String name, String drsSku);
	Object [] queryIvsInfo(String name, String drsSku);
	List<Object[]> queryIvsStockQuantity(String unsName, String ivsName, String sku);
	void increaseStockQuantity(String unsName,String ivsName,String drsSku,int quantity, Integer lineSeq);
	Integer queryRefundedQuantity(String ivsName, String sku);

	List<Object []> queryShipmentInfo(Country unsDestinationCountry,String drsSku);
}
