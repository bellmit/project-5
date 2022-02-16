package com.kindminds.drs.api.data.access.usecase.product;

import java.util.List;

import com.kindminds.drs.api.v1.model.product.SKU;

public interface MaintainProductSkuDao {
	public SKU query(String skuCodeByDrs);
	public String insert(SKU sku,String drsSkuCode);
	public String update(String origDrsCode,SKU sku, String newDrsCode);
	public String updateSkuMltAndContainLithium(SKU sku);
	public String updateSku(String drsSkuCode,SKU sku);
	public String delete(String skuCodeByDrs);
	public List<String> queryBaseCodeList(String supplierKcode);
	public String querySupplierKcode(String skuCodeByDrs);
	public boolean isProductSkuSupplierCodeExist(String supplierKcode, String skuCodeBySupplier);
}
