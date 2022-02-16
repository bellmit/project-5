package com.kindminds.drs.api.v2.biz.domain.model.product;

import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;

import java.time.OffsetDateTime;

public interface MarketingMaterialEditingRequest {


    String getId();

    OffsetDateTime getCreateTime();

    ProductEditingStatusType getStatus();

    ProductMarketingMaterial getProductMarketingMaterial();

    void submit();

    void approve();

    void reject();

    void update(ProductMarketingMaterial productMarketingMaterial);

}
