package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.api.usecase.accounting.ProcessMarketSideTransactionUco;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.service.helper.MarketSideTransactionHelper;
import com.kindminds.drs.util.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContextLocal.xml" })
public class TestProcessMarketSideTransactionUco2 {
	
	@Autowired private ProcessMarketSideTransactionUco uco;
	@Autowired private DrsTransactionDao drsTransactionRepo;
	@Autowired private MarketSideTransactionHelper marketSideTransactionHelper;
	
	@Test @Transactional
	public void testProcessOrderFromAmazon(){
		/*
		Date start = this.asUtcDateTime("2017-03-29 23:35:23");
		Date end   = this.asUtcDateTime("2017-03-30 02:26:50");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		*/

		//List<MarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		this.uco.processMarketSideTransactions("161",131782);

		/*
		assertEquals(3,transactionList.size());
		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-29 23:35:23"),"Order","Amazon.com",  "107-6018347-8027434","K151-DH-700-2W"),        transactionList.get(0));
		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-30 00:47:38"),"Order","Amazon.ca",   "702-8887439-2867431","K526-PawboPlus-US-CA"),  transactionList.get(1));
		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-30 02:26:49"),"Order","Amazon.co.uk","202-4718725-8901911","K510-85U06R01R0-Orange"),transactionList.get(2));
		*/
	}

	@Test @Transactional
    public void testCollectOrdersFromAmazon() {
	    uco.collectMarketSideTransactions("134");

//        uco.processMarketSideTransactions("134");
    }
	

	
	private Date asUtcDate(String dateStr) {
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd Z");
	}
	
	private Date asUtcDateTime(String dateStr) {
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd HH:mm:ss Z");
	}
	
}
