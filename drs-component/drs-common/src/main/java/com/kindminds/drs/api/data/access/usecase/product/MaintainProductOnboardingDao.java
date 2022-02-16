package com.kindminds.drs.api.data.access.usecase.product;

import java.util.List;


import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;



public interface MaintainProductOnboardingDao {
	//list
	public List<ProductDto> queryBaseProductOnboardingList(int startIndex, int size);
	public List<ProductDto> queryBaseProductOnboardingList(int startIndex, int size, String companyKcode);
	public int queryBaseProductOnboardingCount();
	public int queryBaseProductOnboardingCount(String companyKcode);
	//query
	public ProductDto queryBaseProductOnboarding(String productBaseCode);

	public String querySupplierKcodeOfBaseProductOnboarding(String productBaseCode);
	public ProductDetail queryProductInfoMarketSide(String supplierKcode, String productBaseCode, String country);
	public ProductDetail queryProductMarketingMaterialSource(String supplierKcode,String productBaseCode);
	public ProductDetail queryProductMarketingMaterialMarketSide(String supplierKcode,String productBaseCode,String country);


	//insert
	public String insertProductInfoSource(String companyKcode,String productBaseCode,String jsonData);
	public String insertProductInfoMarketSide(String companyKcode,String productBaseCode,String country,String jsonData);
	public String insertProductMarketingMaterialSource(String companyKcode,String productBaseCode,String jsonData);
	public String insertProductMarketingMaterialMarketSide(String companyKcode,String productBaseCode,String country,String jsonData);
	//update
	public String updateProductInfoMarketSide(String companyKcode,String productBaseCode,String country,String jsonData);
	public String updateProductMarketingMaterialSource(String companyKcode,String productBaseCode,String jsonData);
	public String updateProductMarketingMaterialMarketSide(String companyKcode,String productBaseCode,String country,String jsonData);
	public void updateStatusForProductInfoSource(String companyKcode,String productBaseCode,
												 ProductEditingStatusType status);
	public void updateStatusForProductInfoMarketSideByRegion(String companyKcode,String productBaseCode,
															 String country,ProductEditingStatusType status);
	public void updateStatusForProductMarketingMaterialSource(String companyKcode,String productBaseCode,
															  ProductEditingStatusType status);
	public void updateJsonDataForProductInfoSource(String companyKcode,String productBaseCode,String jsonData);
	//delete
	public void deleteProductInfoSource(String companyKcode,String productBaseCode);
	public void deleteProductInfoMarketSide(String companyKcode,String productBaseCode);
	public void deleteProductMarketingMaterialSource(String companyKcode,String productBaseCode);
	public void deleteProductMarketingMaterialMarketSide(String companyKcode,String productBaseCode);

	//other
	public boolean isProductBaseCodeExist(String supplierKcode, String productBaseCode);
}