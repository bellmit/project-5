package com.kindminds.drs.service.usecase.accounting;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.usecase.accounting.DoMs2ssSettlementUco;
import com.kindminds.drs.api.usecase.accounting.MaintainInternationalTransactionUco;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.impl.DrsTransactionImpl;
import com.kindminds.drs.impl.DrsTransactionLineItemImpl;
import com.kindminds.drs.impl.InternationalTransactionImplForTest;
import com.kindminds.drs.impl.InternationalTransactionLineItemImplForTest;
import com.kindminds.drs.impl.ProductBaseImplForTest;
import com.kindminds.drs.impl.ProductSkuImplForTest;
import com.kindminds.drs.v1.model.impl.close.BillStatementImpl;
import com.kindminds.drs.v1.model.impl.close.BillStatementLineItemImpl;
import com.kindminds.drs.util.DateHelper;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestDoMs2ssSettlementUco {
	
	@Autowired private DoMs2ssSettlementUco uco;
	@Autowired private MaintainProductBaseUco productBaseUco;
	@Autowired private MaintainProductSkuUco productSkuUco;
	@Autowired private DrsTransactionDao dtRepo;
	@Autowired private MaintainInternationalTransactionUco internationalTransactionUco;

	private void prepareSkus(){
		this.productBaseUco.insert(new ProductBaseImplForTest("K448",null,"1", "BP-K448-1", "small small", "", null));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-1", "RY1", "K448-RY1", "Jeou Lu AAA", "JLA", "7777777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST"));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-1", "RY2", "K448-RY2", "Jeou Lu BBB", "JLB", "7777777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST"));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-1", "RY3", "K448-RY3", "Jeou Lu CCC", "JLB", "7777777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST"));
	}
	
	@Test @Transactional @Ignore("not yet complete")
	public void testProductReturn(){
//		Date stmntStart = this.toDate("2016-04-17 00:00:00 +0000");
//		Date stmntEnd =   this.toDate("2016-05-01 00:00:00 +0000");
//		int newstmntId = uco.doStatementForTest("K3", "K2", stmntStart, stmntEnd);
//		BillStatement result = uco.getExistingBillStatement(newstmntId);
	}
	
	@Test @Transactional
	public void test_K4_K2_Order_Refund(){
		
		String ivsNameUs = "test-ivs-name-us";
		String ivsNameUk = "test-ivs-name-uk";
		String unsNameUs = "test-uns-name-us";
		String unsNameUk = "test-uns-name-uk";
		String orderIdUs = "113-7907694-8005827";
		String orderIdUk = "203-3748086-4567517";
		
		DrsTransactionImpl orderUs = new DrsTransactionImpl(AmazonTransactionType.ORDER.getValue(),"2015-12-01 00:09:26 +0000",1,"K486-HLB",1,"Amazon.com",orderIdUs,ivsNameUs,unsNameUs,new ArrayList<DrsTransactionLineItem>());
		orderUs.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                           "USD", "20.130000"));
		orderUs.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K486","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		orderUs.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K486","SS2SP_UnitProfitShareAddition",             "USD",  "6.524995"));
		orderUs.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.967925"));
		this.dtRepo.insert(orderUs);
		
		DrsTransactionImpl orderUk = new DrsTransactionImpl(AmazonTransactionType.ORDER.getValue(),"2015-12-01 00:09:26 +0000",1,"K486-HLB",1,"Amazon.co.uk",orderIdUk,ivsNameUk,unsNameUk,new ArrayList<DrsTransactionLineItem>());
		orderUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K4",  "K2","MS2SS_UNIT_DDP_PAYMENT",                           "USD", "20.130000"));
		orderUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K486","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		orderUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K486","SS2SP_UnitProfitShareAddition",             "GBP",  "2.267919"));
		orderUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K4",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-7.134900"));
		this.dtRepo.insert(orderUk);
		
		DrsTransactionImpl refndUk = new DrsTransactionImpl(AmazonTransactionType.REFUND.getValue(),"2015-12-05 04:56:30 +0000",1,"K486-HLB",1,"Amazon.co.uk",orderIdUk,ivsNameUk,unsNameUk,new ArrayList<DrsTransactionLineItem>());
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K4","K2",  TransactionLineType.MS2SS_UNIT_DDP_REFUND.getName(),                                 "USD", "-20.130000"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K486",TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND.getName(),                     "TWD","-257.250000"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K486",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION.getName(),             "GBP",  "-2.267919"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K4","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND.getName(),    "USD",   "7.134900"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(5,"K2","K486",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE.getName(),  "GBP",  "-2.700000"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(6,"K4","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE.getName(),"USD",  "-3.767850"));
		this.dtRepo.insert(refndUk);
		
		Date stmntStart = this.toDate("2015-12-01 00:00:00 +0000");
		Date stmntEnd =   this.toDate("2015-12-10 00:00:00 +0000");
		int newstmntId = uco.doStatementForTest("K4", "K2", stmntStart, stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId,"",stmntStart,stmntEnd,"K4","K2","USD","-3.77","0.00","00.00","00.00","-3.77");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,1,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "20.130000","USD", "20.130000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,                                 unsNameUk, "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,2,StatementLineType.MS2SS_PRODUCT_INVENTORY_REFUND, "USD","-20.130000","USD","-20.130000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_DDP_REFUND,                                 unsNameUk, "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,3,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD", "-7.134900","USD", "-7.130000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,     unsNameUk, "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,4,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD",  "7.134900","USD",  "7.130000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND,    unsNameUk, "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,5,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD", "-3.767850","USD", "-3.770000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE,unsNameUk, "UK"));
		BillStatement result = uco.getExistingBillStatement(newstmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testSettleOrderOneSku(){
		this.prepareSkus();
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K448-RY1",1,"Amazon.com","108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                           "USD", "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder1);
		
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd =   this.toDate("2013-09-01 00:00:00 +0000");
		int newstmntId = uco.doStatementForTest("K3", "K2", stmntStart, stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId,"",stmntStart,stmntEnd,"K3","K2","USD","18.91","0.00","00.00","00.00","18.91");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 1, StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","20.130000","USD","20.130000",null,"K448-RY1", 1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,                unsName, "US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 2, StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD","-1.216250","USD","-1.220000",null,"K448-RY1", 1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES, unsName, "US"));
		BillStatement result = uco.getExistingBillStatement(newstmntId);
		assertEquals(expected,result);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testSettleOrderRefundOneSku(){
		this.prepareSkus();
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K448-RY1",1,  "Amazon.com", "103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                            "USD", "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",               "TWD","257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",              "USD",  "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales", "USD", "-1.216250"));
		DrsTransactionImpl dtRefund = new DrsTransactionImpl("RETAIL_REFUND","2013-08-06 17:08:28 +0000",1,"K448-RY1",1, "Amazon.com", "103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_REFUND",                         "USD", "-20.130000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Refund",                   "TWD","-257.250000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareSubtraction",              "USD", "-13.110995"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailRefund",   "USD",  "-3.013750"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(5,"K2","K448","SS2SP_UnitProfitShareSubtractionRefundFee",     "USD",  "-4.230000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(6,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailRefundFee","USD",  "-4.230000"));
		this.dtRepo.insert(dtOrder2);
		this.dtRepo.insert(dtRefund);

		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd =   this.toDate("2013-09-01 00:00:00 +0000");
		int newstmntId = uco.doStatementForTest("K3", "K2", stmntStart, stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId,"",stmntStart,stmntEnd,"K3","K2","USD", "-8.46", "0.00", "00.00", "00.00", "-8.46");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,1,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "20.130000","USD", "20.130000",null,"K448-RY1",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,                                 unsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,2,StatementLineType.MS2SS_PRODUCT_INVENTORY_REFUND, "USD","-20.130000","USD","-20.130000",null,"K448-RY1",1,TransactionLineType.MS2SS_UNIT_DDP_REFUND,                                 unsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,3,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD", "-1.216250","USD", "-1.220000",null,"K448-RY1",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,     unsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,4,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD", "-3.013750","USD", "-3.010000",null,"K448-RY1",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND,    unsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,5,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD", "-4.230000","USD", "-4.230000",null,"K448-RY1",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE,unsName,"US"));
		BillStatement result = uco.getExistingBillStatement(newstmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional 
	public void test_ORDER_K448_1A_X2_REFUND_K448_1A_X1(){
		this.prepareSkus();
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K448-RY1",1,  "Amazon.com", "108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                 "USD",  "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",               "TWD", "257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",              "USD",   "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales", "USD",  "-1.216250"));
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K448-RY1",1,  "Amazon.com", "103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                 "USD",  "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",               "TWD", "257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",              "USD",   "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales", "USD",  "-1.216250"));
		DrsTransactionImpl dtRefund = new DrsTransactionImpl("RETAIL_REFUND","2013-08-06 17:08:28 +0000",1,"K448-RY1",1, "Amazon.com", "103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_REFUND",                         "USD", "-20.130000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Refund",                   "TWD","-257.250000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareSubtraction",              "USD", "-13.110995"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailRefund",   "USD",  "-3.013750"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(5,"K2","K448","SS2SP_UnitProfitShareSubtractionRefundFee",     "USD",  "-4.230000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(6,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailRefundFee","USD",  "-4.230000"));
		this.dtRepo.insert(dtOrder1);
		this.dtRepo.insert(dtOrder2);
		this.dtRepo.insert(dtRefund);
		
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-09-01 00:00:00 +0000");
		int newstmntId = this.uco.doStatementForTest("K3", "K2", stmntStart, stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId,"",stmntStart,stmntEnd,"K3","K2","USD", "10.46", "0.00","00.00","00.00","10.46");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,1,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "40.260000","USD", "40.260000",null,"K448-RY1",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,                                 unsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,2,StatementLineType.MS2SS_PRODUCT_INVENTORY_REFUND, "USD","-20.130000","USD","-20.130000",null,"K448-RY1",1,TransactionLineType.MS2SS_UNIT_DDP_REFUND,                                 unsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,3,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD", "-2.432500","USD", "-2.430000",null,"K448-RY1",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,     unsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,4,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD", "-3.013750","USD", "-3.010000",null,"K448-RY1",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND,    unsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,5,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD", "-4.230000","USD", "-4.230000",null,"K448-RY1",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE,unsName,"US"));
		BillStatement result = this.uco.getExistingBillStatement(newstmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testImportDutyAndMsdcPaymentOnBehalfOfSsdc(){
		Date stmntStart = this.toDate("2016-04-03 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-04-17 00:00:00 +0000");
		int newstmntId = uco.doSettlement(BillStatementType.DRAFT,"K3","K2",stmntStart,stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId, "", stmntStart, stmntEnd, "K3","K2","USD", "1093.56", "1060.41", "1785.84", "114.02", "482.15");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 1,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","29.440000","USD","29.440000",null,"K486-KNB",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-9","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 2,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","32.450000","USD","32.450000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-9","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 3,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","80.760000","USD","80.760000",null,"K486-HLK",3,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-6","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 4,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","29.720000","USD","29.720000",null,"K486-HLP",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 5,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","64.620000","USD","64.620000",null,"K486-BAL",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-6","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 6,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","92.800000","USD","92.800000",null,"K151-DH-500W",10,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 7,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","20.980000","USD","20.980000",null,"K151-DH-500CP",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-13","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 8,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","728.220000","USD","728.220000",null,"K489-RW70056-01",6,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-8","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 9,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","121.370000","USD","121.370000",null,"K489-RW70057-01",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-5","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,10,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","2108.180000","USD","2108.180000",null,"K488-PLUG100",23,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-11","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,11,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","85.890000","USD","85.890000",null,"K488-PLUG100",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-5","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,12,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","54.660000","USD","54.660000",null,"K491-01A1BR26CWMG",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-7","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,13,StatementLineType.MS2SS_PRODUCT_INVENTORY_REFUND,"USD","-9.280000","USD","-9.280000",null,"K151-DH-500W",1,TransactionLineType.MS2SS_UNIT_DDP_REFUND,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,14,StatementLineType.MS2SS_PRODUCT_INVENTORY_REFUND,"USD","-366.640000","USD","-366.640000",null,"K488-PLUG100",4,TransactionLineType.MS2SS_UNIT_DDP_REFUND,"UNS-K2-11","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,15,StatementLineType.MS2SS_PRODUCT_INVENTORY_REFUND,"USD","-257.670000","USD","-257.670000",null,"K488-PLUG100",3,TransactionLineType.MS2SS_UNIT_DDP_REFUND,"UNS-K2-5","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,16,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-1.713750","USD","-1.710000",null,"K486-KNB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-9","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,17,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-1.859250","USD","-1.860000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-9","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,18,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","10.942250","USD","10.940000",null,"K486-HLK",3,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-6","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,19,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","0.800750","USD","0.800000",null,"K486-HLP",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,20,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","2.715000","USD","2.720000",null,"K486-BAL",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-6","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,21,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","0.151500","USD","0.150000",null,"K151-DH-500W",10,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,22,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","0.087500","USD","0.090000",null,"K151-DH-500CP",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-13","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,23,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","19.490450","USD","19.490000",null,"K489-RW70056-01",6,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-8","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,24,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","3.350075","USD","3.350000",null,"K489-RW70057-01",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-5","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,25,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-487.749000","USD","-487.750000",null,"K488-PLUG100",23,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-11","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,26,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","1.427500","USD","1.430000",null,"K488-PLUG100",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-5","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,27,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","1.555000","USD","1.560000",null,"K491-01A1BR26CWMG",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-7","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,28,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","0.088750","USD","0.090000",null,"K151-DH-500W",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,29,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","53.943000","USD","53.940000",null,"K488-PLUG100",4,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND,"UNS-K2-11","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,30,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-5.032500","USD","-5.030000",null,"K488-PLUG100",3,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND,"UNS-K2-5","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,31,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-3.460000","USD","-3.460000",null,"K151-DH-500W",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,32,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-18.210000","USD","-18.210000",null,"K488-PLUG100",4,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE,"UNS-K2-11","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,33,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-13.060000","USD","-13.060000",null,"K488-PLUG100",3,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE,"UNS-K2-5","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,34,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-27.540000","USD","-27.540000",null,"K486-KNB",     20,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,"UNS-K2-13","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,35,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-27.540000","USD","-27.540000",null,"K486-KNK",     20,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,"UNS-K2-13","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,36,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-13.770000","USD","-13.770000",null,"K486-KNP",     10,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,"UNS-K2-13","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,37,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-15.200000","USD","-15.200000",null,"K486-HLK",     10,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,"UNS-K2-13","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,38,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-33.550000","USD","-33.550000",null,"K486-BAL",     20,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,"UNS-K2-13","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,39,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "-8.670000","USD", "-8.670000",null,"K151-DH-500CP",24,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,"UNS-K2-13","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,40,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-1250.000000","USD","-1250.000000",null,null,null,TransactionLineType.MARKET_SIDE_MARKETING_ACTIVITY,"UNS-K2-11","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,41,StatementLineType.MSDC_PAYMENT_ON_BEHALF_OF_SSDC,"USD","-35.420000","USD","-35.420000",null,null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,42,StatementLineType.MS2SS_SSDC_PAYMENT_ON_BEHALF_OF_MSDC,"USD","126.270000","USD","126.270000",null,null,null,TransactionLineType.MS2SS_SSDC_PAYMENT_ONBEHALF_OF_MSDC_FOR_IMPORT_DUTY,null,"TW"));
		BillStatement result = this.uco.getExistingDraftBillStatement(newstmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testInternationalTransaction(){
		InternationalTransactionImplForTest testTransactionUsK486 = new InternationalTransactionImplForTest(null,"2015-10-23",0,"K3",null,"K2",null,"K486",null,"USD",null,"",new ArrayList<>());
		testTransactionUsK486.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","75.11"));
		testTransactionUsK486.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","13.01"));
		InternationalTransactionImplForTest testTransactionUsK151 = new InternationalTransactionImplForTest(null,"2015-10-24",0,"K3",null,"K2",null,"K151",null,"USD",null,"",new ArrayList<>());
		testTransactionUsK151.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","13.11"));
		testTransactionUsK151.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","13.09"));
		testTransactionUsK151.getLineItems().add(new InternationalTransactionLineItemImplForTest(3, 3,"INVENTORY_PLACEMENT_PROGRAM_COST","","17.88"));
		InternationalTransactionImplForTest testTransactionUK = new InternationalTransactionImplForTest(null,"2013-09-16",0,"K4",null,"K2",null,"K486",null,"USD","37.06","",new ArrayList<>());
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","17.00"));
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","20.06"));
		this.internationalTransactionUco.createForSettlementTest(testTransactionUsK486);
		this.internationalTransactionUco.createForSettlementTest(testTransactionUsK151);
		this.internationalTransactionUco.createForSettlementTest(testTransactionUK);
		Date stmntStart = this.toDate("2015-10-18 00:00:00 +0000");
		Date stmntEnd = this.toDate("2015-11-01 00:00:00 +0000");
		int newstmntId = uco.doSettlement(BillStatementType.DRAFT,"K3","K2",stmntStart,stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId,"",stmntStart,stmntEnd,"K3","K2","USD","205.83","-2878.78","0.00","0.00","-2672.95");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 1,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "53.940000","USD", "53.940000",null,"K486-KNB",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 2,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "28.030000","USD", "28.030000",null,"K486-KNB",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 3,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "56.060000","USD", "56.060000",null,"K486-KNK",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 4,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "29.720000","USD", "29.720000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 5,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "29.720000","USD", "29.720000",null,"K486-HLK",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 6,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "64.540000","USD", "64.540000",null,"K486-BAL",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 7,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","101.190000","USD","101.190000",null,"K486-BAL",3,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 8,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-2.805000","USD","-2.810000",null,"K486-KNB",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 9,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-2.417500","USD","-2.420000",null,"K486-KNB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,10,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-4.915000","USD","-4.920000",null,"K486-KNK",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,11,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-1.472500","USD","-1.470000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,12,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-1.562500","USD","-1.560000",null,"K486-HLK",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,13,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-3.040000","USD","-3.040000",null,"K486-BAL",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,14,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-8.945000","USD","-8.950000",null,"K486-BAL",3,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,15,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-17.880000","USD","-17.880000",null,null,null,TransactionLineType.INVENTORY_PLACEMENT_PROGRAM_COST,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,16,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-13.090000","USD","-13.090000",null,null,null,TransactionLineType.FBA_REMOVAL_FEE,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,17,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-13.010000","USD","-13.010000",null,null,null,TransactionLineType.FBA_REMOVAL_FEE,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,18,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-13.110000","USD","-13.110000",null,null,null,TransactionLineType.FBA_LONG_TERM_STORAGE,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,19,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-75.110000","USD","-75.110000",null,null,null,TransactionLineType.FBA_LONG_TERM_STORAGE,"UNS-K2-4","US"));
		BillStatement result = this.uco.getExistingDraftBillStatement(newstmntId);
		assertEquals(expected,result);
	}
	//TODO: correct test case
	@Test @Transactional
	public void testInternationalTransactionMsdcPaymentOnBehalfOfSsdcFromEuroCompany(){
		Date stmntStart = this.toDate("2017-03-05 00:00:00 +0000");
		Date stmntEnd = this.toDate("2017-03-19 00:00:00 +0000");
		int newstmntId = this.uco.doSettlement(BillStatementType.DRAFT,"K6","K2",stmntStart,stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId,"",stmntStart,stmntEnd,"K6","K2","USD","-202.80","987.63","0.00","0.00","784.83");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,1,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD",  "33.800000","USD",  "33.800000",null,"K510-85U06B01R0-Blue",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-56","DE"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,2,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,       "USD",  "-3.725674","USD",  "-3.730000",null,"K510-85U06B01R0-Blue",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-56","DE"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,3,StatementLineType.MSDC_PAYMENT_ON_BEHALF_OF_SSDC, "EUR","-219.210000","USD","-232.870000",null,                  null, null, TransactionLineType.MARKET_SIDE_ADVERTISING_COST,null, "DE")); 
		BillStatement result = this.uco.getExistingDraftBillStatement(newstmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testInternationalTransactionMs2sp(){
		InternationalTransactionImplForTest testTransactionUsK486 = new InternationalTransactionImplForTest(null,"2015-10-23",CashFlowDirection.MS2SP.getKey(),"K3",null,"K2",null,"K486",null,"USD",null,"",new ArrayList<>());
		testTransactionUsK486.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,66,"PRODUCT_RELATED_ADDITION",         "","75.11"));
		testTransactionUsK486.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,79,"MARKETING_ACTIVITY_EXPENSE_REFUND","","13.01"));
		InternationalTransactionImplForTest testTransactionUsK151 = new InternationalTransactionImplForTest(null,"2015-10-24",CashFlowDirection.SP2MS.getKey(),"K3",null,"K2",null,"K151",null,"USD",null,"",new ArrayList<>());
		testTransactionUsK151.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","13.11"));
		testTransactionUsK151.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","13.09"));
		testTransactionUsK151.getLineItems().add(new InternationalTransactionLineItemImplForTest(3, 3,"INVENTORY_PLACEMENT_PROGRAM_COST","","17.88"));
		InternationalTransactionImplForTest testTransactionUK = new InternationalTransactionImplForTest(null,"2013-09-16",0,"K4",null,"K2",null,"K486",null,"USD","37.06","",new ArrayList<>());
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","17.00"));
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","20.06"));
		this.internationalTransactionUco.createForSettlementTest(testTransactionUsK486);
		this.internationalTransactionUco.createForSettlementTest(testTransactionUsK151);
		this.internationalTransactionUco.createForSettlementTest(testTransactionUK);
		Date stmntStart = this.toDate("2015-10-18 00:00:00 +0000");
		Date stmntEnd = this.toDate("2015-11-01 00:00:00 +0000");
		int newstmntId = uco.doSettlement(BillStatementType.DRAFT,"K3","K2",stmntStart,stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId,"",stmntStart,stmntEnd,"K3","K2","USD","382.07","-2878.78","0.00","0.00","-2496.71");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 1,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "53.940000","USD", "53.940000",null,"K486-KNB",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 2,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "28.030000","USD", "28.030000",null,"K486-KNB",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 3,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "56.060000","USD", "56.060000",null,"K486-KNK",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 4,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "29.720000","USD", "29.720000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 5,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "29.720000","USD", "29.720000",null,"K486-HLK",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 6,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD", "64.540000","USD", "64.540000",null,"K486-BAL",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 7,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,"USD","101.190000","USD","101.190000",null,"K486-BAL",3,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 8,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "-2.805000","USD", "-2.810000",null,"K486-KNB",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 9,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "-2.417500","USD", "-2.420000",null,"K486-KNB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,10,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "-4.915000","USD", "-4.920000",null,"K486-KNK",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,11,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "-1.472500","USD", "-1.470000",null,"K486-HLB",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,12,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "-1.562500","USD", "-1.560000",null,"K486-HLK",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,13,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "-3.040000","USD", "-3.040000",null,"K486-BAL",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,14,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "-8.945000","USD", "-8.950000",null,"K486-BAL",3,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,15,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-17.880000","USD","-17.880000",null,   null,null,TransactionLineType.INVENTORY_PLACEMENT_PROGRAM_COST,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,16,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-13.090000","USD","-13.090000",null,   null,null,TransactionLineType.FBA_REMOVAL_FEE,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,17,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD","-13.110000","USD","-13.110000",null,   null,null,TransactionLineType.FBA_LONG_TERM_STORAGE,"UNS-K2-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,18,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "75.110000","USD", "75.110000",null,   null,null,TransactionLineType.PRODUCT_RELATED_ADDITION,"UNS-K2-4","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,19,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,"USD", "13.010000","USD", "13.010000",null,   null,null,TransactionLineType.MARKETING_ACTIVITY_EXPENSE_REFUND,"UNS-K2-4","US"));
		BillStatement result = this.uco.getExistingDraftBillStatement(newstmntId);
		assertEquals(expected,result);
	}
	//TODO: correct test case
	@Test @Transactional
	public void testInternationalTransactionUk(){
		InternationalTransactionImplForTest testTransactionUsK486 = new InternationalTransactionImplForTest(null,"2016-09-12",CashFlowDirection.MS2SP.getKey(),"K4",null,"K2",null,"K486",null,"USD",null,"",new ArrayList<>());
		testTransactionUsK486.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,66,"PRODUCT_RELATED_ADDITION",         "","75.11"));
		testTransactionUsK486.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,79,"MARKETING_ACTIVITY_EXPENSE_REFUND","","13.01"));
		InternationalTransactionImplForTest testTransactionUsK520 = new InternationalTransactionImplForTest(null,"2016-09-13",CashFlowDirection.SP2MS.getKey(),"K4",null,"K2",null,"K520",null,"USD",null,"",new ArrayList<>());
		testTransactionUsK520.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","13.11"));
		testTransactionUsK520.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","13.09"));
		testTransactionUsK520.getLineItems().add(new InternationalTransactionLineItemImplForTest(3, 3,"INVENTORY_PLACEMENT_PROGRAM_COST","","17.88"));
		InternationalTransactionImplForTest testTransactionUK = new InternationalTransactionImplForTest(null,"2013-09-14",0,"K3",null,"K2",null,"K486",null,"USD","37.06","",new ArrayList<>());
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","17.00"));
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","20.06"));
		this.internationalTransactionUco.createForSettlementTest(testTransactionUsK486);
		this.internationalTransactionUco.createForSettlementTest(testTransactionUsK520);
		this.internationalTransactionUco.createForSettlementTest(testTransactionUK);
		Date stmntStart = this.toDate("2016-09-04 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-09-17 00:00:00 +0000");
		int newstmntId = uco.doSettlement(BillStatementType.DRAFT,"K4","K2",stmntStart,stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newstmntId,"",stmntStart,stmntEnd,"K4","K2","USD","159.49","0.00","0.00","0.00","159.49");
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 1,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,     "USD", "37.980000","USD", "37.980000",null,"K486-HLP",          1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,                             "UNS-K2-23","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 2,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,     "USD", "37.450000","USD", "37.450000",null,"K520-TF002C-SC-1AR",1,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,                             "UNS-K2-26","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 3,StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT,     "USD", "74.900000","USD", "74.900000",null,"K520-TF002C-SC-1AB",2,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,                             "UNS-K2-26","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 4,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "USD","-10.562020","USD","-10.560000",null,"K486-HLP",          1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES, "UNS-K2-23","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 5,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "USD","-12.654014","USD","-12.650000",null,"K520-TF002C-SC-1AR",1,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES, "UNS-K2-26","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 6,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "USD","-25.308028","USD","-25.310000",null,"K520-TF002C-SC-1AB",2,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES, "UNS-K2-26","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 7,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP", "-8.160000","USD","-10.690000",null,"K486-KNB",          8,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,            "UNS-K2-23","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 8,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP", "-3.970000","USD", "-5.200000",null,"K486-HLB",          5,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,            "UNS-K2-23","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId, 9,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP", "-3.970000","USD", "-5.200000",null,"K486-HLK",          5,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,            "UNS-K2-23","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,10,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP", "-3.970000","USD", "-5.200000",null,"K486-HLP",          5,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,            "UNS-K2-23","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,11,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP", "-8.160000","USD","-10.690000",null,"K486-SPB",          8,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,            "UNS-K2-23","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,12,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP", "-5.810000","USD", "-7.610000",null,"K486-SPR",          8,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,            "UNS-K2-23","UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,13,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP","-17.880000","USD","-23.420000",null,             null,null,TransactionLineType.INVENTORY_PLACEMENT_PROGRAM_COST,            "UNS-K2-26", "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,14,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP","-13.090000","USD","-17.150000",null,             null,null,TransactionLineType.FBA_REMOVAL_FEE,                             "UNS-K2-26", "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,15,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP","-13.110000","USD","-17.170000",null,             null,null,TransactionLineType.FBA_LONG_TERM_STORAGE,                       "UNS-K2-26", "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,16,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP", "75.110000","USD", "98.380000",null,             null,null,TransactionLineType.PRODUCT_RELATED_ADDITION,                    "UNS-K2-23", "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,17,StatementLineType.MS2SS_PURCHASE_ALLOWANCE,            "GBP", "13.010000","USD", "17.040000",null,             null,null,TransactionLineType.MARKETING_ACTIVITY_EXPENSE_REFUND,           "UNS-K2-23", "UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newstmntId,18,StatementLineType.MS2SS_SSDC_PAYMENT_ON_BEHALF_OF_MSDC,"GBP", "34.040000","USD", "44.590000",null,             null,null,TransactionLineType.MS2SS_SSDC_PAYMENT_ONBEHALF_OF_MSDC_FOR_IMPORT_DUTY,null, "TW"));

		BillStatement result = this.uco.getExistingDraftBillStatement(newstmntId);
		assertEquals(expected,result);
	}
	
	private Date toDate(String dateStr) {
		return DateHelper.toDate(dateStr, "yyyy-MM-dd HH:mm:ss Z");
	}
}
