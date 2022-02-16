package com.kindminds.drs.service.usecase;

import java.util.ArrayList;

import com.kindminds.drs.service.security.MockAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.kindminds.drs.api.usecase.accounting.ViewSs2spStatementUco;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport.Ss2spProfitShareReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSettleableItemReport.Ss2spSettleableItemReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemProfitShare;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemSellBackRelated;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemServiceExpense;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemShipmentRelated;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.service.test.dto.statement.Ss2spProfitShareReportImpl;
import com.kindminds.drs.service.test.dto.statement.Ss2spProfitShareReportLineItemImpl;
import com.kindminds.drs.service.test.dto.statement.Ss2spSettleableItemReportImpl;
import com.kindminds.drs.service.test.dto.statement.Ss2spSettleableItemReportLineItemImpl;
import com.kindminds.drs.service.test.dto.statement.Ss2spStatementItemProfitShareImpl;
import com.kindminds.drs.service.test.dto.statement.Ss2spStatementItemSellBackRelatedImpl;
import com.kindminds.drs.service.test.dto.statement.Ss2spStatementItemServiceExpenseImpl;
import com.kindminds.drs.service.test.dto.statement.Ss2spStatementItemShipmentRelatedImpl;
import com.kindminds.drs.service.test.dto.statement.Ss2spStatementReportImpl;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestViewSs2spStatementUco {
	
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private ViewSs2spStatementUco uco;
	
	@Test
	public void testGetStatementV1(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spStatementReportImpl expect = new Ss2spStatementReportImpl(1,"2015-06-27","2015-07-11","4609","TWD","K2","K486","1466","1466.00","0.00","4609",null,null,new ArrayList<Ss2spStatementItemProfitShare>(),new ArrayList<Ss2spStatementItemShipmentRelated>(),null,new ArrayList<Ss2spStatementItemServiceExpense>());
		expect.getProfitShareItems().add(new Ss2spStatementItemProfitShareImpl("SS_ProductProfitShareForSP","492","25","517","TWD","517.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD","4092","ss2spStatement.noteForPaymentRefund","IVS-K486-2","QA05680901","4092.000000"));
		assertEquals(expect,this.uco.getSs2spStatement(BillStatementType.OFFICIAL,"STM-K486-3"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetStatementV2(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spStatementReportImpl expect = new Ss2spStatementReportImpl(2,"2015-11-29","2015-12-12","6782","TWD","K2","K486","6776","6776","0","6782","859","43",new ArrayList<Ss2spStatementItemProfitShare>(),new ArrayList<Ss2spStatementItemShipmentRelated>(),null,new ArrayList<Ss2spStatementItemServiceExpense>());
		expect.getProfitShareItems().add(new Ss2spStatementItemProfitShareImpl("SS_ProductProfitShareForSP","859","43","902","TWD","902.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD","4288","ss2spStatement.noteForPaymentRefund","IVS-K486-2","QA05680901","4288.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD","5072","ss2spStatement.noteForPaymentRefund","IVS-K486-3","RN05681054","5072.000000"));
		expect.getStatementItemsServiceExpense().add(new Ss2spStatementItemServiceExpenseImpl("SS_ServiceSaleForSP","TWD","-3480","-3480.000000","SG50307106"));
		assertEquals(expect,this.uco.getSs2spStatement(BillStatementType.OFFICIAL,"STM-K486-14"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetStatementV3(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spStatementReportImpl expect = new Ss2spStatementReportImpl(2,"2016-01-10","2016-01-23","6634","TWD","K2","K486","6653","6653","0","6634","494","25",new ArrayList<Ss2spStatementItemProfitShare>(),new ArrayList<Ss2spStatementItemShipmentRelated>(),null,new ArrayList<Ss2spStatementItemServiceExpense>());
		expect.getProfitShareItems().add(new Ss2spStatementItemProfitShareImpl("SS_ProductProfitShareForSP","494","25","519","TWD","519.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD", "728","ss2spStatement.noteForPaymentRefund","IVS-K486-2","QA05680901", "728.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD","2655","ss2spStatement.noteForPaymentRefund","IVS-K486-3","RN05681054","2655.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD","6712","ss2spStatement.noteForPaymentRefund","IVS-K486-4","SG07529852","6712.000000"));
		expect.getStatementItemsServiceExpense().add(new Ss2spStatementItemServiceExpenseImpl("SS_ServiceSaleForSP","TWD","-3980","-3980.000000","AU50048008"));
		assertEquals(expect,this.uco.getSs2spStatement(BillStatementType.OFFICIAL,"STM-K486-17"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetStatementV4(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		Ss2spStatementReportImpl expect = new Ss2spStatementReportImpl(2,"2016-05-29","2016-06-11","8592","TWD","K2","K489","5700","5700","0","8592","1770","89",new ArrayList<Ss2spStatementItemProfitShare>(),new ArrayList<Ss2spStatementItemShipmentRelated>(),new ArrayList<Ss2spStatementItemSellBackRelated>(),new ArrayList<Ss2spStatementItemServiceExpense>());
		expect.getProfitShareItems().add(new Ss2spStatementItemProfitShareImpl("SS_ProductProfitShareForSP","1770","89","1859","TWD","1859.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD","12936","ss2spStatement.noteForPaymentRefund","IVS-K489-1","SG50852752","12936.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD","23284","ss2spStatement.noteForPaymentRefund","IVS-K489-2","SG50852778","23284.000000"));
		expect.getShipmentRelatedItems().add(new Ss2spStatementItemShipmentRelatedImpl("ss2spStatement.PaymentAndRefund","TWD", "7762","ss2spStatement.noteForPaymentRefund","IVS-K489-3","BM50495306", "7762.000000"));
		expect.getSellBackRelatedItems().add(new Ss2spStatementItemSellBackRelatedImpl("ss2spStatement.SellBack","TWD","-36220","ss2spStatement.noteForSellBack","CD50117124","-36220.000000"));
		expect.getStatementItemsServiceExpense().add(new Ss2spStatementItemServiceExpenseImpl("SS_ServiceSaleForSP","TWD","-1029","-1029.000000","CD50117116"));
		assertEquals(expect,this.uco.getSs2spStatement(BillStatementType.OFFICIAL,"STM-K489-16"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetSs2spProfitShareReportV1(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spProfitShareReportImpl expect = new Ss2spProfitShareReportImpl(2,"2015-07-12","2015-07-25","K2","K486","441",null,"441","TWD",new ArrayList<Ss2spProfitShareReportLineItem>());
		expect.getLineItems().add(new Ss2spProfitShareReportLineItemImpl("US","USD","TWD","14.09","441",null));
		assertEquals(expect,this.uco.getSs2spProfitShareReport(BillStatementType.OFFICIAL,"STM-K486-4"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetSs2spProfitShareReportV2(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spProfitShareReportImpl expect = new Ss2spProfitShareReportImpl(2,"2015-11-01","2015-11-14","K2","K486","-425","-21","-446","TWD",new ArrayList<Ss2spProfitShareReportLineItem>());
		expect.getLineItems().add(new Ss2spProfitShareReportLineItemImpl("US","USD","TWD","-13.15","-425",null));
		assertEquals(expect,this.uco.getSs2spProfitShareReport(BillStatementType.OFFICIAL,"STM-K486-12"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetSs2spProfitShareReportV3(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spProfitShareReportImpl expect = new Ss2spProfitShareReportImpl(3,"2016-02-07","2016-02-20","K2","K486","-107","-5","-112","TWD",new ArrayList<Ss2spProfitShareReportLineItem>());
		expect.getLineItems().add(new Ss2spProfitShareReportLineItemImpl("US","USD","TWD","-3.26","-107","32.912200"));
		assertEquals(expect,this.uco.getSs2spProfitShareReport(BillStatementType.OFFICIAL,"STM-K486-19"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetSettleableItemReportV1ImportDuty(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spSettleableItemReportImpl expect = new Ss2spSettleableItemReportImpl("MS2SS_PURCHASE_ALWNC_IMPORT_DUTY","2015-12-27","2016-01-09","K2","K486","USD","11.31",new ArrayList<Ss2spSettleableItemReportLineItem>());
		expect.getItemList().add(new Ss2spSettleableItemReportLineItemImpl("2015-12-30 16:00:00","K486-BAL","Balloon Chalk Bag","MS2SS_PURCHASE_ALWNC_IMPORT_DUTY",null,"IVS-K486-4","USD","11.310000","11.310000"));
		assertEquals(expect,this.uco.getSs2spSettleableItemReportForProfitShare(BillStatementType.OFFICIAL,"STM-K486-16","US","K486-BAL","MS2SS_PURCHASE_ALWNC_IMPORT_DUTY"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetSettleableItemReportV3ImportDuty(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spSettleableItemReportImpl expect = new Ss2spSettleableItemReportImpl("MS2SS_PURCHASE_ALWNC_IMPORT_DUTY","2016-04-03","2016-04-16","K2","K486","USD","33.550000",new ArrayList<Ss2spSettleableItemReportLineItem>());
		expect.getItemList().add(new Ss2spSettleableItemReportLineItemImpl("2016-04-14 16:00:00","K486-BAL","Balloon Chalk Bag","MS2SS_PURCHASE_ALWNC_IMPORT_DUTY",null,"IVS-K486-6","USD","33.550000","33.550000"));
		assertEquals(expect,this.uco.getSs2spSettleableItemReportForProfitShare(BillStatementType.OFFICIAL,"STM-K486-23","US","K486-BAL","MS2SS_PURCHASE_ALWNC_IMPORT_DUTY"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetSettleableItemReportV1ProfitShare(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spSettleableItemReportImpl expect = new Ss2spSettleableItemReportImpl("SS2SP_UnitProfitShareAddition","2015-12-27","2016-01-09","K2","K486","USD","2.46",new ArrayList<Ss2spSettleableItemReportLineItem>());
		expect.getItemList().add(new Ss2spSettleableItemReportLineItemImpl("2016-01-04 23:40:38","K486-HLB","Hula Chalk Bag - Black","SS2SP_UnitProfitShareAddition",AmazonTransactionType.ORDER.getValue(),"105-2667511-7037858","USD","2.459876","2.459876"));
		assertEquals(expect,this.uco.getSs2spSettleableItemReportForProfitShare(BillStatementType.OFFICIAL,"STM-K486-16","US","K486-HLB","SS2SP_UnitProfitShareAddition"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetSettleableItemReportV3ProfitShare(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		Ss2spSettleableItemReportImpl expect = new Ss2spSettleableItemReportImpl("SS2SP_UnitProfitShareAddition","2016-04-03","2016-04-16","K2","K486","USD","3.239732",new ArrayList<Ss2spSettleableItemReportLineItem>());
		expect.getItemList().add(new Ss2spSettleableItemReportLineItemImpl("2016-04-11 00:00:20","K486-HLK","Hula Chalk Bag - Khaki","SS2SP_UnitProfitShareAddition",AmazonTransactionType.ORDER.getValue(),"108-1424633-3744236","USD","1.103244","1.103244"));
		expect.getItemList().add(new Ss2spSettleableItemReportLineItemImpl("2016-04-12 13:07:36","K486-HLK","Hula Chalk Bag - Khaki","SS2SP_UnitProfitShareAddition",AmazonTransactionType.ORDER.getValue(),"113-5851907-6513818","USD","1.033244","1.033244"));
		expect.getItemList().add(new Ss2spSettleableItemReportLineItemImpl("2016-04-15 01:21:12","K486-HLK","Hula Chalk Bag - Khaki","SS2SP_UnitProfitShareAddition",AmazonTransactionType.ORDER.getValue(),"116-0373260-3511469","USD","1.103244","1.103244"));
		assertEquals(expect,this.uco.getSs2spSettleableItemReportForProfitShare(BillStatementType.OFFICIAL,"STM-K486-23","US","K486-HLK","SS2SP_UnitProfitShareAddition"));
		MockAuth.logout();
	}

}