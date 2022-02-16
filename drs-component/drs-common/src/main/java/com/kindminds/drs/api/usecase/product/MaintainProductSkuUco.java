package com.kindminds.drs.api.usecase.product;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.product.SKU;

public interface MaintainProductSkuUco { 
	public String insert(SKU sku);
	public String insert(SKU sku,String drsSkuCode);
	public SKU get(String skuCodeByDrs);
	public SKU getProductSku(String skuCodeByDrs);
	public String update(SKU sku);
	public String updateSku(SKU sku);
	public String updateSkuMltAndContainLithium(SKU sku);
	public String delete(String skuCodeByDrs);
	// OPTION SOURCES
	public List<String> getBaseCodeList(String supplierKcode);
	public List<String> getSkuStatusList();
	public Map<String,String> getSupplierKcodeToNameMap();
	public boolean isSkuExist(String supplierKcode, String skuCodeBySupplier);
}
