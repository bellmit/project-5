package com.kindminds.drs.persist.v1.model.mapping.product;

import com.kindminds.drs.api.v1.model.product.AmazonAsin;






public class AmazonAsinImpl implements AmazonAsin {
    //@Id //@Column(name = "id")
    private int id;
    //@Column(name = "product_id")
    private int productId;
    //@Column(name = "marketplace_id")
    private int marketplaceId;
    //@Column(name = "marketplace_name")
    private String marketplaceName;
    //@Column(name = "asin")
    private String asin;

    public AmazonAsinImpl() {
    }

    public AmazonAsinImpl(int id, int productId, int marketplaceId, String marketplaceName, String asin) {
        this.id = id;
        this.productId = productId;
        this.marketplaceId = marketplaceId;
        this.marketplaceName = marketplaceName;
        this.asin = asin;
    }

    @Override
    public int getProductId() {
        return this.productId;
    }

    @Override
    public int getMarketplaceId() {
        return this.marketplaceId;
    }

    @Override
    public String getMarketplaceName() {
        return this.marketplaceName;
    }

    @Override
    public String getAsin() {
        return this.asin;
    }
}
