package com.kindminds.drs.api.data.cqrs.store.event.dao.productV2;

import com.kindminds.drs.Country;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductVariationDao {

    Optional<List<String>> getIds(String productId, Country marketSide);


    List<String> findMarketSides(String productBaseCode , String productVariationCode);


    List<Object[]> get(String productId , OffsetDateTime productCreateTime  , Country marketSide);

}
