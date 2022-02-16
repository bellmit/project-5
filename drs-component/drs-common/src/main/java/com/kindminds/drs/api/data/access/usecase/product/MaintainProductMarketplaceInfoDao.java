package com.kindminds.drs.api.data.access.usecase.product;

import java.util.List;

import com.kindminds.drs.api.v1.model.product.AmazonAsin;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;

public interface MaintainProductMarketplaceInfoDao {
	public ProductMarketplaceInfo query(String skuCodeByDrs,Integer marketplaceId);
	public ProductMarketplaceInfo insert(ProductMarketplaceInfo info);
	public ProductMarketplaceInfo update(ProductMarketplaceInfo info);
	public boolean delete(String skuCodeByDrs,Integer marketplaceId);
	public String querySupplierKcodeOfProductSku(String skuCodeByDrs);
	public List<Integer> queryMarketplaceIdListOfExistingMarketplaceInfo(String skuCodeByDrs);
	
	public ProductMarketplaceInfo queryForK101(String skuCodeByDrs,Integer marketplaceId);
	public ProductMarketplaceInfo insertForK101(ProductMarketplaceInfo info);
	public ProductMarketplaceInfo updateForK101(ProductMarketplaceInfo info);
	public boolean deleteForK101(String skuCodeByDrs,Integer marketplaceId);	
	public List<Integer> queryMarketplaceIdListOfExistingMarketplaceInfoForK101(String skuCodeByDrs);
	public List<AmazonAsin> queryLiveAmazonAsins();
    public List<AmazonAsin> queryLiveK101AmazonAsins();
}
