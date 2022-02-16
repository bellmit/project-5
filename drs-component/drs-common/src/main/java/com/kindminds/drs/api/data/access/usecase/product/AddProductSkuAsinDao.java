package com.kindminds.drs.api.data.access.usecase.product;

import java.util.List;

import com.kindminds.drs.Marketplace;

public interface AddProductSkuAsinDao {
	List<Object []> querySkuAsins(String splrKcode,int marketplaceId);
	List<Object []> queryMarketplaceSkuWithoutAsin();
	boolean queryMarketplaceSkuAsinExist(Marketplace marketplace, String drsSku, String asin);
	void insertSkuAsin(Marketplace marketplace, String drsSku, String asin);
	boolean queryMarketplaceSkuAsinFnskuExist(int marketplaceId, String marketplaceSku, String asin, String fnsku);
	int insertFbaRecord(int marketplaceId, String marketplaceSku, String asin, String fnsku, String afn, String mfn);
	int insertInventoryRecord(int marketplaceId, String marketplaceSku, String asin);
	boolean queryPmiIdExist(int marketplaceId, String marketplaceSku);
	int updateFbaRecord(int marketplaceId, String marketplaceSku, String asin, String fnsku, String afn, String mfn);
	void toggleStorageFeeFlag(int marketplaceId, String marketplaceSku);
	boolean fnskuIsUpdated(int marketplaceId, String marketplaceSku);
}
