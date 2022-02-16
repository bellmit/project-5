package com.kindminds.drs.api.usecase.accounting;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.NonProcessedMarketSideTransaction;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;

public interface ProcessMarketSideTransactionUco {

	List<SettlementPeriod> getSettlementPeriodList();

	String collectMarketSideTransactions(String settlementPeriodId);

	String collectMarketSideTransactions(String settlementPeriodId, String companyCode);

	String processMarketSideTransactions(String settlementPeriodId);

	String processMarketSideTransactions(String settlementPeriodId, String companyCode);

	String processMarketSideTransactions(String settlementPeriodId,int transactionId);

	String deleteNonProcessedTransactions();

	List<NonProcessedMarketSideTransaction> getNonProcessedTransactionList();

	// For Test Use Only
	String collectMarketSideTransactionsForTest(Date start,Date end);

	String collectAndProcessMarketSideTransactions();
	
}
