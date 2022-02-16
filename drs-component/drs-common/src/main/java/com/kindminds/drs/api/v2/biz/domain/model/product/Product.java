package com.kindminds.drs.api.v2.biz.domain.model.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;

import java.time.OffsetDateTime;
import java.util.List;

public interface Product {

    String getId();

    OffsetDateTime getCreateTime();

    Country getMarketside();

    String getSupplierKcode();

    String getProductBaseCode();

    String getData();

    List<ProductVariation> getProductVariations();

    String getEditingStatusText();

    ProductEditingStatusType getEditingStatus();

    Boolean isActivated();

    //todo need refactor
    void edit(ProductEditingStatusType editingStatus);

    void clone(Product product);


}
