package com.kindminds.drs.api.v2.biz.domain.model.settlement;

import com.kindminds.drs.api.v2.biz.domain.model.Repository;
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;

import java.util.Date;
import java.util.List;

public interface MarketSideTransactionRepo extends Repository<MarketSideTransaction> {

    List<MarketSideTransaction> findNonProcessedTransactions(String  kcode);

    List<MarketSideTransaction> findNonProcessedTransactionById(Integer mstId);

    List<MarketSideTransaction> findRawTransactions(Date startPoint, Date endPoint , String  kcode);
}
