package com.kindminds.drs.api.usecase.product;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.product.AmazonAsin;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;

public interface MaintainProductMarketplaceInfoUco { 
	public ProductMarketplaceInfo insert(ProductMarketplaceInfo info);
	public ProductMarketplaceInfo get(String skuCodeByDrs,Integer marketplaceId);
	public ProductMarketplaceInfo getProductMarketplace(String skuCodeByDrs,Integer marketplaceId);
	public ProductMarketplaceInfo update(ProductMarketplaceInfo info);
	public boolean delete(String skuCodeByDrs,Integer marketplaceId);
	// OPTION SOURCES
	public String getMarketplaceCurrency(String marketplaceName);
	public List<String> getProductSkuMarketplaceStatusList();
	public Map<String,String> getSupplierKcodeToNameMap();
	public List<Marketplace> getMarketplaceList();

	public List<AmazonAsin> getLiveAmazonAsins();
	public List<AmazonAsin> getLiveK101AmazonAsins();
}
