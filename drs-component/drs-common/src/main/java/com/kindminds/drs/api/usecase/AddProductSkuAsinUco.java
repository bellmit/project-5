package com.kindminds.drs.api.usecase;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.product.SkuFnskuAsin;
import com.kindminds.drs.api.v1.model.product.SkuWithoutAsin;

public interface AddProductSkuAsinUco {
	List<Marketplace> getMarketplaces();
	Map<String,String> getKcodeToSupplierName();	
	List<SkuFnskuAsin> getSkuToAsin(String marketplaceIdText, String splrKcode);
	List<SkuWithoutAsin> getMarketplaceToSku();
	String addFbaData(byte[] fileData, int marketplaceId);
	String addInventoryData(byte[] fileData, int marketplaceId);
	void toggleStorageFeeFlag(int marketplaceId, String marketplaceSku);
}
