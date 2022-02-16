package com.kindminds.drs.core.biz.settlement;

import java.util.Date;
import java.util.List;

public class RawMarketSideTransaction extends AbstractMarketSideTransaction{

    public RawMarketSideTransaction(Integer id, Date transactionDate, String type,
                                     String source, String sourceId, String sku) {

        super(id, transactionDate, type, source, sourceId, sku);
    }

    public RawMarketSideTransaction(Integer id, Date transactionDate, String type,
                                    String source, String sourceId, String sku ,String description) {

        super(id, transactionDate, type, source, sourceId, sku , description);
    }

    @Override
    public List<Integer> process(Date periodStart, Date periodEnd) {
        return null;
    }
}
