package com.kindminds.drs.api.data.access.rdb.product;

import java.util.Optional;

public interface LegacyProductDao {
    void insertProductRef(String mpProductId, int mpMarketSide, String productBaseCode);
    void insertDraftProductRef(String mpProductId, int mpMarketSide, String productBaseCode);
    void insertProductRef(String mpProductId, int mpMarketSide, int productId);
    Optional<Integer> getSettlementProductId(String productBaseCode);
}
