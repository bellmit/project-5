package com.kindminds.drs.api.data.access.usecase.product;

import java.util.List;

import com.kindminds.drs.api.v1.model.product.BaseProduct;

public interface MaintainProductBaseDao {
	int queryCount(String supplierCompanyKcode);
	List<BaseProduct> queryListWithSku(int startIndex,int size,String supplierCompanyKcode);
	int queryProductSkuCountInBase(String baseCodeByDrs);
	BaseProduct query(String baseCodeByDrs);
	String insert(BaseProduct base,String drsBaseCode);
	String update(String origDrsCode,BaseProduct base,String newDrsCode);
	String updateBaseProduct(String drsBaseCode,BaseProduct base);
	String delete(String baseCodeByDrs);
	String querySupplierKcodeOfProductBase(String baseCodeByDrs);
	boolean isProductBaseSupplierCodeExist(String supplierKcode,String baseCodeBySupplier);
	List<String> queryCategoryList();
}
