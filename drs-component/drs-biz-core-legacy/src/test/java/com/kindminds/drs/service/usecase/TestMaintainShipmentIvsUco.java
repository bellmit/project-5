package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.impl.ShpIvsImpl;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;

import com.kindminds.drs.service.security.MockAuth;
import com.kindminds.drs.api.usecase.logistics.MaintainShipmentIvsUco;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.persist.v1.model.mapping.sales.PurchaseOrderInfoImpl;
import com.kindminds.drs.persist.v1.model.mapping.sales.PurchaseOrderSkuInfoImpl;
import com.kindminds.drs.impl.ShipmentIvsSearchConditionImpl;
import com.kindminds.drs.impl.ProductBaseImplForTest;
import com.kindminds.drs.impl.ProductSkuImplForTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContextLocal.xml" })
public class TestMaintainShipmentIvsUco {
	
	@Autowired private MaintainShipmentIvsUco uco;
	@Autowired private MaintainProductBaseUco productBaseUco;
	@Autowired private MaintainProductSkuUco productSkuUco;
	@Autowired private AuthenticationManager authenticationManager;

	private static ProductBaseImplForTest productBaseToBeCreate;
	private static ProductSkuImplForTest productSkuToBeCreate1;
	private static ProductSkuImplForTest productSkuToBeCreate2;
	private static ProductSkuImplForTest productSkuToBeCreate3;
	
