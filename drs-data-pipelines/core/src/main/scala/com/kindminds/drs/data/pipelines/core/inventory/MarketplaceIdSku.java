package com.kindminds.drs.data.pipelines.core.inventory;

import java.util.Objects;

public class MarketplaceIdSku {

    private Integer marketplaceId;
    private String marketplaceSku;

    public MarketplaceIdSku(Integer marketplaceId, String marketplaceSku) {
        this.marketplaceId = marketplaceId;
        this.marketplaceSku = marketplaceSku;
    }

    public Integer getMarketplaceId() {
        return marketplaceId;
    }

    public String getMarketplaceSku() {
        return marketplaceSku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketplaceIdSku that = (MarketplaceIdSku) o;
        return Objects.equals(marketplaceId, that.marketplaceId) &&
                Objects.equals(marketplaceSku, that.marketplaceSku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marketplaceId, marketplaceSku);
    }
}
