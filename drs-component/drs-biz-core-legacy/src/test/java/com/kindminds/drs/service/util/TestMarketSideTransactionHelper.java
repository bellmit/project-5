package com.kindminds.drs.service.util;

import com.kindminds.drs.service.helper.MarketSideTransactionHelper;
import com.kindminds.drs.util.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMarketSideTransactionHelper {
	
	@Autowired private MarketSideTransactionHelper helper;
	
	@Test
	public void testGetProcessor(){
//		assertEquals(MarketSideTransactionProcessorShopifyOrder.class,    this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-02-15 14:41:56"),"Order","TrueToSource","USTTS1090","K504-TD-3140B")).getClass());
//		assertEquals(MarketSideTransactionProcessorOrderUS.class,         this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-29 23:35:18"),"Order","Amazon.com","112-1780005-3010642","K151-DH-700-2VP")).getClass());
//		assertEquals(MarketSideTransactionProcessorOrderUK.class,         this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-13 15:38:19"),"Order","Amazon.co.uk","203-2364787-0461157","K510-85U06B01R0-Blue")).getClass());
//		assertEquals(MarketSideTransactionProcessorOrderCA.class,         this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-04 06:02:02"),"Order","Amazon.ca","702-4799439-6163414","K510-85U06R01R0-Orange")).getClass());
//		assertEquals(MarketSideTransactionProcessorOrderDE.class,         this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-06 01:34:06"),"Order","Amazon.de","302-9982638-0535552","K510-85U06R01R0-Orange")).getClass());
//		assertEquals(MarketSideTransactionProcessorOrderIT.class,         this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-11 13:54:30"),"Order","Amazon.it","404-6167338-6241900","K510-85U06B01R0-Blue")).getClass());
//		assertEquals(MarketSideTransactionProcessorOrderFR.class,         this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-14 10:24:43"),"Order","Amazon.fr","406-1982118-5874760","K510-85U06B01R0-Blue")).getClass());
//		assertEquals(MarketSideTransactionProcessorOrderES.class,         this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-04 11:06:16"),"Order","Amazon.es","408-3319041-5243530","K510-85U06B01R0-Blue")).getClass());
//		assertEquals(MarketSideTransactionProcessorRefund.class,          this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-01 07:11:02"),"Refund","Amazon.com","002-0119321-3120253","K508-TC3582BU")).getClass());
//		assertEquals(MarketSideTransactionProcessorFbaLostDamage.class,   this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-05 00:10:20"),"other-transaction","Amazon.com",null,"K507-95ISM-A1020","WAREHOUSE_LOST")).getClass());
//		assertEquals(MarketSideTransactionProcessorFbaLostDamage.class,   this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-01-24 04:57:01"),"other-transaction","Amazon.com",null,"K504-TD-3140B","WAREHOUSE_DAMAGE")).getClass());
//		assertEquals(MarketSideTransactionProcessorFbaReimbursement.class,this.helper.getProcessor(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-03 05:04:50"),"other-transaction","Amazon.co.uk","204-0279759-9928322","K510-85U06B01R0-Blue","REVERSAL_REIMBURSEMENT")).getClass());
	}
	
	private Date asUtcDateTime(String dateStr) {
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd HH:mm:ss Z");
	}
	
}
