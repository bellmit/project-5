package com.kindminds.drs.api.v1.model.product;

import com.kindminds.drs.Marketplace;

public interface ProductMarketplaceInfo {
	public enum ProductMarketStatus{
		REGION_ABSENT("Absent"), REGION_ONBOARDING("Onboarding"), REGION_LIVE("Live"), REGION_DEACTIVATED("Deactivated"), REGION_ABORTED("Aborted");
		private String dbValue;
		ProductMarketStatus(String dbValue){this.dbValue = dbValue;}
		public String getDbValue(){return this.dbValue;}
		public static ProductMarketplaceInfo.ProductMarketStatus fromDbValue(String value){
			for(ProductMarketplaceInfo.ProductMarketStatus rs: ProductMarketStatus.values()){
				if(rs.dbValue.equals(value)){
					return rs;
				}
			}
			return null;
		}
	}
	public String getProductCodeByDrs();
	public String getProductNameBySupplier();
	public Marketplace getMarketplace();
	public String getMarketplaceSku();
	public String getCurrency();
	public String getStatus();
	public String getMSRP();
	public String getSupplierSuggestedBaseRetailPrice();
	public String getCurrentBaseRetailPrice();
	public String getEstimatedDrsRetainment();
	public String getReferralRate();
	public String getEstimatedMarketplaceFees();
	public String getEstimatedFulfillmentFees();
	public String getEstimatedImportDuty();
	public String getEstimatedFreightCharge();
	public String getApproxSupplierNetRevenue();
}