package com.kindminds.drs.api.v2.biz.domain.model.repo.product;

import com.kindminds.drs.Country;

import com.kindminds.drs.api.v2.biz.domain.model.Repository;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;

import java.util.*;

public interface ProductRepo extends Repository<Product> {

    Optional<Product> find(String id ,Country marketSide);

    List<Product> findAllMarketSide(String id);
}