package com.kindminds.drs.api.data.cqrs.store.event.dao.productV2;

import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;

import java.util.List;
import java.util.Optional;

public interface ProductMarketingMaterialDao {


    Optional<List<String>> getIds(String productVariationId);

    List<Object[]> queryById(String id);

    void insert(ProductMarketingMaterial productMarketingMaterial);



}
