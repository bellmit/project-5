package com.kindminds.drs.core.biz.settlement;

import java.util.Date;

public abstract class EBayTransaction extends AbstractMarketSideTransaction {

    public EBayTransaction(Integer id, Date transactionDate, String type,
                     String source, String sourceId, String sku) {

        super(id, transactionDate, type, source, sourceId, sku);
    }

    public EBayTransaction(Integer id, Date transactionDate, String type,
                     String source, String sourceId, String sku, String description) {

        super(id, transactionDate, type, source, sourceId, sku, description);
    }

    public EBayTransaction(Integer id, Date transactionDate, String type,
                     String source, String sourceId, String sku,
                     String description, String exceptionMsg, String stackTrace) {

        super(id, transactionDate, type, source, sourceId, sku, description, exceptionMsg, stackTrace);
    }
}
