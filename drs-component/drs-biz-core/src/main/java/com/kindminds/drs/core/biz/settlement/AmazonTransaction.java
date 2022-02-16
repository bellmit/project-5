package com.kindminds.drs.core.biz.settlement;

import java.util.Date;

public abstract class AmazonTransaction extends AbstractMarketSideTransaction {

    public AmazonTransaction(Integer id, Date transactionDate, String type,
                                     String source, String sourceId, String sku) {

        super(id, transactionDate, type, source, sourceId, sku);
    }

    public AmazonTransaction(Integer id, Date transactionDate, String type,
                                     String source, String sourceId, String sku, String description) {

        super(id, transactionDate, type, source, sourceId, sku, description);
    }
}
