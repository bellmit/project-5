package com.kindminds.drs.api.v2.biz.domain.model.inventory;

import java.util.List;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;

//todo arthur remove it
public interface SkuShipmentStockAllocator {
	List<SkuShipmentAllocationInfo> requestAllocations(Country unsDestinationCountry, MarketSideTransaction transaction, Integer quantity);
	List<SkuShipmentAllocationInfo> requestAllocations(Country unsDestinationCountry, String drsSku, Integer quantity);
	void increaseStockQuantity(String unsName,String ivsName,String drsSku,int quantity);
	Integer updateQuantityReturnToSupplier(String ivsName, String unsName, String drsSku, int quantity);

	void updateQuantityReturnToSupplierAllowOverflow(String ivsName, String unsName,
													 String drsSku, int quantity);
}
