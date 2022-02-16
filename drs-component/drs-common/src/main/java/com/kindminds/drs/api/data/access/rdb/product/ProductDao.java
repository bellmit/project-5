package com.kindminds.drs.api.data.access.rdb.product;

import com.kindminds.drs.api.data.transfer.productV2.ProductPackageDimWeightDto;

import java.util.List;
import java.util.Map;


public  interface ProductDao {

     Map queryActiveSkuCodeToDrsNameMap(int companyId);


     List queryProductBaseCode( String supplierKcode);


     List queryProductSkuCode( String productBaseCode);


     String queryBaseProductBySku( String sku);


     Map queryAllSkuToBaseProductMap( String companyCode);


     List<ProductPackageDimWeightDto> queryPackageDimWeight(String baseProduct);


}