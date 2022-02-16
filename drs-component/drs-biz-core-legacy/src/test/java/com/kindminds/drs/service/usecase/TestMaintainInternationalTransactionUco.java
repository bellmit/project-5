package com.kindminds.drs.service.usecase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import com.kindminds.drs.service.security.MockAuth;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import com.kindminds.drs.Currency;
import com.kindminds.drs.api.usecase.accounting.MaintainInternationalTransactionUco;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.InternationalTransactionImplForTest;
import com.kindminds.drs.impl.InternationalTransactionLineItemImplForTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainInternationalTransactionUco {
	
	@Autowired private MaintainInternationalTransactionUco uco;
	@Autowired private AuthenticationManager authenticationManager;
	
	private static String todayInUtcDate;
	private static String tomorrowInUtcDate;
	
	@BeforeClass
	public static void setTodayAndTomorrowInUtcDate() {
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.format("%02d", calendar.get(Calendar.MONTH)+1);
		String date = String.format("%02d", calendar.get(Calendar.DATE));
		todayInUtcDate = year+"-"+month+"-"+date;
		calendar.add(Calendar.DATE, 1);
		year = String.valueOf(calendar.get(Calendar.YEAR));
		month = String.format("%02d", calendar.get(Calendar.MONTH)+1);
		date = String.format("%02d", calendar.get(Calendar.DATE));
		tomorrowInUtcDate = year+"-"+month+"-"+date;
	}
	
	@Test
	public void testGetMsdcKcodeNameMap(){
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Map<String,String> msdcKcodeNameMap = this.uco.getMsdcKcodeNameMap();
		MockAuth.logout();
		assertTrue(!msdcKcodeNameMap.containsKey("K2"));
		assertEquals(msdcKcodeNameMap.size(),7);
		assertEquals(msdcKcodeNameMap.get("K3"),"Beanworthy");
		assertEquals(msdcKcodeNameMap.get("K3"),"Beanworthy");
		assertEquals(msdcKcodeNameMap.get("K4"),"BW-UK");
	}
	
	@Test
	public void testGetSsdcKcodeNameMap(){
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Map<String,String> ssdcKcodeNameMap = this.uco.getSsdcKcodeNameMap();
		MockAuth.logout();
		assertEquals(ssdcKcodeNameMap.size(),1);
		assertEquals(ssdcKcodeNameMap.get("K2"),"KindMinds");
	}
	
	@Test
	public void testGetSplrKcodeNameMap(){
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Map<String,String> splrKcodeNameMap = this.uco.getSplrKcodeNameMap("K2");
		MockAuth.logout();
		assertTrue(!splrKcodeNameMap.containsKey("K2"));
		assertTrue(!splrKcodeNameMap.containsKey("K3"));
		assertTrue(!splrKcodeNameMap.containsKey("K4"));
		assertEquals(splrKcodeNameMap.get("K486"),"Hanchor");
	}
	
	@Test
	public void TestGetCurrency(){
		assertEquals(Currency.USD,this.uco.getCurrency("K3"));
		assertEquals(Currency.GBP,this.uco.getCurrency("K4"));
		assertEquals(Currency.CAD,this.uco.getCurrency("K5"));
		assertEquals(Currency.EUR,this.uco.getCurrency("K6"));
	}
	
	@Test
	public void TestGetLineItemKeyNameMap(){
		Map<Integer,String> resultMap = this.uco.getLineItemKeyNameMap(CashFlowDirection.SP2MS.getKey());
		assertEquals(9,resultMap.size());
		assertEquals("MARKET_SIDE_ADVERTISING_COST",resultMap.get(5));
		assertEquals("FBA_REMOVAL_FEE",resultMap.get(80));
		assertEquals("FBA_LONG_TERM_STORAGE",resultMap.get(81));
		assertEquals("MARKET_SIDE_MARKETING_ACTIVITY",resultMap.get(76));
		assertEquals("INVENTORY_PLACEMENT_PROGRAM_COST",resultMap.get(3));
		assertEquals("OTHER_REFUND_AND_ALLOWANCE_FROM_MARKET_SIDE_TO_CUSTOMER",resultMap.get(89));
		assertEquals("SHIPPING",resultMap.get(91));
		assertEquals("FBA_MONTHLY_STORAGE",resultMap.get(95));
	}
	
	@Test
	public void TestGetLineItemKeyNameMS2SP(){
		Map<Integer,String> resultMap = this.uco.getLineItemKeyNameMap(CashFlowDirection.MS2SP.getKey());
		assertEquals(4,resultMap.size());
		assertEquals("PRODUCT_RELATED_ADDITION",resultMap.get(66));
		assertEquals("MARKETING_ACTIVITY_EXPENSE_REFUND",resultMap.get(79));
		assertEquals("BALANCE_ADJUSTMENT_ADDITION",resultMap.get(88));
		assertEquals("MARKET_SIDE_ADVERTISING_COST_REFUND",resultMap.get(90));
	}
	
	@Test @Transactional
	public void testGetList(){
		InternationalTransactionImplForTest expected = new InternationalTransactionImplForTest(null,todayInUtcDate,0,"K3","Beanworthy","K2","KindMinds","K510","Sound Land","USD","86.28", "", new ArrayList<>());
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,80,"FBA_REMOVAL_FEE", "", "11.17"));
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,81,"FBA_LONG_TERM_STORAGE", "", "75.11"));
		Integer id = this.uco.create(expected);
		expected.setId(id);
		expected.setIsEditable(true);
		assertTrue(this.isInDtoList(expected));
	}
	
	private boolean isInDtoList(InternationalTransaction it){
		int pageIndex = 1;
		int totalPages = 0;
		do {
			DtoList<InternationalTransaction> dtoList = this.uco.getList(pageIndex);
			totalPages = dtoList.getPager().getTotalPages();
			for(InternationalTransaction transaction:dtoList.getItems()){
				if(transaction.getId().equals(it.getId())&&transaction.isEditable().equals(it.isEditable())) return true;
			}
			pageIndex+=1;
		} while(pageIndex<totalPages);
		return false;
	}
	
	@Test @Transactional
	public void testCreate(){
		InternationalTransactionImplForTest expected = new InternationalTransactionImplForTest(null,todayInUtcDate,0,"K3","Beanworthy","K2","KindMinds","K510","Sound Land","USD","86.28", "", new ArrayList<>());
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,80,"FBA_REMOVAL_FEE", "", "11.17"));
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,81,"FBA_LONG_TERM_STORAGE", "", "75.11"));
		Integer id = this.uco.create(expected);
		expected.setId(id);
		expected.setIsEditable(true);
		assertEquals(expected, this.uco.get(id));
	}
	
	@Test @Transactional
	public void testUpdate(){
		InternationalTransactionImplForTest expected = new InternationalTransactionImplForTest(null,todayInUtcDate,0,"K3","Beanworthy","K2","KindMinds","K510","Sound Land","USD","86.28", "", new ArrayList<>());
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,80,"FBA_REMOVAL_FEE", "", "11.17"));
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,81,"FBA_LONG_TERM_STORAGE", "", "75.11"));
		Integer id = this.uco.create(expected);
		expected.getLineItems().clear();
		expected.setId(id);
		expected.setUtcDate(tomorrowInUtcDate);
		expected.setMsdcKcode("K4");
		expected.setSplrKcode("K486");
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,81,"FBA_LONG_TERM_STORAGE", "", "75.10"));
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,80,"FBA_REMOVAL_FEE", "", "11.16"));
		this.uco.update(expected);
		expected.setMsdcName("BW-UK");
		expected.setSplrName("Hanchor");
		expected.setTotal("86.26");
		expected.setCurrency(Currency.GBP);
		expected.setIsEditable(true);
		assertEquals(expected, this.uco.get(id));
	}
	
	@Test @Transactional
	public void testDelete(){
		InternationalTransactionImplForTest expected = new InternationalTransactionImplForTest(null,todayInUtcDate,0,"K3","Beanworthy","K2","KindMinds","K510","Sound Land","USD","86.28", "", new ArrayList<>());
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(1,80,"FBA_REMOVAL_FEE", "", "11.17"));
		expected.getLineItems().add(new InternationalTransactionLineItemImplForTest(2,81,"FBA_LONG_TERM_STORAGE", "", "75.11"));
		Integer id = this.uco.create(expected);
		this.uco.delete(id);
		expected.setId(id);
		assertTrue(!this.isInDtoList(expected));
	}

}
