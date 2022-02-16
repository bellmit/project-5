package com.kindminds.drs.service.usecase.accounting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.kindminds.drs.api.v1.model.accounting.NonProcessedMarketSideTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.service.helper.MarketSideTransactionHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.accounting.ProcessMarketSideTransactionUco;

import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
//import com.kindminds.drs.v1.model.impl.MarketSideTransactionImpl;
import com.kindminds.drs.util.DateHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestProcessMarketSideTransactionUcoNew {
	
	@Autowired private ProcessMarketSideTransactionUco uco;
	@Autowired private DrsTransactionDao drsTransactionRepo;
	@Autowired private MarketSideTransactionHelper marketSideTransactionHelper;
	
	@Test @Transactional
	public void testCollectOrdersFromAmazon(){
		Date start = this.asUtcDateTime("2017-03-29 23:35:23");
		Date end   = this.asUtcDateTime("2017-03-30 02:26:50");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(3,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-29 23:35:23"),"Order","Amazon.com",  "107-6018347-8027434","K151-DH-700-2W"),        transactionList.get(0));
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-30 00:47:38"),"Order","Amazon.ca",   "702-8887439-2867431","K526-PawboPlus-US-CA"),  transactionList.get(1));
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-30 02:26:49"),"Order","Amazon.co.uk","202-4718725-8901911","K510-85U06R01R0-Orange"),transactionList.get(2));
	}
	
	@Test @Transactional
	public void testCollectOrderFromTrueToSource(){
		Date start = this.asUtcDateTime("2017-02-15 14:41:56");
		Date end   = this.asUtcDateTime("2017-02-15 14:41:57");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-02-15 14:41:56"),"Order","TrueToSource","USTTS1090","K504-TD-3140B"),transactionList.get(0));
	}
	
	@Test @Transactional
	public void testCollectRefundsFromAmazon(){
		Date start = this.asUtcDateTime("2017-03-22 03:39:45");
		Date end   = this.asUtcDateTime("2017-03-22 03:39:46");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-22 03:39:45"),"Refund","Amazon.com","104-0873894-2813056","K151-DH-700-2VP"),transactionList.get(0));
	}
	
	@Test @Transactional
	public void testCollectOtherTransactionFromAmazonWithoutOrderId(){
		Date start = this.asUtcDateTime("2017-03-13 17:07:57");
		Date end   = this.asUtcDateTime("2017-03-13 17:07:58");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-13 17:07:57"),"other-transaction","Amazon.com",null,"K508-TC056BU","WAREHOUSE_DAMAGE"),transactionList.get(0));
	}
	
	@Test @Transactional
	public void testCollectOtherTransactionFromAmazonComInboundMissing(){
		Date start = this.asUtcDateTime("2017-09-15 12:02:13");
		Date end   = this.asUtcDateTime("2017-09-15 12:02:14");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-09-15 12:02:13"),"other-transaction","Amazon.com",null,"K520-TF002H-S-1AR","MISSING_FROM_INBOUND"),transactionList.get(0));
	}
	
	@Test @Transactional
	public void testCollectOtherTransactionFromAmazonCaInboundMissing(){
		Date start = this.asUtcDateTime("2017-05-14 00:00:00");
		Date end   = this.asUtcDateTime("2017-05-14 00:00:01");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-05-14 00:00:00"),"other-transaction","Amazon.ca",null,"K510-85U05001R0","MISSING_FROM_INBOUND"),transactionList.get(0));
	}

	@Test @Transactional
	public void testCollectFulfillmentOrder(){
		Date start = this.asUtcDateTime("2017-04-07 17:35:40");
		Date end   = this.asUtcDateTime("2017-04-07 17:35:41");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-07 17:35:40"),"Order","Amazon.com","S01-4194729-0650941","K520-TF002C-SC-1AB"),transactionList.get(0));
	}
	
	@Test @Transactional
	public void testCollectFulfillmentOrderIgnoreEbay(){
		Date start = this.asUtcDateTime("2017-03-20 16:42:13");
		Date end   = this.asUtcDateTime("2017-03-20 16:42:14");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
	}
	
	@Test @Transactional
	public void testCollectFulfillmentOrderIgnoreTrueToSource(){
		Date start = this.asUtcDateTime("2017-02-17 02:22:11");
		Date end   = this.asUtcDateTime("2017-02-17 02:22:12");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(0,transactionList.size());
	}
	
	@Test @Transactional
	public void testCollectOtherTransactionFromAmazonWithOrderId(){
		Date start = this.asUtcDateTime("2017-03-30 08:34:46");
		Date end   = this.asUtcDateTime("2017-03-30 08:34:47");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-30 08:34:46 UTC"),"other-transaction","Amazon.com","109-5197861-9193069","K504-TD-3140B","REVERSAL_REIMBURSEMENT"),transactionList.get(0));
	}

	@Test @Transactional
	public void testCollectFbaReturnToSellableFromAmazonCom(){
		Date start = this.asUtcDateTime("2017-03-25 06:39:08");
		Date end   = this.asUtcDateTime("2017-03-25 06:39:09");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-25 06:39:08 UTC"),"FBA Return To Sellable","Amazon.com","002-3220172-8433824","K151-DH-700-2VP"),transactionList.get(0));
	}
	
	@Test @Transactional
	public void testCollectFbaReturnToSellableFromAmazonFr(){
		Date start = this.asUtcDateTime("2017-09-09 13:52:47");
		Date end   = this.asUtcDateTime("2017-09-09 13:52:48");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(1,transactionList.size());
		//assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-09-09 13:52:47 UTC"),"FBA Return To Sellable","Amazon.fr","406-3409043-6111546","K521-CHACOBAG001"),transactionList.get(0));
	}
	
	@Test @Transactional
	public void testCollectFbaReturnToSupplier(){
		Date start = this.asUtcDateTime("2017-10-01 00:00:00");
		Date end   = this.asUtcDateTime("2017-10-01 00:00:01");
		this.uco.collectMarketSideTransactionsForTest(start,end);
		List<NonProcessedMarketSideTransaction> transactionList = this.uco.getNonProcessedTransactionList();
		assertEquals(7,transactionList.size());
//		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.com",  null,"K491-01A1BR26CWGD"),transactionList.get(0));
//		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.com",  null,"K491-01A1BR26CWMB"),transactionList.get(1));
//		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.com",  null,"K491-01A1BR26CWMG"),transactionList.get(2));
//		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.co.uk",null,"K525-X5"          ),transactionList.get(3));
//		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.co.uk",null,"K525-X5-Combo"    ),transactionList.get(4));
//		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.co.uk",null,"K525-X5-DE"       ),transactionList.get(5));
//		assertEquals(new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.co.uk",null,"K525-X5-IT"       ),transactionList.get(6));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfTrueToSource(){
		Date start = this.asUtcDate("2017-02-05");
		Date end   = this.asUtcDate("2017-02-19");
		String orderId = "USTTS1090";

//		MarketSideTransactionImpl transaction = new MarketSideTransactionImpl(this.asUtcDateTime("2017-02-15 14:41:56"),"Order","TrueToSource","USTTS1090","K504-TD-3140B");
//		List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K504-TD-3140B","UNS","IVS","USD","121.56","TWD","1600","0.05","0.03129",null));
//	    transaction.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(transaction);
//	    List<Integer> dtIdList = processor.process(start,end,transaction);
//		DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-02-15 14:41:56 +0000",1,"K504-TD-3140B",1,"TrueToSource",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","118.000000","4.773572","5.900000",null,"3.020000","11.801180","50.064000","42.441248",null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "121.560000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K504","SS2SP_Unit_Inventory_Payment",              "TWD","1680.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K504","SS2SP_UnitProfitShareAddition",             "USD",  "42.441248"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-17.253572"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));

	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfEbayDe(){
		Date start = this.asUtcDate("2017-05-28");
		Date end   = this.asUtcDate("2017-06-11");
		String orderId = "2583";
//		MarketSideTransactionImpl transaction = new MarketSideTransactionImpl(this.asUtcDateTime("2017-06-05 12:12:29"),"Order","eBay.de",orderId,"K526-PawboPlus-EU3");
//		List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K526-PawboPlus-EU3","UNS","IVS","USD","147.33","TWD","3800","0.05","0.024570","0.02911"));
//	    transaction.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(transaction);
//	    List<Integer> dtIdList = processor.process(start,end,transaction);
//		DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-06-05 12:12:29 +0000",1,"K526-PawboPlus-EU3",1,"eBay.de",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("EUR","167.226891","7.224202","26.025000","0.000000","10.863832","17.859832","110.618000","-5.363975",null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K6","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "147.330000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K526","SS2SP_Unit_Inventory_Payment",              "TWD","3990.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K526","SS2SP_UnitProfitShareAddition",             "EUR",  "-5.363975"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K6","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD",  "-8.972185"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfEbayUs(){
		Date start = this.asUtcDate("2017-05-28");
		Date end   = this.asUtcDate("2017-06-11");
		String orderId = "2584";
//		MarketSideTransactionImpl transaction = new MarketSideTransactionImpl(this.asUtcDateTime("2017-06-09 04:21:18"),"Order","eBay.com",orderId,"K526-PawboPlus-US");
//		List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K526-PawboPlus-US","UNS","IVS","USD","171.50","TWD","3180","0.05","0.032080",null));
//	    transaction.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(transaction);
//	    List<Integer> dtIdList = processor.process(start,end,transaction);
//		DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-06-09 04:21:18 +0000",1,"K526-PawboPlus-US",1,"eBay.com",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","169.000000","7.300800","15.590000","0.000000","6.400000","18.049200","102.014400","19.645600",null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "171.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K526","SS2SP_Unit_Inventory_Payment",              "TWD","3339.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K526","SS2SP_UnitProfitShareAddition",             "USD",  "19.645600"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-31.790800"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonCom(){
		Date start = this.asUtcDate("2017-03-19");
		Date end   = this.asUtcDate("2017-04-02");
	    String orderId = "112-1780005-3010642";
	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
	   // allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2VP","UNS","IVS","USD","19.81","TWD","320","0.05","0.03127",null));
//	    MarketSideTransactionImpl transaction = new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-29 23:35:18"),"Order","Amazon.com",orderId,"K151-DH-700-2VP");
//	    transaction.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(transaction);
//	    List<Integer> dtIdList = processor.process(start,end,transaction);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-03-29 23:35:18 +0000",1,"K151-DH-700-2VP",1,"Amazon.com",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","25.000000","1.080000","3.750000","0.000000","4.180000","2.670000","10.006400","3.313600", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "19.810000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K151","SS2SP_Unit_Inventory_Payment",              "TWD","336.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K151","SS2SP_UnitProfitShareAddition",             "USD",  "3.313600"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-3.820000"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonComWithMultipleUnits(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
	    String orderId = "111-0200757-4442609";
//	    MarketSideTransactionImpl transaction = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-06 17:37:13"),"Order","Amazon.com",orderId,"K151-DH-700-2W");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2W","UNS","IVS","USD","15.15","TWD","285","0.05","0.03244",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2W","UNS","IVS","USD","15.15","TWD","285","0.05","0.03244",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2W","UNS","IVS","USD","15.15","TWD","285","0.05","0.03244",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2W","UNS","IVS","USD","15.15","TWD","285","0.05","0.03244",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2W","UNS","IVS","USD","15.15","TWD","285","0.05","0.03244",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2W","UNS","IVS","USD","15.15","TWD","285","0.05","0.03244",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2W","UNS","IVS","USD","15.15","TWD","285","0.05","0.03244",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-700-2W","UNS","IVS","USD","15.15","TWD","285","0.05","0.03244",null));
//	    transaction.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(transaction);
//	    List<Integer> dtIdList = processor.process(start,end,transaction);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-04-06 17:37:13 +0000",1,"K151-DH-700-2W",1,"Amazon.com",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","22.000000","0.947760","3.300000","0.000000","4.180000","2.343066","9.245400","1.983774", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "15.150000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K151","SS2SP_Unit_Inventory_Payment",              "TWD","299.250000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K151","SS2SP_UnitProfitShareAddition",             "USD",  "1.983774"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.577760"));
//	    for(int i=0;i<8;i++){
//	    	expect.setTransactionSequence(i+1);
//	    	assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(i)));
//	    }
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonComWithMultipleUnitsPromotion(){
		Date start = this.asUtcDate("2017-07-23");
		Date end   = this.asUtcDate("2017-08-06");
	    String orderId = "113-0239588-6801040";
//	    MarketSideTransactionImpl transaction = new MarketSideTransactionImpl(this.asUtcDateTime("2017-07-29 22:19:26"),"Order","Amazon.com",orderId,"K151-DH-500W");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K151-DH-500W","UNS","IVS","USD","9.61","TWD","90","0.05","0.03082",null));
//	    transaction.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(transaction);
//	    List<Integer> dtIdList = processor.process(start,end,transaction);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-07-29 22:19:26 +0000",1,"K151-DH-500W",1,"Amazon.com",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","5.600000","0.240212","1.000000","0.000000","2.990000","0.593852","2.773800","-1.997864", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "9.610000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K151","SS2SP_Unit_Inventory_Payment",              "TWD","94.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K151","SS2SP_UnitProfitShareAddition",             "USD","-1.997864"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD","-8.240212"));
//	    for(int i=0;i<13;i++){
//	    	expect.setTransactionSequence(i+1);
//	    	assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(i)));
//	    }
//	    expect.setTransactionSequence(14);
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","5.500000","0.235923","1.000000","0.000000","2.990000","0.583248","2.773800","-2.082971", null));
//	    expect.getSettleableItemList().clear();
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "9.610000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K151","SS2SP_Unit_Inventory_Payment",              "TWD","94.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K151","SS2SP_UnitProfitShareAddition",             "USD","-2.082971"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD","-8.335923"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(13)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonComFulfillment(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
	    String orderId = "S01-4194729-0650941";
//	    MarketSideTransactionImpl order = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-07 17:35:40"),"Order","Amazon.com",orderId,"K520-TF002C-SC-1AB");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K520-TF002C-SC-1AB","UNS","IVS","USD","27.77","TWD","509.85","0.05","0.03208",null));
//	    order.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(order);
//	    List<Integer> dtIdList = processor.process(start,end,order);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-04-07 17:35:40 +0000",1,"K520-TF002C-SC-1AB",1,"Amazon.com",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","0.000000","0.000000","0.000000","0.000000","5.950000","0.000000","16.355988","-22.305988", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "27.770000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K520","SS2SP_Unit_Inventory_Payment",              "TWD","535.342500"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K520","SS2SP_UnitProfitShareAddition",             "USD","-22.305988"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD","-33.720000"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonCoUk(){ //String orderId = "204-9541414-1123550"; // 'K520-TF002P-S-1AB', 'K520-TF002H-S-1AB'
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
	    String orderId = "203-2364787-0461157";
//	    MarketSideTransactionImpl order = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-13 15:38:19"),"Order","Amazon.co.uk",orderId,"K510-85U06B01R0-Blue");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K510-85U06B01R0-Blue","UNS","IVS","USD","30","TWD","509.85","0.05","0.023850",null));
//	    order.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(order);
//	    List<Integer> dtIdList = processor.process(start,end,order);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-04-13 15:38:19 +0000",1,"K510-85U06B01R0-Blue",1,"Amazon.co.uk",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("GBP","32.500000","1.404000","4.680000","0.000000","1.700000","3.471000","12.159923","9.085077", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K4","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "30.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K510","SS2SP_Unit_Inventory_Payment",              "TWD","535.342500"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K510","SS2SP_UnitProfitShareAddition",             "GBP",  "9.085077"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K4","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD",  "0.987191"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonCa(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
		String orderId = "702-4799439-6163414";
//	    MarketSideTransactionImpl order = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-04 06:02:02"),"Order","Amazon.ca",orderId,"K510-85U06R01R0-Orange");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K510-85U06R01R0-Orange","UNS","IVS","USD","29.19","TWD","510","0.05","0.039570",null));
//	    order.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(order);
//	    List<Integer> dtIdList = processor.process(start,end,order);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-04-04 06:02:02 +0000",1,"K510-85U06R01R0-Orange",1,"Amazon.ca",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("CAD","43.000000","1.857600","6.450000","0.060000","5.570000","4.592400","20.180700","4.289300", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K5","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "29.190000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K510","SS2SP_Unit_Inventory_Payment",              "TWD","535.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K510","SS2SP_UnitProfitShareAddition",             "CAD",  "4.289300"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K5","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-7.265616"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonDe(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
		String orderId = "302-9982638-0535552";
//	    MarketSideTransactionImpl order = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-06 01:34:06"),"Order","Amazon.de",orderId,"K510-85U06R01R0-Orange");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K510-85U06R01R0-Orange","UNS","IVS","USD","33.80","TWD","510","0.05","0.039570","0.029630"));
//	    order.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(order);
//	    List<Integer> dtIdList = processor.process(start,end,order);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-04-06 01:34:06 +0000",1,"K510-85U06R01R0-Orange",1,"Amazon.de",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("EUR","36.670000","1.584144","5.280000","0.000000","3.390000","3.916356","15.111300","7.388200", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K6","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "33.800000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K510","SS2SP_Unit_Inventory_Payment",              "TWD","535.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K510","SS2SP_UnitProfitShareAddition",             "EUR",  "7.388200"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K6","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-5.680850"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonIt(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
		String orderId = "404-6167338-6241900";
//	    MarketSideTransactionImpl order = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-11 13:54:30"),"Order","Amazon.it",orderId,"K510-85U06B01R0-Blue");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K510-85U06B01R0-Blue","UNS","IVS","USD","33.80","TWD","510","0.05","0.039570","0.029630"));
//	    order.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(order);
//	    List<Integer> dtIdList = processor.process(start,end,order);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-04-11 13:54:30 +0000",1,"K510-85U06B01R0-Blue",1,"Amazon.it",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("EUR","36.670000","1.584144","5.280000","0.000000","3.390000","3.916356","15.111300","7.388200", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K8","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "33.800000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K510","SS2SP_Unit_Inventory_Payment",              "TWD","535.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K510","SS2SP_UnitProfitShareAddition",             "EUR",  "7.388200"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K8","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-5.680850"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonFr(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
		String orderId = "406-1982118-5874760";
//	    MarketSideTransactionImpl order = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-14 10:24:43"),"Order","Amazon.fr",orderId,"K510-85U06B01R0-Blue");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K510-85U06B01R0-Blue","UNS","IVS","USD","33.80","TWD","510","0.05","0.039570","0.029630"));
//	    order.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(order);
//	    List<Integer> dtIdList = processor.process(start,end,order);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-04-14 10:24:43 +0000",1,"K510-85U06B01R0-Blue",1,"Amazon.fr",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("EUR","36.670000","1.584144","5.280000","0.000000","3.390000","3.916356","15.111300","7.388200", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K7","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "33.800000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K510","SS2SP_Unit_Inventory_Payment",              "TWD","535.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K510","SS2SP_UnitProfitShareAddition",             "EUR",  "7.388200"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K7","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-5.680850"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOrderOfAmazonEs(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
		String orderId = "408-3319041-5243530";
//	    MarketSideTransactionImpl order = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-04 11:06:16"),"Order","Amazon.es",orderId,"K510-85U06B01R0-Blue");
//	    List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K510-85U06B01R0-Blue","UNS","IVS","USD","33.80","TWD","510","0.05","0.039570","0.029630"));
//	    order.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(order);
//	    List<Integer> dtIdList = processor.process(start,end,order);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("Order","2017-04-04 11:06:16 +0000",1,"K510-85U06B01R0-Blue",1,"Amazon.es",orderId,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("EUR","36.670000","1.584144","5.280000","0.000000","3.390000","3.916356","15.111300","7.388200", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K9","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "33.800000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K510","SS2SP_Unit_Inventory_Payment",              "TWD","535.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K510","SS2SP_UnitProfitShareAddition",             "EUR",  "7.388200"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K9","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-5.680850"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromRefundOfAmazonComWithSalesTaxServiceFee(){
		Date start = this.asUtcDate("2017-03-19");
		Date end   = this.asUtcDate("2017-04-02");
		String orderId = "002-0119321-3120253";	
//		MarketSideTransactionImpl transaction = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-01 07:11:02"),"Refund","Amazon.com",orderId,"K508-TC3582BU");
//		MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(transaction);
//		List<Integer> dtIds = processor.process(start,end,transaction);
//		String ivsName = "IVS-K508-5";
//		String unsName = "UNS-K2-54";
//		DrsTransactionImpl expect = new DrsTransactionImpl("Refund","2017-04-01 07:11:02 +0000",1,"K508-TC3582BU",1,"Amazon.com",orderId,ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
//		expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","-19.990000","-0.148706","-3.000000","-0.040000","-2.990000","-2.825327","-6.005900","-4.980067","-0.600000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  TransactionLineType.MS2SS_UNIT_DDP_REFUND.getName(),                           "USD", "-14.480000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K508",TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND.getName(),                     "TWD","-199.500000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K508",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION.getName(),             "USD",  "-4.980067"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND.getName(),    "USD",   "0.668706"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(5,"K2","K508",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE.getName(),  "USD",  "-3.630000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(6,"K3","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE.getName(),"USD",  "-3.630000"));
//		assertEquals(expect,this.drsTransactionRepo.query(dtIds.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromRefundOfAmazonComMultipleItemsAndPromotion(){
		Date start = this.asUtcDate("2017-08-06");
		Date end   = this.asUtcDate("2017-08-20");
		String orderId = "113-0239588-6801040";
//		MarketSideTransactionImpl transaction = new MarketSideTransactionImpl(this.asUtcDateTime("2017-08-11 02:55:39"),"Refund","Amazon.com",orderId,"K151-DH-500W");
//		MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(transaction);
//		List<Integer> dtIds = processor.process(start,end,transaction);
//		String ivsName = "IVS-K151-6";
//		String unsName = "UNS-K2-19";
//		DrsTransactionImpl expect = new DrsTransactionImpl("Refund","2017-08-11 02:55:39 +0000",1,"K151-DH-500W",1,"Amazon.com",orderId,ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
//		expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","-5.600000","-0.041703","-1.000000","0.000000","-2.990000","-0.792361","-2.773800","1.997864","-0.200000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  TransactionLineType.MS2SS_UNIT_DDP_REFUND.getName(),                           "USD", "-9.610000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K151",TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND.getName(),                     "TWD","-94.500000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K151",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION.getName(),             "USD",  "1.997864"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND.getName(),    "USD",  "8.041703"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(5,"K2","K151",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE.getName(),  "USD", "-3.190000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(6,"K3","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE.getName(),"USD", "-3.190000"));
//		for(int i=0;i<13;i++){
//			expect.setTransactionSequence(i+1);
//			assertEquals(expect,this.drsTransactionRepo.query(dtIds.get(i)));
//		}
//	    expect.setTransactionSequence(14);
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","-5.500000","-0.040959","-1.000000","0.000000","-2.990000","-0.778212","-2.773800","2.082971","-0.200000"));
//	    expect.getSettleableItemList().clear();
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  TransactionLineType.MS2SS_UNIT_DDP_REFUND.getName(),                           "USD", "-9.610000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K151",TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND.getName(),                     "TWD","-94.500000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K151",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION.getName(),             "USD",  "2.082971"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND.getName(),    "USD",  "8.140959"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(5,"K2","K151",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE.getName(),  "USD", "-3.190000"));
//		expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(6,"K3","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE.getName(),"USD", "-3.190000"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIds.get(13)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOtherOfAmazonComWarehoustLost(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
//		MarketSideTransactionImpl other = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-05 00:10:20"),"other-transaction","Amazon.com",null,"K507-95ISM-A1020","WAREHOUSE_LOST");
//		List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K507-95ISM-A1020","UNS","IVS","USD","110","TWD","2150","0.05","0.03208",null));
//	    other.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(other);
//	    List<Integer> dtIdList = processor.process(start,end,other);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("other-transaction","2017-04-05 00:10:20 +0000",1,"K507-95ISM-A1020",1,"Amazon.com",null,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","139.530000","6.027696","0.000000","0.000000","0.000000","14.901804","68.972000","49.628500", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "110.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K507","SS2SP_Unit_Inventory_Payment",              "TWD","2257.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K507","SS2SP_UnitProfitShareAddition",             "USD",  "49.628500"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD",  "23.502304"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOtherOfAmazonComWarehoustDamage(){
		Date start = this.asUtcDate("2017-01-22");
		Date end   = this.asUtcDate("2017-02-05");
//		MarketSideTransactionImpl other = new MarketSideTransactionImpl(this.asUtcDateTime("2017-01-24 04:57:01"),"other-transaction","Amazon.com",null,"K504-TD-3140B","WAREHOUSE_DAMAGE");
//		List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K504-TD-3140B","UNS","IVS","USD","90","TWD","2000","0.05","0.03208",null));
//	    other.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(other);
//	    List<Integer> dtIdList = processor.process(start,end,other);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("other-transaction","2017-01-24 04:57:01 +0000",1,"K504-TD-3140B",1,"Amazon.com",null,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","96.290000","4.039173","0.000000","0.000000","0.000000","9.985658","64.160000","18.105169", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD",  "90.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K504","SS2SP_Unit_Inventory_Payment",              "TWD","2100.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K504","SS2SP_UnitProfitShareAddition",             "USD",  "18.105169"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD",   "2.250827"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOtherOfAmazonComMissingFromInbound(){
		Date start = this.asUtcDate("2017-09-03");
		Date end   = this.asUtcDate("2017-09-17");
//		MarketSideTransactionImpl other = new MarketSideTransactionImpl(this.asUtcDateTime("2017-09-15 12:02:13"),"other-transaction","Amazon.com",null,"K520-TF002H-S-1AR","MISSING_FROM_INBOUND");
//		List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K520-TF002H-S-1AR","UNS","IVS","USD","32.19","TWD","566.88","0.05","0.032760",null));
//	    other.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(other);
//	    System.out.println(processor.getClass().getName());
//	    List<Integer> dtIdList = processor.process(start,end,other);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("other-transaction","2017-09-15 12:02:13 +0000",1,"K520-TF002H-S-1AR",1,"Amazon.com",null,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("USD","31.000000","1.293072","0.000000","0.000000","0.000000","3.196751","18.570989","7.939188", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "32.190000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K520","SS2SP_Unit_Inventory_Payment",              "TWD","595.224000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K520","SS2SP_UnitProfitShareAddition",             "USD",  "7.939188"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-2.483072"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOtherOfAmazonCaMissingFromInboundMultipleUnits(){
		Date start = this.asUtcDate("2017-05-14");
		Date end   = this.asUtcDate("2017-05-28");
//		MarketSideTransactionImpl other = new MarketSideTransactionImpl(this.asUtcDateTime("2017-05-14 00:00:00"),"other-transaction","Amazon.ca",null,"K510-85U05001R0","MISSING_FROM_INBOUND");
//		List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//		for(int i=0;i<20;i++){
//			allocationInfos.add(new SkuShipmentAllocationInfoImpl("K510-85U05001R0","UNS","IVS","USD","54.52","TWD","1220.00","0.05","0.042870",null));
//		}
//	    other.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(other);
//	    List<Integer> dtIdList = processor.process(start,end,other);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("other-transaction","2017-05-14 00:00:00 +0000",1,"K510-85U05001R0",1,"Amazon.ca",null,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("CAD","74.340000","3.121314","0.000000","0.000000","0.000000","7.716566","52.301400","11.200720", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K5","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD",  "54.520000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K510","SS2SP_Unit_Inventory_Payment",              "TWD","1281.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K510","SS2SP_UnitProfitShareAddition",             "CAD",  "11.200720"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K5","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD",   "-1.485581"));
//	    for(int i=0;i<20;i++){
//	    	expect.setTransactionSequence(i+1);
//	    	assertEquals("Fail on transaction "+(i+1),expect,this.drsTransactionRepo.query(dtIdList.get(i)));
//	    }
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOtherOfAmazonFrWarehoustDamage(){
		Date start = this.asUtcDate("2017-03-05");
		Date end   = this.asUtcDate("2017-03-19");
//		MarketSideTransactionImpl other = new MarketSideTransactionImpl(this.asUtcDateTime("2017-03-05 20:53:36"),"other-transaction","Amazon.fr",null,"K526-PawboPlus-EU3","WAREHOUSE_DAMAGE");
//		List<SkuShipmentAllocationInfo> allocationInfos = new ArrayList<>();
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K526-PawboPlus-EU3","UNS","IVS","USD","110","TWD","2150","0.05","1000","0.03048"));
//	    allocationInfos.add(new SkuShipmentAllocationInfoImpl("K526-PawboPlus-EU3","UNS","IVS","USD","110","TWD","2150","0.05","1000","0.03048"));
//	    other.setAllocationInfos(allocationInfos);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(other);
//	    List<Integer> dtIdList = processor.process(start,end,other);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("other-transaction","2017-03-05 20:53:36 +0000",1,"K526-PawboPlus-EU3",1,"Amazon.fr",null,"IVS","UNS",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("EUR","144.150000","6.227280","0.000000","0.000000","0.000000","15.395220","65.532000","56.995500", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K7","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "110.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K526","SS2SP_Unit_Inventory_Payment",              "TWD","2257.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K526","SS2SP_UnitProfitShareAddition",             "EUR",  "56.995500"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K7","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD",  "37.995216"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
//	    expect.setTransactionSequence(2);
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(1)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromOtherOfAmazonCoUkFbaReversal(){
		Date start = this.asUtcDate("2017-04-02");
		Date end   = this.asUtcDate("2017-04-16");
//		MarketSideTransactionImpl other = new MarketSideTransactionImpl(this.asUtcDateTime("2017-04-03 05:04:50"),"other-transaction","Amazon.co.uk","204-0279759-9928322","K510-85U06B01R0-Blue","REVERSAL_REIMBURSEMENT");
//	    other.setAssignedSourceOrderSeq(0);
//	    MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(other);
//	    List<Integer> dtIdList = processor.process(start,end,other);
//	    DrsTransactionImpl expect = new DrsTransactionImpl("other-transaction","2017-04-03 05:04:50 +0000",1,"K510-85U06B01R0-Blue",1,"Amazon.co.uk","204-0279759-9928322","IVS-K510-14","UNS-K2-56",new ArrayList<DrsTransactionLineItem>());
//	    expect.setSettleableItemElements(new DrsTransactionLineItemSourceImpl("GBP","32.620000","1.409184","0.000000","0.000000","0.000000","3.483816","13.035600","14.691400", null));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K4","K2",  "MS2SS_UNIT_DDP_PAYMENT",                    "USD", "33.800000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K510","SS2SP_Unit_Inventory_Payment",              "TWD","535.500000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K510","SS2SP_UnitProfitShareAddition",             "GBP", "14.691400"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K4","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales","USD",  "5.329936"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromFbaUsReturnToSupplier(){
		Date start = this.asUtcDate("2017-10-01");
		Date end   = this.asUtcDate("2017-10-14");
//		MarketSideTransactionImpl rt = new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.com",null,"K491-01A1BR26CWGD");
//		MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(rt);
//		List<Integer> dtIdList = processor.process(start,end,rt);
//		assertEquals(11,dtIdList.size());
//		DrsTransactionImpl expect = new DrsTransactionImpl("FBA Return to Supplier","2017-10-01 00:00:00 +0000",1,"K491-01A1BR26CWGD",1,"Amazon.com",null,"IVS-K491-1","UNS-K2-7",new ArrayList<DrsTransactionLineItem>());
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_Product_Inventory_Return", "USD",    "0.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K491","SS2SP_Unit_Inventory_Payment",   "TWD", "1323.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K491","SS2SP_Unit_Inventory_Sell_Back", "TWD","-1323.000000"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
//	    for(int i=0;i<11;i++){
//	    	expect.setTransactionSequence(i+1);
//	    	assertEquals("Fail on transaction "+(i+1),expect,this.drsTransactionRepo.query(dtIdList.get(i)));
//	    }
	}
	
	@Test @Transactional
	public void testGenerateDrsTransactionFromFbaUkReturnToSupplier(){
		Date start = this.asUtcDate("2017-10-01");
		Date end   = this.asUtcDate("2017-10-14");
//		MarketSideTransactionImpl rt = new MarketSideTransactionImpl(this.asUtcDateTime("2017-10-01 00:00:00 UTC"),"FBA Return to Supplier","Amazon.co.uk",null,"K525-X5-DE");
//		MarketSideTransactionProcessor processor = this.marketSideTransactionHelper.getProcessor(rt);
//		List<Integer> dtIdList = processor.process(start,end,rt);
//		assertEquals(25,dtIdList.size());
//		DrsTransactionImpl expect = new DrsTransactionImpl("FBA Return to Supplier","2017-10-01 00:00:00 +0000",1,"K525-X5-DE",1,"Amazon.co.uk",null,"IVS-K525-2","UNS-K2-40",new ArrayList<DrsTransactionLineItem>());
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K4","K2",  "MS2SS_Product_Inventory_Return", "USD",    "0.000000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K525","SS2SP_Unit_Inventory_Payment",   "TWD", "9981.300000"));
//	    expect.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K525","SS2SP_Unit_Inventory_Sell_Back", "TWD","-9981.300000"));
//	    assertEquals(expect,this.drsTransactionRepo.query(dtIdList.get(0)));
//	    for(int i=0;i<25;i++){
//	    	expect.setTransactionSequence(i+1);
//	    	assertEquals("Fail on transaction "+(i+1),expect,this.drsTransactionRepo.query(dtIdList.get(i)));
//	    }
	}
	
	private Date asUtcDate(String dateStr) {
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd Z");
	}
	
	private Date asUtcDateTime(String dateStr) {
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd HH:mm:ss Z");
	}
	
}
