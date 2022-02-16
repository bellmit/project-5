package com.kindminds.drs.api.usecase.product;


import com.kindminds.drs.api.v2.biz.domain.model.product.Product;

public interface LegacyProductUco {
    void createProductRef(Product mpProduct, String productBaseCode);
    void createDraftProductRef(String mpProductId, int mpMarketSide, String productBaseCode);
    void createProductRef(String mpProductId ,  int marketSideId, int productId) ;
}
