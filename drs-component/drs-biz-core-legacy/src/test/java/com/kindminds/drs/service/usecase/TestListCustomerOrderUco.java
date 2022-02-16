package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.sales.CustomerOrder;
import com.kindminds.drs.api.v1.model.sales.ShopifyOrderItemDetail;

import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;
import com.kindminds.drs.service.security.MockAuth;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.kindminds.drs.api.usecase.sales.ListCustomerOrderUco;

import com.kindminds.drs.api.v1.model.accounting.AmazonOrderDetail;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.ListCustomerOrderConditionImpl;
import com.kindminds.drs.util.DateHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestListCustomerOrderUco {
	
	@Autowired private ListCustomerOrderUco uco;
	@Autowired private AuthenticationManager authenticationManager;
	
	@Test 
	public void testGetSupplierKcodeToNameMap(){
		Map<String,String> result = this.uco.getSupplierKcodeToNameMap();
		assertTrue(result.size()>=0);
		assertTrue(!result.containsKey("K408"));
		assertTrue(!result.containsKey("K448"));
	}
	
	@Test
	public void testGetProductBaseCodeToNameMap(){
		Map<String,String> result = this.uco.getProductBaseCodeToNameMap("K486");
		for(String key:result.keySet()){
			assertEquals("BP-K486",key.substring(0, 7));
		}
	}
	
	@Test
	public void testGetProductSkuCodeToNameMap(){
		Map<String,String> result = this.uco.getProductSkuCodeToNameMap("BP-K486-KN");
		assertEquals(3,result.size());
		assertTrue(result.containsKey("K486-KNB"));
		assertTrue(result.containsKey("K486-KNK"));
		assertTrue(result.containsKey("K486-KNP"));
	}
	
	@Test
	public void testGetDefaultListByDrsUser(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ListCustomerOrderConditionImpl nullCondition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		DtoList<CustomerOrder> dtoList = this.uco.getList(nullCondition,1);
		int totalPageNums = dtoList.getPager().getTotalPages();
		if(totalPageNums>=2) dtoList = this.uco.getList(nullCondition,2);
		MockAuth.logout();
	}
	
	@Test
	public void testGetListBySupplier(){
		List<String> hanchorSkuCodeList = Arrays.asList("KNB", "KNK", "KNP", "HLB", "HLK", "HLP", "BAL", "SPB", "SPR");
		MockAuth.login(authenticationManager, "junping@hanchor.com", "HNKbY5Qs");
		ListCustomerOrderConditionImpl nullCondition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		DtoList<CustomerOrder> dtoList =  this.uco.getList(nullCondition, 1);
		for(CustomerOrder order:dtoList.getItems()){
			assertTrue(hanchorSkuCodeList.contains(order.getSKUCode()));
		}
		int totalPageNums = dtoList.getPager().getTotalPages();
		if(totalPageNums>=2) dtoList = this.uco.getList(nullCondition,2);
		MockAuth.logout();
	}
	
	@Test
	public void testGetListByDrsUserWithConditionSupplierKcode(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl("K486", null, null, null, null, null, null, null, null, null, null, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			Assert.assertEquals("K486",customerOrder.getSKUCode().substring(0,4));
		}
		int totalPageNums = dtoList.getPager().getTotalPages();
		if(totalPageNums>=2) dtoList = this.uco.getList(condition,2);
		MockAuth.logout();
		
	}
	
	@Test
	public void testGetListBySupplierWithNullConditions(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		List<String> hanchorSkuCodeList = Arrays.asList("KNB", "KNK", "KNP", "HLB", "HLK", "HLP", "BAL", "SPB", "SPR");
		MockAuth.login(authenticationManager, "junping@hanchor.com", "HNKbY5Qs");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			assertTrue(hanchorSkuCodeList.contains(customerOrder.getSKUCode()));
		}
		MockAuth.logout();
		
	}
	
	@Test
	public void testGetListByDrsUserWithConditionProductBaseCode(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl("K486", null, null, null, null, "BP-K486-KN", null, null, null, null, null, null, null, null);
		List<String> skuListOf_BP_K486_KN = Arrays.asList("K486-KNB", "K486-KNK", "K486-KNP");
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			assertTrue(skuListOf_BP_K486_KN.contains(customerOrder.getSKUCode()));
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListBySupplierWithConditionProductSkuCode(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl("K486", null, null, null, null, "BP-K486-KN", "K486-KNK", null, null, null, null, null, null, null);
		MockAuth.login(authenticationManager, "junping@hanchor.com", "HNKbY5Qs");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			Assert.assertEquals("KNK",customerOrder.getSKUCode());
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListWithConditionOrderDateRange(){
		String orderDateStartStr = "2016-02-10";
		String orderDateEndStr = "2016-02-20";
		Date orderDateStart = this.dateStrToUtcDate(orderDateStartStr);
		Date orderDateEnd = this.dateStrToUtcDate(orderDateEndStr);
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, orderDateStartStr, orderDateEndStr, null, null, null, null, null, null, null, null, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			assertTrue(orderDateStart.before(this.dateTimeStrToUtcDate(customerOrder.getOrderTimeUTC())));
			assertTrue(this.addOneDays(orderDateEnd).after((this.dateTimeStrToUtcDate(customerOrder.getOrderTimeUTC()))));
		}
		int totalPageNums = dtoList.getPager().getTotalPages();
		for(int i=2;i<totalPageNums;i++){
			dtoList = this.uco.getList(condition,i);
			for(CustomerOrder customerOrder:dtoList.getItems()){
				assertTrue(orderDateStart.before(this.dateTimeStrToUtcDate(customerOrder.getOrderTimeUTC())));
				assertTrue(orderDateEnd.after((this.dateTimeStrToUtcDate(customerOrder.getOrderTimeUTC()))));
			}
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListWithConditionTransactionDateRange(){
		String transactionDateStartStr = "2016-01-10";
		String transactionDateEndStr = "2016-01-20";
		Date transactionDateStart = this.dateStrToUtcDate(transactionDateStartStr);
		Date transactionDateEnd = this.dateStrToUtcDate(transactionDateEndStr);
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, transactionDateStartStr, transactionDateEndStr, null, null, null, null, null, null, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			if(customerOrder.getTransactionTimeUTC() != null && customerOrder.getTransactionTimeUTC() != "(TBD)")assertTrue(transactionDateStart.before(this.dateTimeStrToUtcDate(customerOrder.getTransactionTimeUTC())));
			if(customerOrder.getTransactionTimeUTC() != null && customerOrder.getTransactionTimeUTC() != "(TBD)")assertTrue(this.addOneDays(transactionDateEnd).after((this.dateTimeStrToUtcDate(customerOrder.getTransactionTimeUTC()))));
		}
		for(int i=2;i<5;i++){
			dtoList = this.uco.getList(condition,i);
			for(CustomerOrder customerOrder:dtoList.getItems()){
				if(customerOrder.getTransactionTimeUTC() != null && customerOrder.getTransactionTimeUTC() != "(TBD)")assertTrue(transactionDateStart.before(this.dateTimeStrToUtcDate(customerOrder.getTransactionTimeUTC())));
				if(customerOrder.getTransactionTimeUTC() != null && customerOrder.getTransactionTimeUTC() != "(TBD)")assertTrue(transactionDateEnd.after((this.dateTimeStrToUtcDate(customerOrder.getTransactionTimeUTC()))));
			}
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListWithConditionMerchantSku(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, "K488-PLUG100", null, null, null, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			Assert.assertEquals("K488-PLUG100",customerOrder.getSKUCode());
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListWithConditionMarketplace(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null,"1",null, null, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			Assert.assertEquals(SalesChannel.AMAZON_COM.getDisplayName(),customerOrder.getSalesChannel());
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListWithConditionMarketplaceAmazonCoUkWithoutTrueToSource(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, "2017-08-17", "2017-08-17", null, null, null, null, null, "4",null, null, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			Assert.assertEquals(SalesChannel.AMAZON_CO_UK.getDisplayName(),customerOrder.getSalesChannel());
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListWithConditionMarketplaceOrderId(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, "002-3771805-0165839", null, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			Assert.assertEquals("002-3771805-0165839",customerOrder.getMarketplaceOrderId());
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListWithConditionPromotionId(){
		String promotionId = "Amazon Core Free Shipping";
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, null, null, null, promotionId, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			assertTrue(customerOrder.getPromotionId().toLowerCase().contains(promotionId.toLowerCase()));
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListWithEmailCondition(){
		String email = "x1lqwj8843lv0x3@marketplace.amazon.com";
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, null, null, email, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			Assert.assertEquals("104-0801763-3267422",customerOrder.getMarketplaceOrderId());
		}
		MockAuth.logout();
	}
	
	@Test @Ignore("Requirement ambiguous, using buyer name to search also buyer name and address name")
	public void testGetListWithBuyerNameCondition(){
		String buyerName = "Jason";
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, null, buyerName, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			assertTrue(customerOrder.getBuyer().toLowerCase().contains(buyerName.toLowerCase()));
		}
		MockAuth.logout();
	}

	@Test
	public void testGetListRefundedOrder(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, "103-2831114-1513033", null, null, null, null);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		for(CustomerOrder customerOrder:dtoList.getItems()){
			assertTrue(customerOrder.getOrderStatus().equals("Refunded"));
		}
		MockAuth.logout();
	}

	@Test
	public void testGetListOrderStatusPending(){
		ListCustomerOrderConditionImpl condition = new ListCustomerOrderConditionImpl(null, null, null, null, null, null, null, null, null, null, null, null, null, null, "Pending");
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		DtoList<CustomerOrder> dtoList = this.uco.getList(condition,1);
		System.out.println("*********************************");
		System.out.println("Total records size: " + dtoList.getTotalRecords());
		MockAuth.logout();
	}
	
	@Test
	public void testGetAmazonOrderDetail(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		AmazonOrderDetail amazonOrderDetail = this.uco.getAmazonOrderDetail("Amazon.com", "002-9798028-7745822", "K508-TC3582BU");		
		assertNotNull(amazonOrderDetail);		
		MockAuth.logout();
	}
	
	@Test
	public void testGetShopifyOrderDetail(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShopifyOrderItemDetail shipifyOrderDetail = this.uco.getShopifyOrderDetail("USTTS1090", "K504-TD-3140B");
		assertNotNull(shipifyOrderDetail);
		MockAuth.logout();		
	}
	
	private Date dateStrToUtcDate(String dateStr){
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd z");
	}
	
	private Date dateTimeStrToUtcDate(String dateStr){
		return DateHelper.toDate(dateStr+" UTC", "yyyy-MM-dd HH:mm:ss z");
	}
	
	private Date addOneDays(Date d){
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    c.add(Calendar.DATE, 1);
	    d.setTime( c.getTime().getTime() );
	    return d;
	}
}
