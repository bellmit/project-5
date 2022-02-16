package com.kindminds.drs.api.usecase.product;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface MaintainProductBaseUco {
	DtoList<BaseProduct> getList(int pageIndex);
	String insert(BaseProduct base);
	String insert(BaseProduct base, String drsBaseCode);
	BaseProduct get(String baseCodeByDrs);
	BaseProduct getBaseProduct(String baseCodeByDrs);
	String update(BaseProduct base);
	String updateBaseProduct(BaseProduct base);
	String delete(String baseCodeByDrs);
	List<String> getCategoryList();
	Map<String,String> getSupplierKcodeToNameMap();
	boolean isBaseExist(String supplierKcode, String baseCodeBySupplier);
}
