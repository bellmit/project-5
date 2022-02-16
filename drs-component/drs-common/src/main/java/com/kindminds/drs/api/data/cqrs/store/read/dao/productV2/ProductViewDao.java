package com.kindminds.drs.api.data.cqrs.store.read.dao.productV2;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;


public interface ProductViewDao {

    String saveView(Product product);

    String deleteView(String productId);

    String deleteView(String productId, Country marketSide);


}
