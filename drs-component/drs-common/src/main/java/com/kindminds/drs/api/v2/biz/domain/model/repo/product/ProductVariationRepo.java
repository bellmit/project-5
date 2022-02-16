package com.kindminds.drs.api.v2.biz.domain.model.repo.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;

import java.time.OffsetDateTime;
import java.util.List;

public interface ProductVariationRepo {

    List<ProductVariation> findByProductId(String productId, OffsetDateTime productCreateTime,
                                           Country marketSide);

    ProductVariation find(String productVariationId, OffsetDateTime createTime,
                          Country marketSide);
}