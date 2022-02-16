package com.kindminds.drs.api.usecase.product;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;


import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;


import com.kindminds.drs.api.v1.model.product.BaseProductOnboardingWithSKU;

public interface MaintainProductOnboardingUco {
	/*
	//List base products onboarding
	DtoList<BaseProductOnboarding> retrieveBaseProductOnboardingList(String supplierKcode,int pageIndex);
	BaseProductOnboarding getDetailOfBaseProductOnboarding(String productBaseCode);
	BaseProductOnboardingDetail getProductInfoMarketSide(String supplierKcode,String productBaseCode,String country);
	BaseProductOnboardingDetail getProductMarketingMaterialSource(String supplierKcode,String productBaseCode);
	BaseProductOnboardingDetail getProductMarketingMaterialMarketSide(String supplierKcode,String productBaseCode,String country);
	//Create base product onboarding
	String saveDevelopingBaseProduct(String productInfoSource,String productInfoMarketSide,String productMarketingMaterialSource,String productMarketingMaterialMarketSide) throws IOException;
	//Edit base product onboarding
	//ProductInfoSource
	String saveDraftForProductInfoSource(String productInfoSource,String productInfoMarketSide,String productMarketingMaterialSource,String productMarketingMaterialMarketSide) throws IOException;
	String submitProductInfoSource(String productInfoSource,String productInfoMarketSide,String productMarketingMaterialSource,String productMarketingMaterialMarketSide) throws IOException;
	String approveProductInfoSource(String productInfoSource,String productInfoMarketSide,String productMarketingMaterialSource,String productMarketingMaterialMarketSide) throws IOException;
	String returnProductInfoSource(String productInfoSource,String productInfoMarketSide,String productMarketingMaterialSource,String productMarketingMaterialMarketSide) throws IOException;
	String updateProductInfoSource(String productInfoSource,String productInfoMarketSide,String productMarketingMaterialSource,String productMarketingMaterialMarketSide) throws IOException;
	//ProductInfoMarketSide
	String saveDraftForProductInfoMarketSide(String supplierKcode,String productBaseCode,String country,String jsonData);
	String submitProductInfoMarketSide(String supplierKcode,String productBaseCode,String country,String jsonData);
	String approveProductInfoMarketSide(String supplierKcode,String productBaseCode,String country,String jsonData);
	String returnProductInfoMarketSide(String supplierKcode,String productBaseCode,String country,String jsonData);
	String updateProductInfoMarketSide(String supplierKcode,String productBaseCode,String country,String jsonData);
	//ProductMarketingMaterialSource
	String saveDraftForProductMarketingMaterialSource(String supplierKcode,String productBaseCode,String jsonData);
	String submitProductMarketingMaterialSource(String supplierKcode,String productBaseCode,String jsonData);
	String approveProductMarketingMaterialSource(String supplierKcode,String productBaseCode,String jsonData);
	String returnProductMarketingMaterialSource(String supplierKcode,String productBaseCode,String jsonData);
	String updateProductMarketingMaterialSource(String supplierKcode,String productBaseCode,String jsonData);
	//ProductMarketingMaterialMarketSide
	String updateProductMarketingMaterialMarketSide(String supplierKcode,String productBaseCode,String country,String jsonData);
	*/

	//SKU
	BaseProductOnboardingWithSKU getBaseProductOnboardingWithSKU(String productBaseCode)throws JsonParseException, JsonMappingException, IOException;
	String updateBaseProductOnboardingWithSKU(BaseProductOnboardingWithSKU baseProduct)throws JsonParseException, JsonMappingException, IOException;
	String updateDangerousGoodsCode(String productInfoSource) throws JsonParseException, JsonMappingException, IOException;

	//Other

	/*
	String uploadReferenceFile(String fileName, byte[] bytes);
	void downloadReferenceFile(String fileName, HttpServletResponse response) throws IOException;
	void removeReferenceFile(String fileName);
	String uploadBatteryFile(String fileName, byte[] bytes);
	void downloadBatteryFile(String fileName, HttpServletResponse response) throws IOException;
	void removeBatteryFile(String fileName);
	String uploadMainImageFile(String fileName, byte[] bytes,String region);
	void downloadMainImageFile(String fileName,String region,HttpServletResponse response) throws IOException;
	void removeMainImageFile(String fileName,String region);
	String uploadVariationImageFile(String fileName, byte[] bytes,String region);
	void downloadVariationImageFile(String fileName,String region,HttpServletResponse response) throws IOException;
	void removeVariationImageFile(String fileName,String region);
	String uploadOtherImageFile(String fileName, byte[] bytes,String region);
	void downloadOtherImageFile(String fileName,String region,HttpServletResponse response) throws IOException;
	void removeOtherImageFile(String fileName,String region);
	*/



	Map<String,String> getSupplierKcodeToShortEnUsNameMap();
	boolean isDrsUser();
	String getUserCompanyKcode();
	boolean isBaseProductCodeExist(String supplierKcode, String productBaseCode);

	boolean isExecutableForUpdatingSKUs(ProductEditingStatusType status);

	String getPriceWithTax(String countryCode,BigDecimal price);



	//Map<String,String> getSupplierKcodeToNameMap();
	//boolean isExecutable(OnboardingApplicationLineitemStatusType status);
}