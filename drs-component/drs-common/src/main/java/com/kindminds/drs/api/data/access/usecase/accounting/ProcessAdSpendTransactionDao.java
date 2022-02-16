package com.kindminds.drs.api.data.access.usecase.accounting;

import com.kindminds.drs.api.v1.model.marketing.AdSpendTransaction;

import java.util.List;

public interface ProcessAdSpendTransactionDao {
    boolean isAdSpendProcessed();
    List<AdSpendTransaction> getSponsoredAdTransactions();
    List<AdSpendTransaction> getHeadlineAdTransactions();
}