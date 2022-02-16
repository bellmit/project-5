package com.kindminds.drs.api.v2.biz.domain.model.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;

import java.time.OffsetDateTime;
import java.util.List;

public interface ProductVariation {

    String getId();

    OffsetDateTime getCreateTime();

    String getVariationCode();

    String getData();

    List<ProductMarketingMaterial> getProductMarketingMaterials();

    String getProductId();

    OffsetDateTime getProductCreatetime();

    Country getMarketside();

    void edit();



}
