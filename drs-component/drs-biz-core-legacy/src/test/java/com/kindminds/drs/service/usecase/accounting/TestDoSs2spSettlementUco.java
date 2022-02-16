package com.kindminds.drs.service.usecase.accounting;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.kindminds.drs.service.security.MockAuth;
import com.kindminds.drs.impl.CustomerCareCaseDtoImplForTest;
import com.kindminds.drs.impl.CustomerCareCaseIssueImpl;
import com.kindminds.drs.impl.CustomerCareCaseMessageImplForTest;
import com.kindminds.drs.impl.DomesticTransactionImpl;
import com.kindminds.drs.impl.DomesticTransactionLineItemImpl;
import com.kindminds.drs.impl.DrsTransactionImpl;
import com.kindminds.drs.impl.DrsTransactionLineItemImpl;
import com.kindminds.drs.impl.InternationalTransactionImplForTest;
import com.kindminds.drs.impl.InternationalTransactionLineItemImplForTest;
import com.kindminds.drs.impl.ProductBaseImplForTest;
import com.kindminds.drs.impl.ProductMarketplaceInfoImpl;
import com.kindminds.drs.impl.ProductSkuImplForTest;
import com.kindminds.drs.impl.RemittanceImpl;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.Currency;
import com.kindminds.drs.DrsConstants;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.usecase.accounting.DoSs2spSettlementUco;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueUco;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseUco;
import com.kindminds.drs.api.usecase.accounting.MaintainDomesticTransactionUco;
import com.kindminds.drs.api.usecase.accounting.MaintainInternationalTransactionUco;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.usecase.product.MaintainProductMarketplaceInfoUco;
import com.kindminds.drs.api.usecase.MaintainRemittanceUco;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage.MessageType;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionType;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo.ProductMarketStatus;
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.v1.model.impl.close.BillStatementImpl;
import com.kindminds.drs.v1.model.impl.close.BsliCustomerCaseExemptionInfoImpl;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.v1.model.impl.close.BillStatementLineItemImpl;
import com.kindminds.drs.v1.model.impl.close.BillStatementProfitShareItemImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestDoSs2spSettlementUco {
	
	@Autowired private DoSs2spSettlementUco uco;
	@Autowired private MaintainRemittanceUco rmtUco;
	@Autowired private MaintainProductBaseUco productBaseUco;
	@Autowired private MaintainProductSkuUco productSkuUco;
	@Autowired private MaintainProductMarketplaceInfoUco productUco;
	@Autowired private CurrencyDao currencyRepo;
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private MaintainCustomerCareCaseUco cccUco;
	@Autowired private MaintainCustomerCareCaseIssueUco issueUco;
	@Autowired private DrsTransactionDao dtRepo;
	@Autowired private MaintainInternationalTransactionUco internationalTransactionUco;
	@Autowired private MaintainDomesticTransactionUco domesticTransactionUco;
	
	private void prepareCurrencyExchangeRates(String srcCurrencyStr, String dstCurrencyStr, String dateStr,String rateStr){

		this.currencyRepo.insertExchangeRate(Currency.valueOf(srcCurrencyStr),
				Currency.valueOf(dstCurrencyStr), this.toDate(dateStr).toInstant().atOffset( ZoneOffset.UTC ),
				new BigDecimal(rateStr),DrsConstants.interbankRateForSs2spSettlement);
	}
	
	private void prepareCurrencyExchangeRates(String srcCurrencyStr, String dstCurrencyStr, String dateStr,String rateStr,BigDecimal interbankRate){
		this.currencyRepo.insertExchangeRate(Currency.valueOf(srcCurrencyStr), Currency.valueOf(dstCurrencyStr),
				this.toDate(dateStr).toInstant().atOffset( ZoneOffset.UTC ),new BigDecimal(rateStr),interbankRate);
	}
	
	private void prepareTestProductBase(String supplierKcode,String baseCode){
		this.productBaseUco.insert(new ProductBaseImplForTest(supplierKcode,null,baseCode,null,"supplier name","", null));
	}
	
	private void prepareTestProductSku(String supplierKcode,String drsBaseCode,String skuCode){
		this.productSkuUco.insert(new ProductSkuImplForTest(supplierKcode, drsBaseCode, skuCode, null, "supplier name", "drs name", "7777777","DRS", Status.SKU_ACTIVE, "7", false, "NOTE FOR TEST"));
	}
	
	@Test(expected=IllegalArgumentException.class) @Transactional("transactionManager")
	public void testDeleteOldOfficialFail(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.uco.deleteOfficial("STM-K486-8");
		MockAuth.logout();
	}
	
	@Test(expected=IllegalArgumentException.class) @Transactional("transactionManager")
	public void testDeleteNewestBySupplierFail(){
		MockAuth.login(authenticationManager, "K448.test", "12345");
		this.uco.deleteOfficial("STM-K486-8");
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testUkOrderRefundBasicWithUs(){
		String targetSku = "K448-TEST-SKU1";
		this.prepareCurrencyExchangeRates("USD","TWD","2015-12-01 00:00:00 +0000","32.77");
		this.prepareCurrencyExchangeRates("GBP","TWD","2015-12-01 00:00:00 +0000","48.77");
		this.prepareTestProductBase("K448","1");
		this.prepareTestProductSku("K448", "BP-K448-1", "TEST-SKU1");
		this.productUco.insert(new ProductMarketplaceInfoImpl(targetSku,null,Marketplace.AMAZON_COM,  targetSku,"USD", ProductMarketStatus.REGION_LIVE.name(),"36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50"));
		this.productUco.insert(new ProductMarketplaceInfoImpl(targetSku,null,Marketplace.AMAZON_CO_UK,targetSku,"GBP", ProductMarketStatus.REGION_LIVE.name(),"30.00","30.60","30.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50"));
		
		String ivsNameUs = "test-ivs-name-us";
		String ivsNameUk = "test-ivs-name-uk";
		String unsNameUs = "test-uns-name-us";
		String unsNameUk = "test-uns-name-uk";
		String orderIdUs = "113-7907694-8005827";
		String orderIdUk = "203-3748086-4567517";
		
		DrsTransactionImpl orderUs = new DrsTransactionImpl(AmazonTransactionType.ORDER.getValue(),"2015-12-01 00:09:26 +0000",1,"K448-TEST-SKU1",1,"Amazon.com",orderIdUs,ivsNameUs,unsNameUs,new ArrayList<DrsTransactionLineItem>());
		orderUs.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                           "USD", "20.130000"));
		orderUs.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		orderUs.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "6.524995"));
		orderUs.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.967925"));
		this.dtRepo.insert(orderUs);
		
		DrsTransactionImpl orderUk = new DrsTransactionImpl(AmazonTransactionType.ORDER.getValue(),"2015-12-01 00:09:26 +0000",1,"K448-TEST-SKU1",1,"Amazon.co.uk",orderIdUk,ivsNameUk,unsNameUk,new ArrayList<DrsTransactionLineItem>());
		orderUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K4",  "K2","MS2SS_UNIT_DDP_PAYMENT",                           "USD", "20.130000"));
		orderUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		orderUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "GBP",  "2.267919"));
		orderUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K4",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-7.134900"));
		this.dtRepo.insert(orderUk);
		
		DrsTransactionImpl refndUk = new DrsTransactionImpl(AmazonTransactionType.REFUND.getValue(),"2015-12-05 04:56:30 +0000",1,"K448-TEST-SKU1",1,"Amazon.co.uk",orderIdUk,ivsNameUk,unsNameUk,new ArrayList<DrsTransactionLineItem>());
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K4","K2",  TransactionLineType.MS2SS_UNIT_DDP_REFUND.getName(),                                 "USD", "-20.130000"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448",TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND.getName(),                     "TWD","-257.250000"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION.getName(),             "GBP",  "-2.267919"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K4","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND.getName(),    "USD",   "7.134900"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(5,"K2","K448",TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE.getName(),  "GBP",  "-2.700000"));
		refndUk.getSettleableItemList().add(new DrsTransactionLineItemImpl(6,"K4","K2",  TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE.getName(),"USD",  "-3.767850"));
		this.dtRepo.insert(refndUk);
		
		Date stmntStart = this.toDate("2015-12-01 00:00:00 +0000");
		Date stmntEnd = this.toDate("2015-12-10 00:00:00 +0000");
		int newStmntId = uco.doSettlementForOfficial("K448", stmntStart,stmntEnd);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K448","TWD","343.00","0.00","0.00","0.00","343.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD", "6.520000","TWD", "214.000000","32.770000"));
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,2,"UK","GBP","-2.700000","TWD","-132.000000","48.770000"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "6.524995", null,         null,null,"K448-TEST-SKU1",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "2.267919", null,         null,null,"K448-TEST-SKU1",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",  "-2.267919", null,         null,null,"K448-TEST-SKU1",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION,           null,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",  "-2.700000", null,         null,null,"K448-TEST-SKU1",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE,null,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,5,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "257.250000","TWD", "257.000000",null,"K448-TEST-SKU1",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,             ivsNameUs,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,6,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "257.250000","TWD", "257.000000",null,"K448-TEST-SKU1",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,             ivsNameUk,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,7,StatementLineType.SS2SP_PRODUCT_INVENTORY_REFUND, "TWD","-257.250000","TWD","-257.000000",null,"K448-TEST-SKU1",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND,              ivsNameUk,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,         null,"TWD",   "4.000000",null,     null,null,TransactionLineType.SSI_VAT,                                       null,"TW"));
		assertEquals(expected,this.uco.getStatement(BillStatementType.OFFICIAL,newStmntId));
	}
		
	@Test @Transactional
	public void testAllInOne(){ // import duty transaction has been ignored, need add again some day.
		this.prepareCurrencyExchangeRates("USD","TWD","2013-08-30 08:00:00 +0800","31.105167");
		
		DomesticTransactionImpl dt = new DomesticTransactionImpl(null,"2013-08-02","K2","KindMinds","K488","Roland","inv","5","TWD","888","44","932",new ArrayList<>());
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","111"));
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","777"));
		Integer dtId = this.domesticTransactionUco.createForSettlementTest(dt);
		
		InternationalTransactionImplForTest testTransactionUS = new InternationalTransactionImplForTest(null,"2013-08-01",0,"K3",null,"K2",null,"K488",null,"USD","75.11","",new ArrayList<>());
		testTransactionUS.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","75.11"));
		testTransactionUS.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","13.01"));
		this.internationalTransactionUco.createForSettlementTest(testTransactionUS);
		
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K488-PLUG100",1,  "Amazon.com", "108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                 "USD",  "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K488","SS2SP_Unit_Inventory_Payment",               "TWD", "257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K488","SS2SP_UnitProfitShareAddition",              "USD",   "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales", "USD",  "-1.216250"));
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K488-PLUG100",1,  "Amazon.com", "103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                 "USD",  "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K488","SS2SP_Unit_Inventory_Payment",               "TWD", "257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K488","SS2SP_UnitProfitShareAddition",              "USD",   "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales", "USD",  "-1.216250"));
		DrsTransactionImpl dtRefund = new DrsTransactionImpl("RETAIL_REFUND","2013-08-06 17:08:28 +0000",1,"K488-PLUG100",1, "Amazon.com", "103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_REFUND",            "USD", "-20.130000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K488","SS2SP_Unit_Inventory_Refund",                   "TWD","-257.250000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K488","SS2SP_UnitProfitShareSubtraction",              "USD", "-13.110995"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailRefund",   "USD",  "-3.013750"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(5,"K2","K488","SS2SP_UnitProfitShareSubtractionRefundFee",     "USD",  "-4.230000"));
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(6,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailRefundFee","USD",  "-4.230000"));
		this.dtRepo.insert(dtOrder1);
		this.dtRepo.insert(dtOrder2);
		this.dtRepo.insert(dtRefund);
		
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-09-01 00:00:00 +0000");
		int id = this.uco.doSettlement("K488",stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(id,"",stmntStart,stmntEnd,"K2","K488","TWD","-3538.00","0.00","0.00","0.00","-3538.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(id,1,"US","USD","-87.700000","TWD","-2728.000000","31.105167"));
		expected.addLineItem(new BillStatementLineItemImpl(id,1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "17.761990", null,         null,null,"K488-PLUG100",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(id,2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-13.110995", null,         null,null,"K488-PLUG100",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION,           null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(id,3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "-4.230000", null,         null,null,"K488-PLUG100",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(id,4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-13.010000", null,         null,null,          null,null,TransactionLineType.FBA_REMOVAL_FEE,                               null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(id,5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-75.110000", null,         null,null,          null,null,TransactionLineType.FBA_LONG_TERM_STORAGE,                         null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(id,6,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "514.500000","TWD", "515.000000",null,"K488-PLUG100",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,               ivsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(id,7,StatementLineType.SS2SP_PRODUCT_INVENTORY_REFUND, "TWD","-257.250000","TWD","-257.000000",null,"K488-PLUG100",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND,                ivsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(id,8,StatementLineType.SS_SERVICE_SALE_FOR_SP,         "TWD","-932.000000","TWD","-932.000000",null,     null,null,null,                                                        dtId.toString(),"TW"));
		expected.addLineItem(new BillStatementLineItemImpl(id,9,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,         null,"TWD","-136.000000",null,     null,null,TransactionLineType.SSI_VAT,                                           null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,id);
		assertEquals(expected,result);
	}

	@Test @Transactional
	public void testSettleDrsTransactionOrderTwoItem(){
		this.prepareCurrencyExchangeRates("USD","TWD","2013-08-30 08:00:00 +0800","31.105167");
		this.prepareTestProductBase("K448", "1");
		this.prepareTestProductSku("K448", "BP-K448-1", "TEST-SKU1");
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder1);
		
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder2);
		
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd =   this.toDate("2013-09-01 00:00:00 +0000");
		int newStmntId = uco.doSettlementForOfficial("K448", stmntStart,stmntEnd);
		BillStatementImpl expected = new BillStatementImpl( newStmntId, "", stmntStart, stmntEnd, "K2","K448","TWD", "1095.00", "0.00", "0.00", "0.00", "1095.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD","17.760000","TWD","552.000000","31.105167"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 1, StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  "USD",  "17.761990",  null,         null,null,"K448-TEST-SKU1",    2, TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 2, StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT, "TWD", "514.500000", "TWD", "515.000000",null,"K448-TEST-SKU1",    2, TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT, ivsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 3, StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,   null,         null, "TWD",  "28.000000",null,     null, null, TransactionLineType.SSI_VAT,                         null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.OFFICIAL,newStmntId);
		assertEquals(expected,result);
	}
		
	@Test @Transactional
	public void testSettleDrsTransactionOrderTwoItemsRefundOneItem(){
		this.prepareCurrencyExchangeRates("USD","TWD","2013-08-30 08:00:00 +0800","31.105167");
		this.prepareTestProductBase("K448", "1");
		this.prepareTestProductSku("K448", "BP-K448-1", "TEST-SKU1");
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K448-TEST-SKU1",1,  "Amazon.com", "108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                 "USD",  "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",               "TWD", "257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",              "USD",   "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales", "USD",  "-1.216250"));
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K448-TEST-SKU1",1,  "Amazon.com", "103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_PAYMENT",                 "USD",  "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",               "TWD", "257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",              "USD",   "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3","K2",  "MS2SS_UnitPurchaseAllowanceWithRetailSales", "USD",  "-1.216250"));
		DrsTransactionImpl dtRefund = new DrsTransactionImpl("RETAIL_REFUND","2013-08-06 17:08:28 +0000",1,"K448-TEST-SKU1",1, "Amazon.com", "103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtRefund.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3","K2",  "MS2SS_UNIT_DDP_REFUND",            "USD", "-20.130000"));
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
		int newStmntId = uco.doSettlementForOfficial("K448", stmntStart,stmntEnd);
		BillStatementImpl expected = new BillStatementImpl( newStmntId, "", stmntStart, stmntEnd, "K2","K448","TWD", "272.00", "0.00", "0.00", "0.00", "272.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD","0.420000","TWD","13.000000","31.105167"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 1, StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  "USD",  "17.761990", null,          null,null,"K448-TEST-SKU1",   2, TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 2, StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  "USD", "-13.110995", null,          null,null,"K448-TEST-SKU1",   1, TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION,           null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 3, StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  "USD",  "-4.230000", null,          null,null,"K448-TEST-SKU1",   1, TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 4, StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT, "TWD", "514.500000","TWD",  "515.000000",null,"K448-TEST-SKU1",   2, TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,               ivsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 5, StatementLineType.SS2SP_PRODUCT_INVENTORY_REFUND,  "TWD","-257.250000","TWD", "-257.000000",null,"K448-TEST-SKU1",   1, TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND,                ivsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 6, StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,   null,         null,"TWD",    "1.000000",null,     null,null, TransactionLineType.SSI_VAT,                                       null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.OFFICIAL,newStmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testInternationalTransaction(){
		InternationalTransactionImplForTest testTransactionUS = new InternationalTransactionImplForTest(null,"2013-11-17",0,"K3",null,"K2",null,"K486",null,"USD","75.11","",new ArrayList<>());
		testTransactionUS.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","75.11"));
		testTransactionUS.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","13.01"));
		InternationalTransactionImplForTest testTransactionUK = new InternationalTransactionImplForTest(null,"2013-11-18",0,"K4",null,"K2",null,"K486",null,"USD","37.06","",new ArrayList<>());
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","17.00"));
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","20.06"));
		this.internationalTransactionUco.createForSettlementTest(testTransactionUS);
		this.internationalTransactionUco.createForSettlementTest(testTransactionUK);
		this.prepareCurrencyExchangeRates("USD","TWD","2013-11-17 08:00:00 +0800","31.105167");
		this.prepareCurrencyExchangeRates("GBP","TWD","2013-11-18 08:00:00 +0800","40.385200");
		Date stmntStart = this.toDate("2013-11-13 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-11-27 00:00:00 +0000");
		int newStmntId = this.uco.doSettlement("K486",stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K486","TWD","-4450.00","0.00","0.00","0.00","-4450.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD","-88.120000","TWD","-2741.000000","31.105167"));
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,2,"UK","GBP","-37.060000","TWD","-1497.000000","40.385200"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"USD","-13.010000", null,         null,null,null,null,TransactionLineType.FBA_REMOVAL_FEE,      null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"USD","-75.110000", null,         null,null,null,null,TransactionLineType.FBA_LONG_TERM_STORAGE,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"GBP","-20.060000", null,         null,null,null,null,TransactionLineType.FBA_REMOVAL_FEE,      null,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"GBP","-17.000000", null,         null,null,null,null,TransactionLineType.FBA_LONG_TERM_STORAGE,null,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, null,        null,"TWD","-212.000000",null,null,null,TransactionLineType.SSI_VAT,              null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,newStmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional @Ignore("not yet updated")
	public void testInternationalTransactionMarketingActivity(){
		Date stmntStart = this.toDate("2016-04-03 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-04-17 00:00:00 +0000");
		int newStmntId = this.uco.doSettlement("K488",stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K488","TWD","-11104.00","75205.00","75205.00","0.00","-11104.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD","-1126.020000","TWD","-36075.000000","32.038000"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "321.529500", null,          null,           null,"K488-PLUG100",  24,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-159.743500", null,          null,           null,"K488-PLUG100",   7,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION,           null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "-31.270000", null,          null,           null,"K488-PLUG100",   7,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD","-1250.000000", null,          null,           null,          null,null,TransactionLineType.MARKET_SIDE_MARKETING_ACTIVITY,                null,"US"));
//		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "-9.150000", null,          null,"BP-K488-PLUG1",          null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,             null,"US"));
//		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "2.613600", null,          null,"BP-K488-PLUG1",          null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE_EXEMPTION,   null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 7,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1575.000000","TWD", "1575.000000",           null,"K488-PLUG100",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,          "IVS-K488-1","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 8,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD","36225.000000","TWD","36225.000000",           null,"K488-PLUG100",  23,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,          "IVS-K488-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 9,StatementLineType.SS2SP_PRODUCT_INVENTORY_REFUND, "TWD","-4725.000000","TWD","-4725.000000",           null,"K488-PLUG100",   3,TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND,           "IVS-K488-1","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,10,StatementLineType.SS2SP_PRODUCT_INVENTORY_REFUND, "TWD","-6300.000000","TWD","-6300.000000",           null,"K488-PLUG100",   4,TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND,           "IVS-K488-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,11,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,          null,"TWD","-1804.000000",           null,          null,null,TransactionLineType.SSI_VAT,                                       null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,newStmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testInternationalTransactionMs2sp(){
		InternationalTransactionImplForTest testTransactionUS = new InternationalTransactionImplForTest(null,"2013-11-17",CashFlowDirection.MS2SP.getKey(),"K3",null,"K2",null,"K486",null,"USD","75.11","",new ArrayList<>());
		testTransactionUS.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,66,"MS2SS_PurchaseAllowanceForProductRelatedAddition","","75.11"));
		testTransactionUS.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,79,"MS2SS_PurchaseAllowanceForMarketingActivityExpenseRefund","","13.01"));
		InternationalTransactionImplForTest testTransactionUK = new InternationalTransactionImplForTest(null,"2013-11-18",CashFlowDirection.SP2MS.getKey(),"K4",null,"K2",null,"K486",null,"USD","37.06","",new ArrayList<>());
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE","","17.00"));
		testTransactionUK.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE",      "","20.06"));
		this.internationalTransactionUco.createForSettlementTest(testTransactionUS);
		this.internationalTransactionUco.createForSettlementTest(testTransactionUK);
		this.prepareCurrencyExchangeRates("USD","TWD","2013-11-17 08:00:00 +0800","31.105167");
		this.prepareCurrencyExchangeRates("GBP","TWD","2013-11-18 08:00:00 +0800","40.385200");
		Date stmntStart = this.toDate("2013-11-13 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-11-27 00:00:00 +0000");
		int newStmntId = this.uco.doSettlement("K486",stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K486","TWD","1306.00","0.00","0.00","0.00","1306.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD", "88.120000","TWD", "2741.000000","31.105167"));
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,2,"UK","GBP","-37.060000","TWD","-1497.000000","40.385200"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"USD", "75.110000", null,       null,null,null,null,TransactionLineType.PRODUCT_RELATED_ADDITION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"USD", "13.010000", null,       null,null,null,null,TransactionLineType.MARKETING_ACTIVITY_EXPENSE_REFUND,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"GBP","-20.060000", null,       null,null,null,null,TransactionLineType.FBA_REMOVAL_FEE,      null,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"GBP","-17.000000", null,       null,null,null,null,TransactionLineType.FBA_LONG_TERM_STORAGE,null,"UK"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, null,        null,"TWD","62.000000",null,null,null,TransactionLineType.SSI_VAT,              null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,newStmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testInternationalTransactionInventoryPlacementProgramCosts(){
		Date stmntStart = this.toDate("2016-12-25 00:00:00 +0000");
		Date stmntEnd = this.toDate("2017-01-08 00:00:00 +0000");
		int newStmntId = this.uco.doSettlement("K506",stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K506","TWD","-13067.00","-16152.00","0.00","0.00","-29219.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD","-373.430000","TWD","-11845.000000","31.720200"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"USD","-332.800000", null,         null,null,null,null,TransactionLineType.INVENTORY_PLACEMENT_PROGRAM_COST,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,"USD", "-40.630000", null,         null,null,null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,3,StatementLineType.SS_SERVICE_SALE_FOR_SP,        "TWD","-630.000000","TWD","-630.000000",null,null,null,null,"103","TW"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, null,         null,"TWD","-592.000000",null,null,null,TransactionLineType.SSI_VAT,                         null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,newStmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testDomesticTransaction1(){
		DomesticTransactionImpl dt = new DomesticTransactionImpl(null,"2013-11-13","K2","KindMinds","K486","Roland","inv","5","TWD","888","44","932",new ArrayList<>());
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","111"));
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","777"));
		Integer dtId = this.domesticTransactionUco.createForSettlementTest(dt);
		Date stmntStart = this.toDate("2013-11-13 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-11-27 00:00:00 +0000");
		int newStmntId = this.uco.doSettlement("K486",stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K486","TWD","-932.00","0.00","0.00","0.00","-932.00");
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS_SERVICE_SALE_FOR_SP,"TWD","-932.000000","TWD","-932.000000",null,null,null,null,dtId.toString(),"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,newStmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testDomesticTransaction2(){
		DomesticTransactionImpl dt = new DomesticTransactionImpl(null,"2013-07-09","K2","KindMinds","K488",null,"INV0001","5","TWD","4020","201","4221",new ArrayList<>());
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","1080"));
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","2940"));
		Integer dtId = this.domesticTransactionUco.createForSettlementTest(dt);
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-09-01 00:00:00 +0000");
		int newStmntId = uco.doSettlement("K488", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K488","TWD","-4221.00","0.00","0.00","0.00","-4221.00");
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 1, StatementLineType.SS_SERVICE_SALE_FOR_SP,"TWD","-4221.000000","TWD","-4221.000000",null,null,null,null,dtId.toString(),"TW"));
		assertEquals(expected,this.uco.getStatement(BillStatementType.DRAFT,newStmntId));
	}
	
	@Test @Transactional
	public void testDomesticTransaction_X2(){
		DomesticTransactionImpl dt1 = new DomesticTransactionImpl(null,"2013-07-09","K2","KindMinds","K488",null,"INV0001","5","TWD","4020","201","4221",new ArrayList<>());
		dt1.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","1080"));
		dt1.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","2940"));

		DomesticTransactionImpl dt2 = new DomesticTransactionImpl(null,"2013-07-20","K2","KindMinds","K488",null,"INV0002","5","TWD","480","24","504",new ArrayList<>());
		dt2.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","320"));
		dt2.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","160"));
		
		Integer dtId1 = this.domesticTransactionUco.createForSettlementTest(dt1);
		Integer dtId2 = this.domesticTransactionUco.createForSettlementTest(dt2);
		
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-09-01 00:00:00 +0000");
		int newStmntId = uco.doSettlement("K488", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K488","TWD","-4725.00","0.00","0.00","0.00","-4725.00");
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS_SERVICE_SALE_FOR_SP,"TWD","-4221.000000","TWD","-4221.000000",null,null,null,null,dtId1.toString(),"TW"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,2,StatementLineType.SS_SERVICE_SALE_FOR_SP,"TWD", "-504.000000","TWD", "-504.000000",null,null,null,null,dtId2.toString(),"TW"));
		assertEquals(expected,this.uco.getStatement(BillStatementType.DRAFT,newStmntId));
	}
	
	@Test @Transactional
	public void testSettleImportDuty(){
		Date stmntStart = this.toDate("2016-04-03 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-04-17 00:00:00 +0000");
		int id = this.uco.doSettlement("K486",stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expect = new BillStatementImpl(id,"",stmntStart,stmntEnd,"K2","K486","TWD","2904.00","65.00","0.00","0.00","2969.00");
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(id,1,"US","USD","-107.040000","TWD","-3429.000000","32.038000"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "2.066500", null,          null,null,"K486-KNB",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "1.748350", null,          null,null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "3.239732", null,          null,null,"K486-HLK",   3,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "2.039876", null,          null,null,"K486-HLP",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "3.697464", null,          null,null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-27.540000", null,          null,null,"K486-KNB",  20,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 7,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-27.540000", null,          null,null,"K486-KNK",  20,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-13.770000", null,          null,null,"K486-KNP",  10,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id, 9,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-15.200000", null,          null,null,"K486-HLK",  10,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id,10,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "-33.550000", null,          null,null,"K486-BAL",  20,TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id,11,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "-2.230000", null,          null,null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(id,12,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "728.700000","TWD",  "729.000000",null,"K486-HLP",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-2","US"));
		expect.addLineItem(new BillStatementLineItemImpl(id,13,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD","2460.150000","TWD", "2460.000000",null,"K486-HLK",   3,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-4","US"));
		expect.addLineItem(new BillStatementLineItemImpl(id,14,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD","1770.300000","TWD", "1770.000000",null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-4","US"));
		expect.addLineItem(new BillStatementLineItemImpl(id,15,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "724.500000","TWD",  "725.000000",null,"K486-KNB",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-5","US"));
		expect.addLineItem(new BillStatementLineItemImpl(id,16,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "820.050000","TWD",  "820.000000",null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-5","US"));
		expect.addLineItem(new BillStatementLineItemImpl(id,17,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,         null,"TWD", "-171.000000",null,      null,null, TransactionLineType.SSI_VAT,                         null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,id);
		assertEquals(expect,result);
	}
	
	@Test @Transactional @Ignore("not yet complete")
	public void testProcessFbaReturnToSupplier(){
		this.prepareCurrencyExchangeRates("USD","TWD","2016-04-29 00:00:00 +0000","31.105167");
		DrsTransactionImpl returnTransaction = new DrsTransactionImpl(DrsTransactionType.FBA_RETURN_TO_SUPPLIER.getTextValue(),"2016-04-26 00:00:00 +0000",1,"K488-PLUG100",1,"Amazon.com","113-7696285-0149817","IVS-K488-1","UNS-K2-5",new ArrayList<DrsTransactionLineItem>());
		returnTransaction.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K2","K488","SS2SP_Unit_Inventory_Payment",  "TWD", "1575.000000"));
		returnTransaction.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K488","SS2SP_Unit_Inventory_Sell_Back","TWD","-1575.000000"));
		this.dtRepo.insert(returnTransaction);
		
		Date stmntStart = this.toDate("2016-04-17 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-04-30 00:00:00 +0000");
		int newStmntId = this.uco.doSettlement("K488",stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl( newStmntId,"", stmntStart,stmntEnd,"K2","K488", "TWD", "-11104.00", "", "0.00", "0.00", "-11104.00");
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,  "TWD", "1575.000000","TWD", "1575.000000",null,"K448-1A",1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,  "IVS-K488-1","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,2,StatementLineType.SS2SP_PRODUCT_INVENTORY_SELL_BACK,"TWD","-1575.000000","TWD","-1575.000000",null,"K448-1A",1,TransactionLineType.SS2SP_UNIT_INVENTORY_SELL_BACK,"IVS-K488-1","US"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,newStmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional @Ignore
	public void testCustomerCareCaseEs(){
		List<Integer> issueIds = this.getIssueIdsForK486Test();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		List<String> relatedSkuList = new ArrayList<String>(Arrays.asList("K486-KNB","K486-SPB"));
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K9",Marketplace.AMAZON_ES,"222-22222-22222","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2015-12-01 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.cccUco.save(caseWithSkuToSave1);
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","3","1",null,"K486-KNB",null,"2","This is content for test2",false));
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","6","1",null,"K486-SPB",null,"2","This is content for test2",false));
		MockAuth.logout();
		this.prepareCurrencyExchangeRates("USD","EUR","2016-09-30 06:00:00 +0800","0.89736",DrsConstants.interbankRateForCalculatingCustomerCaseMsdcCharge);
		this.prepareCurrencyExchangeRates("EUR","TWD","2016-09-30 06:00:00 +0800","35.0217",DrsConstants.interbankRateForSs2spSettlement);
		Date stmntStart = this.toDate("2016-09-18 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-10-02 00:00:00 +0000");
		int sId = this.uco.doSettlement("K486", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expect = new BillStatementImpl(sId,"",stmntStart,stmntEnd,"K2","K486","TWD","-1456.00","4255.00","0.00","0.00","2799.00");
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,1,"US","USD","-23.080000","TWD","-717.000000","31.051400"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,2,"UK","GBP", "-1.140000","TWD", "-46.000000","40.385200"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,3,"CA","CAD", "-0.020000","TWD",   "0.000000","23.713900"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,4,"ES","EUR", "-3.080000","TWD","-108.000000","35.021700"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.748350", null,          null,        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.256760", null,          null,        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "3.780280", null,          null,        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));		
		expect.addLineItem(new BillStatementLineItemImpl(sId, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "-29.870000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "-1.140000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "CAD",   "-0.020000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"CA"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 7,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "EUR",   "-1.444750", null,          null,"BP-K486-KN",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"ES"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "EUR",   "-1.633195", null,          null,"BP-K486-SP",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"ES"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 9,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",  "820.050000","TWD",  "820.000000",        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-5","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,10,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1640.100000","TWD", "1640.000000",        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,11,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1770.300000","TWD", "1770.000000",        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,12,StatementLineType.SS_SERVICE_SALE_FOR_SP,         "TWD","-4771.000000","TWD","-4771.000000",        null,      null,null,null,                                                    "59","TW"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,13,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,          null,"TWD",  "-44.000000",        null,      null,null,TransactionLineType.SSI_VAT,                             null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,sId);
		assertEquals(expect,result);
		Assert.assertEquals(0,this.uco.getCustomerCaseInfoList(BillStatementType.DRAFT,sId).size());
	}
	
	@Test @Transactional @Ignore
	public void testCustomerCareCaseFr(){
		List<Integer> issueIds = this.getIssueIdsForK486Test();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		List<String> relatedSkuList = new ArrayList<String>(Arrays.asList("K486-KNB","K486-SPB"));
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K7",Marketplace.AMAZON_FR,"222-22222-22222","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2015-12-01 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.cccUco.save(caseWithSkuToSave1);
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","3","1",null,"K486-KNB",null,"2","This is content for test2",false));
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","6","1",null,"K486-SPB",null,"2","This is content for test2",false));
		MockAuth.logout();
		this.prepareCurrencyExchangeRates("USD","EUR","2016-09-30 06:00:00 +0800","0.89736",DrsConstants.interbankRateForCalculatingCustomerCaseMsdcCharge);
		this.prepareCurrencyExchangeRates("EUR","TWD","2016-09-30 06:00:00 +0800","35.0217",DrsConstants.interbankRateForSs2spSettlement);
		Date stmntStart = this.toDate("2016-09-18 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-10-02 00:00:00 +0000");
		int sId = this.uco.doSettlement("K486", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expect = new BillStatementImpl(sId,"",stmntStart,stmntEnd,"K2","K486","TWD","-1456.00","4255.00","0.00","0.00","2799.00");
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,1,"US","USD","-23.080000","TWD","-717.000000","31.051400"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,2,"UK","GBP", "-1.140000","TWD", "-46.000000","40.385200"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,3,"CA","CAD", "-0.020000","TWD",   "0.000000","23.713900"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,4,"FR","EUR", "-3.080000","TWD","-108.000000","35.021700"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.748350", null,          null,        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.256760", null,          null,        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "3.780280", null,          null,        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));		
		expect.addLineItem(new BillStatementLineItemImpl(sId, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "-29.870000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "-1.140000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "CAD",   "-0.020000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"CA"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 7,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "EUR",   "-1.444750", null,          null,"BP-K486-KN",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"FR"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "EUR",   "-1.633195", null,          null,"BP-K486-SP",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"FR"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 9,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",  "820.050000","TWD",  "820.000000",        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-5","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,10,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1640.100000","TWD", "1640.000000",        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,11,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1770.300000","TWD", "1770.000000",        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,12,StatementLineType.SS_SERVICE_SALE_FOR_SP,         "TWD","-4771.000000","TWD","-4771.000000",        null,      null,null,null,                                                    "59","TW"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,13,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,          null,"TWD",  "-44.000000",        null,      null,null,TransactionLineType.SSI_VAT,                             null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,sId);
		assertEquals(expect,result);
		Assert.assertEquals(0,this.uco.getCustomerCaseInfoList(BillStatementType.DRAFT,sId).size());
	}
	
	@Test @Transactional @Ignore
	public void testCustomerCareCaseCa(){
		List<Integer> issueIds = this.getIssueIdsForK486Test();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		List<String> relatedSkuList = new ArrayList<String>(Arrays.asList("K486-KNB","K486-SPB"));
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K5",Marketplace.AMAZON_CA,"222-22222-22222","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2015-12-01 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.cccUco.save(caseWithSkuToSave1);
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","3","1",null,"K486-KNB",null,"2","This is content for test2",false));
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","6","1",null,"K486-SPB",null,"2","This is content for test2",false));
		MockAuth.logout();
		this.prepareCurrencyExchangeRates("USD","CAD","2016-09-30 06:00:00 +0800","1.33988",DrsConstants.interbankRateForCalculatingCustomerCaseMsdcCharge);
		Date stmntStart = this.toDate("2016-09-18 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-10-02 00:00:00 +0000");
		int sId = this.uco.doSettlement("K486", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expect = new BillStatementImpl(sId,"",stmntStart,stmntEnd,"K2","K486","TWD","-1458.00","4255.00","0.00","0.00","2797.00");
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,1,"US","USD","-23.080000","TWD","-717.000000","31.051400"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,2,"UK","GBP", "-1.140000","TWD", "-46.000000","40.385200"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,3,"CA","CAD", "-4.620000","TWD","-110.000000","23.713900"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.748350", null,          null,        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.256760", null,          null,        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "3.780280", null,          null,        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "-29.870000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "-1.140000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "CAD",   "-0.020000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"CA"));		
		expect.addLineItem(new BillStatementLineItemImpl(sId, 7,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "CAD",   "-2.157207", null,          null,"BP-K486-KN",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"CA"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "CAD",   "-2.438582", null,          null,"BP-K486-SP",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"CA"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 9,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",  "820.050000","TWD",  "820.000000",        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-5","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,10,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1640.100000","TWD", "1640.000000",        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,11,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1770.300000","TWD", "1770.000000",        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,12,StatementLineType.SS_SERVICE_SALE_FOR_SP,         "TWD","-4771.000000","TWD","-4771.000000",        null,      null,null,null,                                                    "59","TW"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,13,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,          null,"TWD",  "-44.000000",        null,      null,null,TransactionLineType.SSI_VAT,                             null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,sId);
		assertEquals(expect,result);
		Assert.assertEquals(0,this.uco.getCustomerCaseInfoList(BillStatementType.DRAFT,sId).size());
	}
	
	@Test @Transactional @Ignore
	public void testCustomerCareCaseIt(){
		List<Integer> issueIds = this.getIssueIdsForK486Test();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		List<String> relatedSkuList = new ArrayList<String>(Arrays.asList("K486-KNB","K486-SPB"));
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K8",Marketplace.AMAZON_IT,"222-22222-22222","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2015-12-01 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.cccUco.save(caseWithSkuToSave1);
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","3","1",null,"K486-KNB",null,"2","This is content for test2",false));
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","6","1",null,"K486-SPB",null,"2","This is content for test2",false));
		MockAuth.logout();
		this.prepareCurrencyExchangeRates("USD","EUR","2016-09-30 06:00:00 +0800","0.89736",DrsConstants.interbankRateForCalculatingCustomerCaseMsdcCharge);
		this.prepareCurrencyExchangeRates("EUR","TWD","2016-09-30 06:00:00 +0800","35.0217",DrsConstants.interbankRateForSs2spSettlement);
		Date stmntStart = this.toDate("2016-09-18 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-10-02 00:00:00 +0000");
		int sId = this.uco.doSettlement("K486", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expect = new BillStatementImpl(sId,"",stmntStart,stmntEnd,"K2","K486","TWD","-1456.00","4255.00","0.00","0.00","2799.00");
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,1,"US","USD","-23.080000","TWD","-717.000000","31.051400"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,2,"UK","GBP", "-1.140000","TWD", "-46.000000","40.385200"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,3,"CA","CAD", "-0.020000","TWD",   "0.000000","23.713900"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,4,"IT","EUR", "-3.080000","TWD","-108.000000","35.021700"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.748350", null,          null,        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.256760", null,          null,        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "3.780280", null,          null,        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));		
		expect.addLineItem(new BillStatementLineItemImpl(sId, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "-29.870000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,        null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "-1.140000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,        null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "CAD",   "-0.020000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,        null,"CA"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 7,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "EUR",   "-1.444750", null,          null,"BP-K486-KN",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"IT"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "EUR",   "-1.633195", null,          null,"BP-K486-SP",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"IT"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 9,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",  "820.050000","TWD",  "820.000000",        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-5","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,10,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1640.100000","TWD", "1640.000000",        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,11,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1770.300000","TWD", "1770.000000",        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,12,StatementLineType.SS_SERVICE_SALE_FOR_SP,         "TWD","-4771.000000","TWD","-4771.000000",        null,      null,null,null,                                                    "59","TW"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,13,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,          null,"TWD",  "-44.000000",        null,      null,null,TransactionLineType.SSI_VAT,                             null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,sId);
		assertEquals(expect,result);
		Assert.assertEquals(0,this.uco.getCustomerCaseInfoList(BillStatementType.DRAFT,sId).size());
	}
	
	@Test @Transactional @Ignore
	public void testCustomerCareCaseDe(){
		List<Integer> issueIds = this.getIssueIdsForK486Test();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		List<String> relatedSkuList = new ArrayList<String>(Arrays.asList("K486-KNB","K486-SPB"));
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K6",Marketplace.AMAZON_DE,"222-22222-22222","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2015-12-01 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.cccUco.save(caseWithSkuToSave1);
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","3","1",null,"K486-KNB",null,"2","This is content for test2",false));
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","6","1",null,"K486-SPB",null,"2","This is content for test2",false));
		MockAuth.logout();
		this.prepareCurrencyExchangeRates("USD","EUR","2016-09-30 06:00:00 +0800","0.89736",DrsConstants.interbankRateForCalculatingCustomerCaseMsdcCharge);
		this.prepareCurrencyExchangeRates("EUR","TWD","2016-09-30 06:00:00 +0800","35.0217",DrsConstants.interbankRateForSs2spSettlement);
		Date stmntStart = this.toDate("2016-09-18 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-10-02 00:00:00 +0000");
		int sId = this.uco.doSettlement("K486", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expect = new BillStatementImpl(sId,"",stmntStart,stmntEnd,"K2","K486","TWD","-1456.00","4255.00","0.00","0.00","2799.00");
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,1,"US","USD","-23.080000","TWD","-717.000000","31.051400"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,2,"UK","GBP", "-1.140000","TWD", "-46.000000","40.385200"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,3,"CA","CAD", "-0.020000","TWD",   "0.000000","23.713900"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,4,"DE","EUR", "-3.080000","TWD","-108.000000","35.021700"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.748350", null,          null,        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "1.256760", null,          null,        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "3.780280", null,          null,        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,    null,"US"));		
		expect.addLineItem(new BillStatementLineItemImpl(sId, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "-29.870000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "-1.140000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "CAD",   "-0.020000", null,          null,        null,      null,null,TransactionLineType.MARKET_SIDE_ADVERTISING_COST,    null,"CA"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 7,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "EUR",   "-1.444750", null,          null,"BP-K486-KN",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"DE"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "EUR",   "-1.633195", null,          null,"BP-K486-SP",      null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,   null,"DE"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 9,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",  "820.050000","TWD",  "820.000000",        null,"K486-HLB",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-5","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,10,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1640.100000","TWD", "1640.000000",        null,"K486-HLK",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,11,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1770.300000","TWD", "1770.000000",        null,"K486-BAL",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-6","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,12,StatementLineType.SS_SERVICE_SALE_FOR_SP,         "TWD","-4771.000000","TWD","-4771.000000",        null,      null,null,null,                                                    "59","TW"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,13,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,          null,"TWD",  "-44.000000",        null,      null,null,TransactionLineType.SSI_VAT,                             null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,sId);
		assertEquals(expect,result);
		Assert.assertEquals(0,this.uco.getCustomerCaseInfoList(BillStatementType.DRAFT,sId).size());
	}
	
	@Test @Transactional @Ignore
	public void testCustomerCareCaseUk(){
		List<Integer> issueIds = this.getIssueIdsForK486Test();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		List<String> relatedSkuList = new ArrayList<String>(Arrays.asList("K520-TF002C-SC-1AY","K520-TF002C-SC-1AR"));
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K4",Marketplace.AMAZON_CO_UK,"222-22222-22222","2014-11-17 11:00:00 +0000","K520","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2015-12-01 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.cccUco.save(caseWithSkuToSave1);
		this.cccUco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2016-09-28 10:00:00 +0000",false,"120","3","1",null,"K520-TF002C-SC-1AY",null,"2","This is content for test2",false));
		// Revenue
		// UK,BP-K520-TF002C-SC-1A,378.000000
		// US,BP-K520-TF002C-SC-1A,944.730000
		MockAuth.logout();
		this.currencyRepo.insertExchangeRate(Currency.USD, Currency.GBP,
				this.toDate("2016-09-30 06:00:00 +0800").toInstant().atOffset( ZoneOffset.UTC )
				,new BigDecimal("0.81717"),DrsConstants.interbankRateForCalculatingCustomerCaseMsdcCharge);
		Date stmntStart = this.toDate("2016-09-18 00:00:00 +0000");
		Date stmntEnd = this.toDate("2016-10-02 00:00:00 +0000");
		int sId = this.uco.doSettlement("K520", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expect = new BillStatementImpl(sId,"",stmntStart,stmntEnd,"K2","K520","TWD","28593.00","-4946.00","0.00","0.00","23647.00");
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,1,"US","USD","145.720000","TWD","4525.000000","31.051400"));
		expect.addProfitShareItem(new BillStatementProfitShareItemImpl(sId,2,"UK","GBP", "44.610000","TWD","1802.000000","40.385200"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "10.822450", null,          null, null, "K520-TF002C-SC-1AY",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",   "10.822450", null,          null, null, "K520-TF002C-SC-1AR",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",  "124.078175", null,          null, null, "K520-TF002C-SC-1AB",  23,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "13.500308", null,          null, null, "K520-TF002C-SC-1AR",   4,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "37.125847", null,          null, null, "K520-TF002C-SC-1AB",  11,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,              null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "-3.375077", null,          null, null, "K520-TF002C-SC-1AR",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION,           null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 7,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "-2.080000", null,          null, null, "K520-TF002C-SC-1AR",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE,null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",   "-1.315644", null,          null,"BP-K520-TF002C-SC-1A",null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,             null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId, 9,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "GBP",    "0.756000", null,          null,"BP-K520-TF002C-SC-1A",null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE_EXEMPTION,   null,"UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,10,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1070.685000","TWD", "1071.000000", null, "K520-TF002C-SC-1AY",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,          "IVS-K520-1","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,11,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "1070.685000","TWD", "1071.000000", null, "K520-TF002C-SC-1AR",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,          "IVS-K520-1","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,12,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD","12312.877500","TWD","12313.000000", null, "K520-TF002C-SC-1AB",  23,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,          "IVS-K520-1","US"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,13,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "2141.370000","TWD", "2141.000000", null, "K520-TF002C-SC-1AR",   4,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,          "IVS-K520-2","UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,14,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD", "5888.767500","TWD", "5889.000000", null, "K520-TF002C-SC-1AB",  11,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,          "IVS-K520-2","UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,15,StatementLineType.SS2SP_PRODUCT_INVENTORY_REFUND, "TWD", "-535.342500","TWD", "-535.000000", null, "K520-TF002C-SC-1AR",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND,           "IVS-K520-2","UK"));
		expect.addLineItem(new BillStatementLineItemImpl(sId,16,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,          null,"TWD",  "316.000000", null,                 null,null,TransactionLineType.SSI_VAT,                                       null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,sId);
		assertEquals(expect,result);
		List<BsliCustomerCaseExemptionInfoImpl> customerCaseFeeInfoList = new ArrayList<BsliCustomerCaseExemptionInfoImpl>();
		customerCaseFeeInfoList.add(new BsliCustomerCaseExemptionInfoImpl(result.getDisplayName(),"UK","GBP","BP-K520-TF002C-SC-1A","378.00","0.002","0.756","1.315644","0.756000"));
		Assert.assertEquals(customerCaseFeeInfoList,this.uco.getCustomerCaseInfoList(BillStatementType.DRAFT,sId));
	}
	
	@Test @Transactional @Ignore
	public void testCustomerCareCaseV4(){
		// -------------- Customer care case, MSDC Message x1, charge usd 4.3
		List<Integer> issueIds = this.getIssueIdsForK486Test();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		List<String> relatedSkuList = new ArrayList<String>(Arrays.asList("K486-BAL", "K486-HLK"));
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2015-12-01 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.cccUco.save(caseWithSkuToSave1);
		this.cccUco.addMessage(caseId,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2015-12-01 10:00:00 +0000",false,"120","2","3",null,"K486-HLK",null,"2","This is content for test2",false));
		this.cccUco.addMessage(caseId,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2015-12-01 10:00:00 +0000",false,"120","2","0",null,"K486-KNB",null,"2","This is content for test2",false));
		this.cccUco.addMessage(caseId,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2015-12-01 10:00:00 +0000",false,"120","2","0",null,"K486-BAL",null,"2","This is content for test2",false));
		MockAuth.logout();
		// --------------
		Date stmntStart = this.toDate("2015-11-29 00:00:00 +0000");
		Date stmntEnd = this.toDate("2015-12-13 00:00:00 +0000");
		int newStmntId = this.uco.doSettlement("K486", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K486","TWD","6642.00","6776.00","6776.00","0.00","6642.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD","22.350000","TWD","726.000000","32.484300"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",     "8.161950", null,          null,      null,"K486-KNB",   5,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 2,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",     "5.969182", null,          null,      null,"K486-KNP",   3,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",     "9.839504", null,          null,      null,"K486-HLB",   4,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 4,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",     "2.459876", null,          null,      null,"K486-HLK",   1,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 5,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "-0.140000", null,          null,"BP-K486-BAL",   null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 6,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "-4.340000", null,          null,"BP-K486-HL",    null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 7,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",     "0.399000", null,          null,"BP-K486-HL",    null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE_EXEMPTION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 8,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",    "-0.140000", null,          null,"BP-K486-KN",    null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 9,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD",     "0.140000", null,          null,"BP-K486-KN",    null,   1,TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE_EXEMPTION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,10,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",   "643.650000","TWD",  "644.000000",      null,"K486-KNP",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,11,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",  "2914.800000","TWD", "2915.000000",      null,"K486-HLB",   4,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,12,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",   "728.700000","TWD",  "729.000000",      null,"K486-HLK",   1,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-2","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,13,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",  "3622.500000","TWD", "3623.000000",      null,"K486-KNB",   5,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,14,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD",  "1449.000000","TWD", "1449.000000",      null,"K486-KNP",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT,"IVS-K486-3","US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,15,StatementLineType.SS_SERVICE_SALE_FOR_SP,         "TWD", "-3480.000000","TWD","-3480.000000",      null,      null,null,null,                                                     "6","TW"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,16,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,           null,"TWD",   "36.000000",      null,      null,null,TransactionLineType.SSI_VAT,                             null,"TW"));
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,newStmntId);
		assertEquals(expected,result);
		List<BsliCustomerCaseExemptionInfoImpl> customerCaseFeeInfoList = new ArrayList<BsliCustomerCaseExemptionInfoImpl>();
		customerCaseFeeInfoList.add(new BsliCustomerCaseExemptionInfoImpl(result.getDisplayName(),"US","USD","BP-K486-HL", "199.50","0.002","0.399","4.340000","0.399000"));
		customerCaseFeeInfoList.add(new BsliCustomerCaseExemptionInfoImpl(result.getDisplayName(),"US","USD","BP-K486-KN", "292.00","0.002","0.584","0.140000","0.140000"));
		Assert.assertEquals(customerCaseFeeInfoList,this.uco.getCustomerCaseInfoList(BillStatementType.DRAFT,newStmntId));
	}

	private List<Integer> getIssueIdsForK486Test(){
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		List<String> relatedBseList = new ArrayList<String>(Arrays.asList("BP-K486-BAL"));
		int issueId1 = this.issueUco.createIssue(new CustomerCareCaseIssueImpl(0, 1, 1, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", relatedBseList, null, null, null, null, null));
		int issueId2 = this.issueUco.createIssue(new CustomerCareCaseIssueImpl(0, 1, 1, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", relatedBseList, null, null, null, null, null));
		int issueId3 = this.issueUco.createIssue(new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", relatedBseList, null, null, null, null, null));
		return new ArrayList<Integer>(Arrays.asList(issueId1,issueId2,issueId3));
	}
	
	@Test @Transactional
	public void testRemittanceReceivedWithServiceSale(){
		this.rmtUco.create(new RemittanceImpl(0,"2013-07-17", "2013-07-22", "K488", "K2", Currency.TWD,  "50",null));
		this.rmtUco.create(new RemittanceImpl(0,"2013-08-17", "2013-08-22", "K2", "K488", Currency.TWD, "100",null));
		
		DomesticTransactionImpl dt = new DomesticTransactionImpl(null,"2013-07-09","K2","KindMinds","K488",null,"INV0001","5","TWD","4020","201","4221",new ArrayList<>());
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","1080"));
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","2940"));
		Integer dtId = this.domesticTransactionUco.createForSettlementTest(dt);
		
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-09-01 00:00:00 +0000");
		int newStmntId = uco.doSettlement("K488", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd,"K2","K488","TWD","-4221.00","0.00","100.00","50.00","-4271.00");
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId, 1, StatementLineType.SS_SERVICE_SALE_FOR_SP,"TWD","-4221.000000","TWD","-4221.000000",null,null,null,null,dtId.toString(),"TW"));
		assertEquals(expected,this.uco.getStatement(BillStatementType.DRAFT,newStmntId));
	}
	
	@Test @Transactional
	public void testRemittanceUnreceivedWithServiceSales(){
		this.rmtUco.create(new RemittanceImpl(0,"2013-07-17", "2013-07-22", "K488", "K2", Currency.TWD,  "50",null));
		this.rmtUco.create(new RemittanceImpl(0,"2013-08-17", "2013-08-22", "K2", "K488", Currency.TWD, "100",null));
		this.rmtUco.create(new RemittanceImpl(0,"2013-07-17",null,"K488", "K2",Currency.TWD, "60",null));
		this.rmtUco.create(new RemittanceImpl(0,"2013-07-17",null,"K2", "K488",Currency.TWD,"110",null));
		
		DomesticTransactionImpl dt = new DomesticTransactionImpl(null,"2013-07-09","K2","KindMinds","K488",null,"INV0001","5","TWD","4020","201","4221",new ArrayList<>());
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","1080"));
		dt.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","2940"));
		Integer dtId = this.domesticTransactionUco.createForSettlementTest(dt);
		
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd = this.toDate("2013-09-01 00:00:00 +0000");
		int newStmntId = uco.doSettlement("K488", stmntStart,stmntEnd,BillStatementType.DRAFT);
		BillStatementImpl expected = new BillStatementImpl(newStmntId,"",stmntStart,stmntEnd, "K2","K488","TWD","-4221.00","0.00","210.00","50.00","-4381.00");
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS_SERVICE_SALE_FOR_SP,"TWD","-4221.000000","TWD","-4221.000000",null,null,null,null,dtId.toString(),"TW"));		
		BillStatement result = this.uco.getStatement(BillStatementType.DRAFT,newStmntId);
		assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testDoSettlementForDraft(){
		this.prepareCurrencyExchangeRates("USD","TWD","2013-08-30 08:00:00 +0800","31.105167");
		this.prepareTestProductBase("K448", "1");
		this.prepareTestProductSku("K448", "BP-K448-1", "TEST-SKU1");
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder1);
		
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder2);
		
		int draftSid = 0;
		try{ draftSid = this.uco.doSettlementForDraft("K448", "2013-07-01", "2013-08-31"); } 
		catch (Exception e) {e.printStackTrace(); fail();}
		
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd =   this.toDate("2013-09-01 00:00:00 +0000");
		BillStatementImpl expected = new BillStatementImpl(draftSid,"",stmntStart,stmntEnd,"K2","K448","TWD","1095.00","0.00","0.00","0.00","1095.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(draftSid,1,"US","USD","17.760000","TWD","552.000000","31.105167"));
		expected.addLineItem(new BillStatementLineItemImpl(draftSid,1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "17.761990", null,        null,null,"K448-TEST-SKU1",    2, TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(draftSid,2,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD","514.500000","TWD","515.000000",null,"K448-TEST-SKU1",    2, TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT, ivsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(draftSid,3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,        null,"TWD", "28.000000",null,     null, null, TransactionLineType.SSI_VAT,                         null,"TW"));
		BillStatement result =this.uco.getStatement(BillStatementType.DRAFT,draftSid);
		assertEquals(expected,result);
	}
	
	@Test @Transactional("transactionManager")
	public void testCreateDraftStatement(){
		this.prepareCurrencyExchangeRates("USD","TWD","2013-08-30 08:00:00 +0800","31.105167");
		this.prepareTestProductBase("K448", "1");
		this.prepareTestProductSku("K448", "BP-K448-1", "TEST-SKU1");
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder1);
		
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder2);
		
		String message = this.uco.createDraft("K448", "2013-07-01", "2016-08-31");
		System.out.println(message);
	}
	
	@Test @Transactional("transactionManager")
	public void testDeleteDraftStatement(){
		this.prepareCurrencyExchangeRates("USD","TWD","2013-08-30 08:00:00 +0800","31.105167");
		this.prepareTestProductBase("K448", "1");
		this.prepareTestProductSku("K448", "BP-K448-1", "TEST-SKU1");
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder1);
		
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder2);
		
		int draftSid = 0;
		try{ draftSid = this.uco.doSettlementForDraft("K448", "2013-07-01", "2013-08-31"); } 
		catch (Exception e) {e.printStackTrace(); fail();}
		BillStatement result =this.uco.getStatement(BillStatementType.DRAFT,draftSid);
		this.uco.deleteDraft(result.getDisplayName());
		assertNull(this.uco.getStatement(BillStatementType.DRAFT,draftSid));
	}
	
	@Test @Transactional("transactionManager")
	public void testConfirmDraftStatement(){
		this.prepareCurrencyExchangeRates("USD","TWD","2013-08-30 08:00:00 +0800","31.105167");
		this.prepareTestProductBase("K448", "1");
		this.prepareTestProductSku("K448", "BP-K448-1", "TEST-SKU1");
		String unsName = "test-uns-name";
		String ivsName = "test-ivs-name";
		
		DrsTransactionImpl dtOrder1 = new DrsTransactionImpl("RETAIL_ORDER","2013-07-03 07:43:16 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","108-1955519-8402662",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder1.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder1);
		
		DrsTransactionImpl dtOrder2 = new DrsTransactionImpl("RETAIL_ORDER","2013-08-06 17:08:28 +0000",1,"K448-TEST-SKU1",1,"Amazon.com","103-9253634-5319423",ivsName,unsName,new ArrayList<DrsTransactionLineItem>());
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(1,"K3",  "K2","MS2SS_UNIT_DDP_PAYMENT",                "USD", "20.130000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(2,"K2","K448","SS2SP_Unit_Inventory_Payment",              "TWD","257.250000"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(3,"K2","K448","SS2SP_UnitProfitShareAddition",             "USD",  "8.880995"));
		dtOrder2.getSettleableItemList().add(new DrsTransactionLineItemImpl(4,"K3",  "K2","MS2SS_UnitPurchaseAllowanceWithRetailSales","USD", "-1.216250"));
		this.dtRepo.insert(dtOrder2);
		
		int draftSid = 0;
		try{ draftSid = this.uco.doSettlementForDraft("K448", "2013-07-01", "2013-08-31"); } 
		catch (Exception e) {e.printStackTrace(); fail();}
		BillStatement draft = this.uco.getStatement(BillStatementType.DRAFT,draftSid);
		int newStmntId = this.uco.confirmDraft(draft.getDisplayName());
		Date stmntStart = this.toDate("2013-07-01 00:00:00 +0000");
		Date stmntEnd =   this.toDate("2013-09-01 00:00:00 +0000");
		BillStatementImpl expected = new BillStatementImpl( newStmntId,"",stmntStart,stmntEnd,"K2","K448","TWD","1095.00","0.00","0.00","0.00","1095.00");
		expected.addProfitShareItem(new BillStatementProfitShareItemImpl(newStmntId,1,"US","USD","17.760000","TWD","552.000000","31.105167"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,1,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP, "USD", "17.761990", null,        null,null,"K448-TEST-SKU1",   2,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,null,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,2,StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT,"TWD","514.500000","TWD","515.000000",null,"K448-TEST-SKU1",   2,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT, ivsName,"US"));
		expected.addLineItem(new BillStatementLineItemImpl(newStmntId,3,StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP,  null,        null,"TWD", "28.000000",null,     null,null,TransactionLineType.SSI_VAT,                         null,"TW"));
		BillStatement formal =this.uco.getStatement(BillStatementType.OFFICIAL,newStmntId);
		assertEquals(expected,formal);
	}

	private Date toDate(String dateStr) {
		return DateHelper.toDate(dateStr, "yyyy-MM-dd HH:mm:ss Z");
	}

}