	@BeforeClass
	public static void prepareTestData(){
		productBaseToBeCreate = new ProductBaseImplForTest("K448",null,"TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		productSkuToBeCreate1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU1", "K448-TEST-SKU1", "Jeou Lu A", "JLA", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		productSkuToBeCreate2 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU2", "K448-TEST-SKU2", "Jeou Lu B", "JLB", "7777777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST");
		productSkuToBeCreate3 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU3", "K448-TEST-SKU3", "Jeou Lu C", "JL3", "7777777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST");
	}
	
	@Test @Transactional("transactionManager")
	public void getDestinationCountryList(String kcode){
		List<String> destinationCountryList = this.uco.getApprovedDestinationCountries(kcode);
		assertEquals(3,destinationCountryList.size());
		assertEquals("US",destinationCountryList.get(0));
		assertEquals("UK",destinationCountryList.get(1));
		assertEquals("CA",destinationCountryList.get(2));
	}
	
	@Test @Transactional("transactionManager")
	public void testGetFcaDeliveryDate(){
		Assert.assertEquals("2016-02-26",this.uco.getFcaDeliveryDate("US","AIR_CARGO", 2, "2016-03-04"));
		Assert.assertEquals("2016-02-26",this.uco.getFcaDeliveryDate("UK","AIR_CARGO", 2, "2016-03-04"));
	}
	
	@Test @Transactional("transactionManager")
	public void testGetActiveAndOnboardingSkuCodeToNameMap(){
		this.productBaseUco.insert(new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU1", "K448-TEST-SKU1", "Jeou Lu A", "JLA", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST"));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU2", "K448-TEST-SKU2", "Jeou Lu B", "JLB", "7777777","DRS", Status.SKU_PREPARING_LAUNCH,"7",false,"NOTE FOR TEST"));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU3", "K448-TEST-SKU3", "Jeou Lu C", "JL3", "7777777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST"));
		MockAuth.login(authenticationManager, "K448.test", "12345");
		Map<String,String> result = this.uco.getActiveAndOnboardingSkuCodeToSupplierNameMap();
		assertTrue(!result.containsKey("TEST-SKU1"));
		assertTrue(result.containsKey("K448-TEST-SKU2"));
		assertTrue(result.containsKey("K448-TEST-SKU3"));
		assertTrue(!result.containsValue("Jeou Lu A"));
		assertTrue(result.containsValue("Jeou Lu B"));
		assertTrue(result.containsValue("Jeou Lu C"));
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testGetActiveAndOnboardingSkuCodeToNameMapForSpecificSupplier(){
		this.productBaseUco.insert(new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU1", "K448-TEST-SKU1", "Jeou Lu A", "JLA", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST"));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU2", "K448-TEST-SKU2", "Jeou Lu B", "JLB", "7777777","DRS", Status.SKU_PREPARING_LAUNCH,"7",false,"NOTE FOR TEST"));
		this.productSkuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU3", "K448-TEST-SKU3", "Jeou Lu C", "JL3", "7777777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST"));
		MockAuth.login(authenticationManager, "K448.test", "12345");
		Map<String,String> result = this.uco.getActiveAndOnboardingSkuCodeToSupplierNameMap("K448");
		assertTrue(!result.containsKey("TEST-SKU1"));
		assertTrue(result.containsKey("K448-TEST-SKU2"));
		assertTrue(result.containsKey("K448-TEST-SKU3"));
		assertTrue(!result.containsValue("Jeou Lu A"));
		assertTrue(result.containsValue("Jeou Lu B"));
		assertTrue(result.containsValue("Jeou Lu C"));
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testGetSupplierCurrency(){
		MockAuth.login(authenticationManager, "K448.test", "12345");
		Currency result = this.uco.getSupplierCurrency();
		MockAuth.logout();
		assertEquals(Currency.TWD,result);
	}
	
	@Test @Transactional("transactionManager")
	public void testRetrieveShipmentListBySupplierWithNullConditions(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,null,null,null);		
		assertEquals(true,this.isInDtoList(condition,"IVS-K486-11"));
		assertEquals(true,this.isInDtoList(condition,"IVS-K486-10"));
		assertEquals(true,this.isInDtoList(condition,"IVS-K486-8"));
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testRetrieveShipmentListBySupplierWithConditionDestinationCountry(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,"US",null,null);		
		assertEquals(true,this.isInDtoList(condition,"IVS-K486-10"));
		assertEquals(false,this.isInDtoList(condition,"IVS-K486-11"));
		MockAuth.logout();		
	}
	
	@Test @Transactional("transactionManager")
	public void testRetrieveShipmentListBySupplierWithConditionShippingMethod(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,null, ShippingMethod.AIR_CARGO,null);
		assertEquals(true,this.isInDtoList(condition,"IVS-K486-5"));
		assertEquals(false,this.isInDtoList(condition,"IVS-K486-3"));
		MockAuth.logout();				
	}
	
	@Test @Transactional("transactionManager")
	public void testRetrieveShipmentListBySupplierWithConditionShipmentStatus(){
		this.generateProductData();

		//todo arthur
		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5","38725.5","1936","40662","0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000","","",new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1","TEST-SKU1-NAME","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2","TEST-SKU2-NAME","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3","TEST-SKU3-NAME","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		*/

		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		//String savedShpName = this.uco.createDraft(draftToBeCreate1);
		String savedShpName = "";


		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,null,null, ShipmentStatus.SHPT_DRAFT);
		assertEquals(true,this.isInDtoList(condition,savedShpName));
		MockAuth.logout();		
	}
	
	@Test @Transactional("transactionManager")	
	public void testRetrieveShipmentListBySupplierWithMoreThanOneCondition(){
		this.generateProductData();

		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5","38725.5","1936","40662","0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000","","",new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1","TEST-SKU1-NAME","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2","TEST-SKU2-NAME","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3","TEST-SKU3-NAME","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		*/
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		//String savedShpName = this.uco.createDraft(draftToBeCreate1);
		String savedShpName  = "";

		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,"US",ShippingMethod.SEA_FREIGHT, ShipmentStatus.SHPT_DRAFT);
		assertEquals(true,this.isInDtoList(condition,savedShpName));
		MockAuth.logout();	
	}
	
	@Test @Transactional("transactionManager")
	public void testRetrieveShipmentListByDrsBuyerWithNullConditions(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,null,null,null);	
		assertEquals(true,this.isInDtoList(condition,"IVS-K510-42"));
		assertEquals(true,this.isInDtoList(condition,"IVS-K531-3"));
		assertEquals(true,this.isInDtoList(condition,"IVS-K504-6"));
		MockAuth.logout();
	}

	@Test @Transactional("transactionManager")
	public void testRetrieveShipmentListByDrsBuyerWithConditionSellerCompanyKcode(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl("K486",null,null,null);	
		assertEquals(true,this.isInDtoList(condition,"IVS-K486-11"));
		assertEquals(false,this.isInDtoList(condition,"IVS-K151-14"));
		MockAuth.logout();		
	}
	
	@Test @Transactional("transactionManager")
	public void testRetrieveShipmentListByDrsBuyerWithConditionDestinationCountry(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,"US",null,null);	
		assertEquals(true,this.isInDtoList(condition,"IVS-K510-45"));
		assertEquals(false,this.isInDtoList(condition,"IVS-K151-15"));
		MockAuth.logout();		
	}
	
	@Test @Transactional("transactionManager")
	public void testRetrieveShipmentListByDrsBuyerWithConditionShippingMethod(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,null,ShippingMethod.SEA_FREIGHT,null);	
		assertEquals(true,this.isInDtoList(condition,"IVS-K510-41"));
		assertEquals(true,this.isInDtoList(condition,"IVS-K508-10"));
		MockAuth.logout();		
	}
	
	@Test @Transactional("transactionManager")	
	public void testRetrieveShipmentListByDrsBuyerWithConditionShipmentStatus(){
		this.generateProductData();

		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5","38725.5","1936","40662","0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000","","",new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "TEST-SKU1-NAME","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "TEST-SKU2-NAME","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3", "TEST-SKU3-NAME","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		*/
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		//String savedShpName = this.uco.createDraft(draftToBeCreate1);
		String savedShpName = "";

		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,null,null,ShipmentStatus.SHPT_DRAFT);	
		assertEquals(true,this.isInDtoList(condition,savedShpName));
		MockAuth.logout();		
	}
	
	@Test @Transactional("transactionManager")	
	public void testRetrieveShipmentListByDrsBuyerWithMoreThanOneCondition(){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl("K520","US",null,null);	
		assertEquals(true,this.isInDtoList(condition,"IVS-K520-20"));
		assertEquals(false,this.isInDtoList(condition,"IVS-K533-1"));
		MockAuth.logout();		
	}
		
	@Test @Transactional("transactionManager")
	public void testInsertDraft_X1(){
		this.generateProductData();
		MockAuth.login(authenticationManager, "K448.test", "12345");
		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl(null,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5",null,null,null,"0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.14",11,7,"100.5","100","90","100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.71",17,5,"260.7","100","90","120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3,"K448-TEST-SKU3","Jeou Lu C","2.71",75,1,"117.7", "70","70", "70"));
		*/

		//todo arthur
		//String savedShpName = this.uco.createDraft(draftToBeCreate1);
		String savedShpName = "";

		/*
		ShpIvsImpl expect = new ShpIvsImpl(savedShpName,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","","K448","K2","TWD","5","38726","1936","40662","0","SHPT_DRAFT","US","SEA_FREIGHT", "2014-11-17 00:00:00",null,null,new ArrayList<IvsLineItem>());
		expect.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.140",11,7,"100.5","100","90","100"));
		expect.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.710",17,5,"260.7","100","90","120"));
		expect.addLineItem(new ShpIvsLineItemImpl(3,"K448-TEST-SKU3","Jeou Lu C","2.710",75,1,"117.7", "70","70", "70"));
		assertEquals(expect,this.uco.get(savedShpName));
		MockAuth.logout();
		*/
	}
	
	@Test @Transactional("transactionManager")
	public void testInsertDraft_X2(){
		this.generateProductData();

		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5",null,null,null,"0",null,"US","SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "Jeou Lu A","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "Jeou Lu B","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3", "Jeou Lu C","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		ShpIvsImpl draftToBeCreate2 = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5",null,null,null,"0",null,"US","SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		draftToBeCreate2.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "Jeou Lu A","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftToBeCreate2.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "Jeou Lu B","2.71", 17, 5, "260.7", "100", "90", "120"));
		*/

		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String savedShpName1 = this.uco.createDraft(draftToBeCreate1);
		String savedShpName2 = this.uco.createDraft(draftToBeCreate2);
		*/
		String savedShpName1 = "";
		String savedShpName2 = "";


		/*
		ShpIvsImpl expect1 = new ShpIvsImpl(savedShpName1,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","","K448",  "K2","TWD","5","38726","1936","40662","0","SHPT_DRAFT","US","SEA_FREIGHT", "2014-11-17 00:00:00",null,null,new ArrayList<IvsLineItem>());
		expect1.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1", "Jeou Lu A","3.140",             11,            7, "100.5","100",          "90",         "100"));
		expect1.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2", "Jeou Lu B","2.710",             17,            5, "260.7","100",          "90",         "120"));
		expect1.addLineItem(new ShpIvsLineItemImpl(3,"K448-TEST-SKU3", "Jeou Lu C","2.710",             75,            1, "117.7", "70",          "70",          "70"));
		ShpIvsImpl expect2 = new ShpIvsImpl( savedShpName2, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","","K448","K2","TWD", "5",    "29898",  "1495",  "31393","0","SHPT_DRAFT",               "US", "SEA_FREIGHT", "2014-11-17 00:00:00",null,null,new ArrayList<IvsLineItem>());
		expect2.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "Jeou Lu A","3.140",             11,            7, "100.5","100",          "90",         "100"));
		expect2.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "Jeou Lu B","2.710",             17,            5, "260.7","100",          "90",         "120"));
		assertEquals(expect1,this.uco.get(savedShpName1));
		assertEquals(expect2,this.uco.get(savedShpName2));
		MockAuth.logout();
		*/
	}
	
	@Test @Transactional("transactionManager")
	public void testUpdateDraft(){
		this.generateProductData();

		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl(null,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5",null,null,null,"0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.14",11,7,"100.5","100","90","100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.71",17,5,"260.7","100","90","120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3,"K448-TEST-SKU3","Jeou Lu C","2.71",75,1,"117.7", "70","70", "70"));
		MockAuth.login(authenticationManager, "K448.test", "12345");
		*/

		//todo arthur
		//String savedShpName = this.uco.createDraft(draftToBeCreate1);
		String savedShpName = "";

		/*
		ShpIvsImpl expectUpdated = new ShpIvsImpl(savedShpName,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","","K448","K2","TWD","5","42661","2133","44794","0","SHPT_DRAFT","US","SEA_FREIGHT","2014-11-17 00:00:00",null,"test note",new ArrayList<IvsLineItem>());

		expectUpdated.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.140",11,6,"110"  ,"100","90","100"));
		expectUpdated.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.710",17,8,"260.3","100","90","120"));
		*/

		//todo arthur
		/*
		String updatedName = this.uco.update(expectUpdated);
		assertEquals(expectUpdated,this.uco.get(updatedName));
		*/

		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testDeleteDraft(){
		this.generateProductData();

		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5",null,null,null,"0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "TEST-SKU1-NAME","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "TEST-SKU2-NAME","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3", "TEST-SKU3-NAME","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		*/

		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		//String savedShpName = this.uco.createDraft(draftToBeCreate1);
		String savedShpName = "";

		String shipmentName = this.uco.delete(savedShpName);
		ShipmentIvsSearchConditionImpl nullCondition = new ShipmentIvsSearchConditionImpl(null,null,null,null);			
		assertEquals(false,this.isInDtoList(nullCondition,shipmentName));
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testSubmit(){
		this.generateProductData();

		/*
		ShpIvsImpl shipment = new ShpIvsImpl(null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5",null,null,null,"0",null,"US","SEA_FREIGHT","2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		shipment.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.14",11,7,"100.5","100","90","100"));
		shipment.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.71",17,5,"260.7","100","90","120"));
		shipment.addLineItem(new ShpIvsLineItemImpl(3,"K448-TEST-SKU3","Jeou Lu C","2.71",75,1,"117.7", "70","70", "70"));
		MockAuth.login(authenticationManager, "K448.test", "12345");
		*/

		//todo arthur
		//String draftName = this.uco.createDraft(shipment);
		String draftName = "";

		String pickedShpName = this.uco.submit(draftName);
		/*
		shipment.setName(pickedShpName);
		shipment.setSellerCompany("K448");
		shipment.setBuyerCompany("K2");
		shipment.setCurrency(Currency.TWD);
		shipment.setSubtotal(new BigDecimal(38726));
		shipment.setSalesTax(new BigDecimal(1936));
		shipment.setTotal(new BigDecimal(40662));
		shipment.setStatus(ShipmentStatus.SHPT_AWAIT_PLAN);
		shipment.setDateCreated("2014-11-17 00:00:00");
		assertEquals(shipment,this.uco.get(pickedShpName));
		*/
		Assert.assertEquals("K448.Test@tw.drs.network",this.uco.methodForTestToGetPickupRequesterEmail(pickedShpName));
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testAccept(){
		this.generateProductData();

		/*
		ShpIvsImpl shipment = new ShpIvsImpl(null,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5",null,null,null,"0",null,"US","SEA_FREIGHT","2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		shipment.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.14",11,7,"100.5","100","90","100"));
		shipment.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.71",17,5,"260.7","100","90","120"));
		shipment.addLineItem(new ShpIvsLineItemImpl(3,"K448-TEST-SKU3","Jeou Lu C","2.71",75,1,"117.7", "70","70", "70"));
		MockAuth.login(authenticationManager, "K448.test", "12345");
		*/

		//todo arthur
		//String draftName = this.uco.createDraft(shipment);
		String draftName = "";

		String pickedShpName = this.uco.submit(draftName);
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		/*
		this.uco.accept(pickedShpName);
		shipment.setName(pickedShpName);
		shipment.setSellerCompany("K448");
		shipment.setBuyerCompany("K2");
		shipment.setCurrency(Currency.TWD);
		shipment.setSubtotal(new BigDecimal(38726));
		shipment.setSalesTax(new BigDecimal(1936));
		shipment.setTotal(new BigDecimal(40662));
		shipment.setStatus(ShipmentStatus.SHPT_PLANNING);
		shipment.setDateCreated("2014-11-17 00:00:00");
		assertEquals(shipment,this.uco.get(pickedShpName));
		assertEquals("K448.Test@tw.drs.network",this.uco.methodForTestToGetPickupRequesterEmail(pickedShpName));
		MockAuth.logout();
		*/
	}
	
	@Test @Transactional("transactionManager")
	public void testConfirm(){
		this.generateProductData();

		/*
		MockAuth.login(authenticationManager, "K448.test", "12345");
		ShpIvsImpl draftShipment = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5",null,null,null,"0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		draftShipment.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "Jeou Lu A","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftShipment.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "Jeou Lu B","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftShipment.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3", "Jeou Lu C","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		*/

		//todo arthur
		//String draftName = this.uco.createDraft(draftShipment);
		String draftName = "";

		String name = this.uco.submit(draftName);
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.uco.accept(name);
		this.uco.confirm(name);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datePruchased = sdf.format(new Date());

		//todo arhtur
		/*
		ShpIvsImpl expectConfirmed = new ShpIvsImpl(name,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","","K448","K2","TWD","5","38726","1936","40662","0","SHPT_CONFIRMED","US","SEA_FREIGHT","2014-11-17 00:00:00.0",datePruchased,null,new ArrayList<IvsLineItem>());
		ShpIvsLineItemImpl ivsLine1 =new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.140",11,7, "100.5","100","90","100");
		ShpIvsLineItemImpl ivsLine2 =new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.710",17,5, "260.7","100","90","120");
		ShpIvsLineItemImpl ivsLine3 =new ShpIvsLineItemImpl(3,"K448-TEST-SKU3","Jeou Lu C","2.71", 75,1, "117.7", "70","70", "70");
		expectConfirmed.addLineItem(ivsLine1);
		expectConfirmed.addLineItem(ivsLine2);
		expectConfirmed.addLineItem(ivsLine3);
		assertEquals(expectConfirmed,this.uco.get(name));
		
		ProductSkuStockImplSvc expectPsst1 = new ProductSkuStockImplSvc(name,ivsLine1.getSkuCode(),expectConfirmed.getSellerCompanyKcode(),expectConfirmed.getBuyerCompanyKcode(),expectConfirmed.getCurrency(),ivsLine1.getUnitAmount(),Integer.valueOf(ivsLine1.getQuantity()),0,0,null);
		ProductSkuStockImplSvc expectPsst2 = new ProductSkuStockImplSvc(name,ivsLine2.getSkuCode(),expectConfirmed.getSellerCompanyKcode(),expectConfirmed.getBuyerCompanyKcode(),expectConfirmed.getCurrency(),ivsLine2.getUnitAmount(),Integer.valueOf(ivsLine2.getQuantity()),0,0,null);
		ProductSkuStockImplSvc expectPsst3 = new ProductSkuStockImplSvc(name,ivsLine3.getSkuCode(),expectConfirmed.getSellerCompanyKcode(),expectConfirmed.getBuyerCompanyKcode(),expectConfirmed.getCurrency(),ivsLine3.getUnitAmount(),Integer.valueOf(ivsLine3.getQuantity()),0,0,null);
		assertEquals(expectPsst1,this.uco.getProductSkuStock(expectConfirmed.getName(),ivsLine1.getSkuCode()));
		assertEquals(expectPsst2,this.uco.getProductSkuStock(expectConfirmed.getName(),ivsLine2.getSkuCode()));
		assertEquals(expectPsst3,this.uco.getProductSkuStock(expectConfirmed.getName(),ivsLine3.getSkuCode()));
		*/
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testUpdateStatus(){
		this.generateProductData();

		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5",null,null,null,"0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "Jeou Lu A","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "Jeou Lu B","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3", "Jeou Lu C","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		*/

		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		//String savedShpName = this.uco.createDraft(draftToBeCreate1);
		String savedShpName = "";

		//todo arthur
		/*
		ShpIvsImpl toBePickUp = new ShpIvsImpl( savedShpName, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5","38725.5","1936","40662","0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		toBePickUp.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "Jeou Lu A","3.14", 11, 7, "100.5", "100", "90", "100"));
		toBePickUp.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "Jeou Lu B","2.71", 17, 5, "260.7", "100", "90", "120"));
		toBePickUp.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3", "Jeou Lu C","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		String pickedShpName = this.uco.submit(savedShpName);
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.uco.accept(pickedShpName);
		this.uco.confirm(pickedShpName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datePruchased = sdf.format(new Date());				
		ShpIvsImpl expectUpdated = new ShpIvsImpl(pickedShpName,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","INVOICENUM","K448","K2","TWD","5","38726","1936","40662","0","SHPT_AWAIT_PICK_UP","US","SEA_FREIGHT","2014-11-17 00:00:00.0",datePruchased,"test note",new ArrayList<IvsLineItem>());
		expectUpdated.addLineItem( new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.140",11,7, "100.5","100","90","100"));
		expectUpdated.addLineItem( new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.710",17,5, "260.7","100","90","120"));
		expectUpdated.addLineItem( new ShpIvsLineItemImpl(3,"K448-TEST-SKU3","Jeou Lu C","2.71", 75,1, "117.7", "70","70", "70"));
		*/


		//todo arthur
		/*
		this.uco.update(expectUpdated);
		assertEquals(expectUpdated,this.uco.get(pickedShpName));
		*/
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testUpdateInvoiceNumber(){
		this.generateProductData();

		/*
		ShpIvsImpl draftToBeCreate1 = new ShpIvsImpl( null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5",null,null,null,"0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "Jeou Lu A","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "Jeou Lu B","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3", "Jeou Lu C","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		
		MockAuth.login(authenticationManager, "K448.test", "12345");
		*/

		//todo arthur
		//String savedShpName = this.uco.createDraft(draftToBeCreate1);
		String savedShpName = "";

		/*
		ShpIvsImpl toBePickUp = new ShpIvsImpl( savedShpName, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","",null,null,null,"5","38725.5","1936","40662","0",null,"US", "SEA_FREIGHT", "2014-11-17 00:00 +0000",null,null,new ArrayList<IvsLineItem>());
		toBePickUp.addLineItem(new ShpIvsLineItemImpl(1, "K448-TEST-SKU1", "Jeou Lu A","3.14", 11, 7, "100.5", "100", "90", "100"));
		toBePickUp.addLineItem(new ShpIvsLineItemImpl(2, "K448-TEST-SKU2", "Jeou Lu B","2.71", 17, 5, "260.7", "100", "90", "120"));
		toBePickUp.addLineItem(new ShpIvsLineItemImpl(3, "K448-TEST-SKU3", "Jeou Lu C","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		String pickedShpName = this.uco.submit(savedShpName);
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.uco.accept(pickedShpName);
		this.uco.confirm(pickedShpName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datePruchased = sdf.format(new Date());						
		ShpIvsImpl expectUpdated = new ShpIvsImpl(pickedShpName,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17","INVOICENUM","K448","K2","TWD","5","38726","1936","40662","0","SHPT_CONFIRMED","US","SEA_FREIGHT","2014-11-17 00:00:00",datePruchased,"test note",new ArrayList<IvsLineItem>());
		expectUpdated.addLineItem( new ShpIvsLineItemImpl(1,"K448-TEST-SKU1","Jeou Lu A","3.140",11,7, "100.5","100","90","100"));
		expectUpdated.addLineItem( new ShpIvsLineItemImpl(2,"K448-TEST-SKU2","Jeou Lu B","2.710",17,5, "260.7","100","90","120"));
		expectUpdated.addLineItem( new ShpIvsLineItemImpl(3,"K448-TEST-SKU3","Jeou Lu C","2.71", 75,1, "117.7", "70","70", "70"));
		*/

		//todo arthur
		/*
		this.uco.update(expectUpdated);
		assertEquals(expectUpdated,this.uco.get(pickedShpName));
		*/
		
		MockAuth.logout();
	}
	
	@Test
	public void testGetDaysToPrepare(){
		Assert.assertEquals(1,this.uco.getDaysToPrepare("EXPRESS"));
		Assert.assertEquals(3,this.uco.getDaysToPrepare("AIR_CARGO"));
		Assert.assertEquals(5,this.uco.getDaysToPrepare("SEA_FREIGHT"));
	}
	
	@Test
	public void testGetShippingMethodList(){
		List<ShippingMethod> shippingMethodList = null;
		shippingMethodList = this.uco.getShippingMethods("US");
		assertEquals(3,shippingMethodList.size());
		assertTrue(shippingMethodList.contains(ShippingMethod.SEA_FREIGHT));
		shippingMethodList = this.uco.getShippingMethods("CA");
		assertEquals(2,shippingMethodList.size());
		assertTrue(!shippingMethodList.contains(ShippingMethod.SEA_FREIGHT));
	}
	
	@Test
	public void testGetDefaultSalesTaxPercentage(){
		MockAuth.login(authenticationManager,"K448.test","12345");
		Assert.assertEquals("5", this.uco.getDefaultSalesTaxPercentage());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		assertNull(this.uco.getDefaultSalesTaxPercentage());
		MockAuth.logout();
	}
	
	private boolean isInDtoList(ShipmentIvsSearchConditionImpl condition,String shipmentName){
		int pageIndex=1;
		int totalPages=0;

		//todo arthur
		/*
		do {
			DtoList<Ivs> dtoList = this.uco.getList(condition,pageIndex);
			if(pageIndex==1) totalPages=dtoList.getPager().getTotalPages();
			for(Ivs shp:dtoList.getItems()){
				if(shp.getName().equals(shipmentName)) return true;
			}
			pageIndex+=1;
		} while(pageIndex<=totalPages);

		*/


		return false;	
	}
				
	private void generateProductData(){
		this.productBaseUco.insert(productBaseToBeCreate);
		this.productSkuUco.insert(productSkuToBeCreate1);
		this.productSkuUco.insert(productSkuToBeCreate2);
		this.productSkuUco.insert(productSkuToBeCreate3);
	}
	
    private byte[] getBytes(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return Files.readAllBytes(file.toPath());
    }
    
    @Test @Ignore
    public void testUploadGuiInvoiceFile() throws IOException {
		MockAuth.login(authenticationManager,"junping@hanchor.com","HNKbY5Qs");
		String result = uco.saveGuiInvoiceFile( "","testSkuAsin_Inventory+Report+08-09-2018_CA.txt",
    			getBytes("test-report/testSkuAsin_Inventory+Report+08-09-2018_CA.txt"));
    	System.out.println(result);
    	assertEquals("success", result);
		MockAuth.logout();
    }
    
    @Test @Ignore
    public void testUploadFileWithNoName() throws IOException {
		MockAuth.login(authenticationManager,"junping@hanchor.com","HNKbY5Qs");
		String result = uco.saveGuiInvoiceFile("","",
    			getBytes("test-report/testSkuAsin_Inventory+Report+08-09-2018_CA.txt"));
    	System.out.println(result);
    	assertEquals("file name missing", result);
		MockAuth.logout();
    }
    
    @Test @Ignore
    public void testRemoveGuiInvoiceFile() throws IOException {
		MockAuth.login(authenticationManager,"junping@hanchor.com","HNKbY5Qs");
//    	assertTrue(uco.removeGuiInvoiceFile(
//    			"testSkuAsin_Inventory+Report+08-09-2018_CA.txt"));
		MockAuth.logout();
    }
    
    @Test
    public void testCountryOfOriginTaiwan() {

		//assertFalse(uco.getIsGuiInvoiceRequired("K520-TH001-S-TB"));
    }
    
    @Test
    public void testCountryOfOriginNotTaiwan() {
    	//assertTrue(uco.getIsGuiInvoiceRequired("K520-WA-S01"));
    }

    @Test @Transactional("transactionManager")
    public void testMixedContentBoxCase() {
		MockAuth.login(authenticationManager,"junping@hanchor.com","HNKbY5Qs");
		ShpIvsImpl ivs = createShpIvsImpl();


		//todo arthur

		/*
		String name = uco.createDraft(ivs);
		Ivs createdShipment = uco.get(name);
		
    	ShpIvsImpl expected = new ShpIvsImpl(new ShipmentIvsImpl(
				"IVS-K486-DRAFT1","Supplier Inventory","2018-12-21","4","2018-12-25",null,
				"K486","K2","TWD","0.05","99861","4993.000","104854.000",
				"US","AIR_CARGO","2018-03-15 07:23:00","SHPT_DRAFT",null,null,
				"Need DRS to help paste label.",3,"300",true,null));
		expected.addLineItem(new ShpIvsLineItemImpl(
				0,1,0,"K486-BAL","Balloon Chalk Bag","20","690","30","30","30","1.000",10,2,true,null,null,1,2));
		expected.addLineItem(new ShpIvsLineItemImpl(
				0,2,0,"K486-HLP","Hula Chalk Bag - Purple","45","781","30","30","30","1.000",15,3,false,null,null,3,5));
		expected.addLineItem(new ShpIvsLineItemImpl(
				0,3,0,"K486-KNP","Kangaroo Chalk Bag - Purple/Gray","5","968","30","30","30","1.000",5,1,false,null,null,6,6));
		expected.addLineItem(new ShpIvsLineItemImpl(
				0,4,1,"K486-HLP","Hula Chalk Bag - Purple","10","781","60","60","60","4.800",10,1,true,null,null,7,7));
		expected.addLineItem(new ShpIvsLineItemImpl(
				0,4,2,"K486-BAL","Balloon Chalk Bag","12","690","60","60","60","4.800",12,0,false,null,null,7,7));
		expected.addLineItem(new ShpIvsLineItemImpl(
				0,4,3,"K486-HLK","Hula Chalk Bag - Khaki","16","781","60","60","60","4.800",16,0,false,null,null,7,7));
		expected.addLineItem(new ShpIvsLineItemImpl(
				0,5,1,"K486-KNB","Kangaroo Chalk Bag - Black/Blue","10","968","45","45","45","2.400",10,1,false,null,null,8,8));
		expected.addLineItem(new ShpIvsLineItemImpl(
				0,5,2,"K486-HLB","Hula Chalk Bag - Black","10","781","45","45","45","2.400",10,0,false,null,null,8,8));
		assertEquals(expected, createdShipment);
		*/

		MockAuth.logout();
    }
    
    private ShpIvsImpl createShpIvsImpl() {

		/*
    	ShpIvsImpl ivs = new ShpIvsImpl(null, 
    			ShipmentType.INVENTORY, "2018-12-21", "4", "2018-12-25", null, null, 
    			null, null, "5", "125", "6", "131", null, "US", "AIR_CARGO", 
    			"2018-03-15 15:23:00+08", null, null, new ArrayList<IvsLineItem>(),
    			"Need DRS to help paste label.", "3", "300.00", true, null);
    	ivs.addLineItem(new ShpIvsLineItemImpl(0,0,0,"K486-BAL","Balloon Chalk Bag",
    			true,"30","30","30","1",10,2,"690"));
    	ivs.addLineItem(new ShpIvsLineItemImpl(0,0,0,"K486-KNP","Kangaroo Chalk Bag - Purple/Gray",
    			false,"30","30","30","1",5,1,"968"));
    	ivs.addLineItem(new ShpIvsLineItemImpl(0,0,0,"K486-HLP", "Hula Chalk Bag - Purple",
    			false,"30","30","30","1",15,3,"781"));
    	//add mixedContent Items
    	ivs.addLineItem(new ShpIvsLineItemImpl(0,2,2,"K486-HLB","Hula Chalk Bag - Black",
    			false,"45","45","45","2.4",10,0,"781"));
    	ivs.addLineItem(new ShpIvsLineItemImpl(0,1,2,"K486-BAL","Balloon Chalk Bag",
    			false,"60","60","60","4.8",12,0,"690"));
    	ivs.addLineItem(new ShpIvsLineItemImpl(0,2,1,"K486-KNB","Kangaroo Chalk Bag - Black/Blue",
    			false,"45","45","45","2.4",10,1,"968"));
    	ivs.addLineItem(new ShpIvsLineItemImpl(0,1,3,"K486-HLK","Hula Chalk Bag - Khaki",
    			false,"60","60","60","4.8",16,0,"781"));
    	ivs.addLineItem(new ShpIvsLineItemImpl(0,1,1,"K486-HLP","Hula Chalk Bag - Purple",
    			true,"60","60","60","4.8",10,1,"781"));
    	return ivs;
    	*/

		return null;
    }
    
    @Test
    public void testGetPurchaseOrderInfo() throws ParseException {
    	String shipmentName = "IVS-K510-56";
    	PurchaseOrderInfo orderInfo = uco.getPurchaseOrderInfo(shipmentName);
    	PurchaseOrderInfo expected = new PurchaseOrderInfoImpl(
    			532,"科音國際股份有限公司","03-3961958#118",
    			"33383桃園市龜山區科技一路32號",null,"2018-03-07",
    			"DRS 辦公室","137800","6890","144690", "TWD", "0.05");
    	Assert.assertEquals(expected, orderInfo);
    	System.out.println(orderInfo);
    }
    
    @Test
    public void testGetPurchaseOrderSkuList() {
    	String shipmentName = "IVS-K510-56";
    	PurchaseOrderSkuInfo expected0 = new PurchaseOrderSkuInfoImpl(
    			532,"K510-85U05001R0","美規  LOOP+DM2S+US變壓器 (使用舊盒)",30,"1220","36600");
    	PurchaseOrderSkuInfo expected1 = new PurchaseOrderSkuInfoImpl(
    			532,"K510-85U13G01R0","JUST MIXER 2 US (Golden)",20,"820","16400");
    	PurchaseOrderSkuInfo expected2 = new PurchaseOrderSkuInfoImpl(
    			532,"K510-85U13S01R0","JUST MIXER 2 US (Silver)",40,"820","32800");
    	PurchaseOrderSkuInfo expected3 = new PurchaseOrderSkuInfoImpl(
    			532,"K510-85U14W01R0","Just Mixer 5 Snow White-US/CA",20,"1340","26800");
    	PurchaseOrderSkuInfo expected4 = new PurchaseOrderSkuInfoImpl(
    			532,"K510-85U20001R0","Just Mixer M Black US-簡配",18,"1400","25200");
    	List<PurchaseOrderSkuInfo> skuList = uco.getPurchaseOrderInfoList(shipmentName);
    	for (PurchaseOrderSkuInfo skuItem : skuList) {
    		System.out.println(skuItem);
    	}
    	Assert.assertEquals(expected0, skuList.get(0));
    	Assert.assertEquals(expected1, skuList.get(1));
    	Assert.assertEquals(expected2, skuList.get(2));
    	Assert.assertEquals(expected3, skuList.get(3));
    	Assert.assertEquals(expected4, skuList.get(4));
    }


	@Test @Transactional
	public void testImportRetailIVS() throws IOException {
		MockAuth.login(authenticationManager, "robert.lee@drs.network", "DrIprl1Oc7a");
		byte[] fileBytes = getBytes("test-report/Retail_IVS_import_template_SKU_wrong.csv");
		System.out.println(uco.importRetailIVS(fileBytes));
		MockAuth.logout();
	}

	@Test @Transactional
	public void testImportDCPIVS() throws IOException {
		MockAuth.login(authenticationManager, "robert.lee@drs.network", "DrIprl1Oc7a");
		byte[] fileBytes = getBytes("test-report/DCP_IVS_import_template_2.csv");
		System.out.println(uco.importDCPIVS(fileBytes, "K618"));
		MockAuth.logout();
	}

	@Test
	public void testGetShipmentLineItemById() throws IOException {
		//System.out.println(uco.getShipmentLineItem(23540));
	}

}