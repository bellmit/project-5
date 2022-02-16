package com.kindminds.drs.api.v1.model.product;

import java.util.List;

public interface SKU extends Product {
	public enum EAN_PROVIDER{ DRS,SUPPLIER }
	public enum Status{
		SKU_DRAFT,
		SKU_PENDING_INITIAL_APPROVAL,
		SKU_AWAITING_SAMPLE,
		SKU_PENDING_FINAL_APPROVAL,
		SKU_PREPARING_LAUNCH,
		SKU_ACTIVE,
		SKU_DEACTIVATED,
		SKU_ABORTED;
	}
	public String getSupplierKcode();
	public String getBaseProductCode();
	public String getCodeByDrs();
	public String getProductEAN();
	public String getEanProvider();
	public String getStatus();
	public String getManufacturingLeadTimeDays();
	public boolean getContainLithiumIonBattery();
	public List<ProductMarketplaceInfo> getRegionInfoList();
}
