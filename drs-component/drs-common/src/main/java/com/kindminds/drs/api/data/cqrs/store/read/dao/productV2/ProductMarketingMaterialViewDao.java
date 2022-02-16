package com.kindminds.drs.api.data.cqrs.store.read.dao.productV2;

import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;

import java.util.List;

public interface ProductMarketingMaterialViewDao {

    List<Object[]> findView(String id);

    void insertView(String supplierKCode, String productBaseCode,
                    ProductMarketingMaterial productMarketingMaterial , Object[] productVariationView ,
                    ProductEditingStatusType status);

    void updateView(ProductMarketingMaterial productMarketingMaterial , ProductEditingStatusType status);

    void deleteView(String id);
}
