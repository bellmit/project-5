package com.kindminds.drs.service.usecase.product;


import com.kindminds.drs.api.usecase.product.LegacyProductUco;
import com.kindminds.drs.api.data.access.rdb.product.LegacyProductDao;

import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("legacyProductUco")
public class LegacyProductUcoImpl implements LegacyProductUco {
    @Autowired
    LegacyProductDao dao;

    @Override
    public void createProductRef(Product mpProduct, String productBaseCode) {

        dao.insertProductRef(mpProduct.getId(), mpProduct.getMarketside().getKey(), productBaseCode);
    }

    @Override
    public void createProductRef(String mpProductId ,  int marketSideId, int productId) {
        dao.insertProductRef(mpProductId, marketSideId, productId);
    }

    @Override
    public void createDraftProductRef(String mpProductId, int mpMarketSide, String productBaseCode) {
        dao.insertDraftProductRef(mpProductId, mpMarketSide, productBaseCode);
    }
}
