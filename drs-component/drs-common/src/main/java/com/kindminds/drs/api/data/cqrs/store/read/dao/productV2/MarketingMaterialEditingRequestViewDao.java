package com.kindminds.drs.api.data.cqrs.store.read.dao.productV2;

import com.kindminds.drs.api.v2.biz.domain.model.product.MarketingMaterialEditingRequest;

import java.util.List;

public interface MarketingMaterialEditingRequestViewDao {

    void insertView(String supplierKcode , MarketingMaterialEditingRequest marketingMaterialEditingRequest , Object[] productVariationView);

    void updateView(MarketingMaterialEditingRequest marketingMaterialEditingRequest);

    void deleteVive(String id);

    List<Object []> findView(String id);

}
