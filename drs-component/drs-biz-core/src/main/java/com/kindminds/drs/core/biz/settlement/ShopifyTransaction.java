package com.kindminds.drs.core.biz.settlement;

import java.util.Date;

public abstract class ShopifyTransaction extends AbstractMarketSideTransaction {

    public ShopifyTransaction(int id, Date transactionDate, String type,
                        String source, String sourceId, String sku) {

        super(id, transactionDate, type, source, sourceId, sku);
    }


    public ShopifyTransaction(int id, Date transactionDate, String type,
                        String source, String sourceId, String sku, String description) {

        super(id, transactionDate, type, source, sourceId, sku, description);
    }
}
