package com.kindminds.drs.api.v2.biz.domain.model.product;

import java.time.OffsetDateTime;

public interface ProductMarketingMaterial {

    String getId();

    OffsetDateTime getCreateTime();

    String getData();

    String getProductVariationId();

    OffsetDateTime getProductVariationCreateTime();

    void update(String data);

    void edit();



}
