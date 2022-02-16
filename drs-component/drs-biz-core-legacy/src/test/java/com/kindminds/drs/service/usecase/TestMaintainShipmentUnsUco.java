package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.List;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.service.security.MockAuth;
import com.kindminds.drs.api.usecase.logistics.MaintainShipmentIvsUco;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import com.kindminds.drs.Country;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.usecase.logistics.MaintainShipmentUnsUco;
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.ProductBaseImplForTest;
import com.kindminds.drs.impl.ProductSkuImplForTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainShipmentUnsUco {


	@Autowired private MaintainShipmentUnsUco uco;
	@Autowired private MaintainShipmentIvsUco isUco;

	@Autowired private MaintainProductBaseUco productBaseUco;
	@Autowired private MaintainProductSkuUco productSkuUco;
	@Autowired private AuthenticationManager authenticationManager;
	
	private static ProductBaseImplForTest productBaseToBeCreate;
	private static ProductSkuImplForTest productSkuToBeCreate1;
	private static ProductSkuImplForTest productSkuToBeCreate2;
	private static ProductSkuImplForTest productSkuToBeCreate3;
	private static Ivs draftISToBeCreate1;
	private static Ivs draftISToBeCreate2;
	
	@BeforeClass @Transactional("transactionManager")
	public static void prepareTestData(){
		
		productBaseToBeCreate = new ProductBaseImplForTest("K448",null,"TEST-BASE1","BP-K448-TEST-BASE1","small small","",null);
		productSkuToBeCreate1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU1", "K448-TEST-SKU1", "Jeou Lu 1", "JL1", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		productSkuToBeCreate2 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU2", "K448-TEST-SKU2", "Jeou Lu 2", "JL2", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		productSkuToBeCreate3 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKU3", "K448-TEST-SKU3", "Jeou Lu 3", "JL3", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");


		//todo arthur temp comment
		/*
		draftISToBeCreate1 = new ShpIvsImpl(null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5","38725.5","1936","40662","0",null,"US","SEA_FREIGHT","2014-11-17 00:00 +0000","","",new ArrayList<ShipmentIvsLineItem>());
		draftISToBeCreate1.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1", "Jeou Lu 1","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftISToBeCreate1.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2", "Jeou Lu 2","2.71", 17, 5, "260.7", "100", "90", "120"));
		draftISToBeCreate1.addLineItem(new ShpIvsLineItemImpl(3,"K448-TEST-SKU3", "Jeou Lu 3","2.71", 75, 1, "117.7",  "70", "70",  "70"));
		draftISToBeCreate2 = new ShpIvsImpl(null, ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5","29898","1495","31393","0",null,"US","EXPRESS","2014-11-17 00:00 +0000","","",new ArrayList<ShipmentIvsLineItem>());
		draftISToBeCreate2.addLineItem(new ShpIvsLineItemImpl(1,"K448-TEST-SKU1", "Jeou Lu 1","3.14", 11, 7, "100.5", "100", "90", "100"));
		draftISToBeCreate2.addLineItem(new ShpIvsLineItemImpl(2,"K448-TEST-SKU2", "Jeou Lu 2","2.71", 17, 5, "260.7", "100", "90", "120"));
		*/
	}
	
	@Test
	public void testGetDestinationCountryCodeList(){

		//todo arthur
		/*
		List<String> dstCountryCodeList = this.uco.getDestinationCountryCodes("K2");
		assertTrue(dstCountryCodeList.contains(Country.US.name()));
		assertTrue(dstCountryCodeList.contains(Country.UK.name()));
		*/
	}
	
	@Test
	public void testGetDrsCompanyKcode(){
		Assert.assertEquals("K3",this.uco.getDrsCompanyKcode(Country.US.name()));
	}
	
	@Test
	public void testGetCompanyCurrency(){
		Assert.assertEquals("TWD",this.uco.getCompanyCurrency("K2"));
	}

	@Test @Transactional("transactionManager")
	public void testGetShipmentDtoList(){
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
	/*
		this.prepareTestProductDataInDB();
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String shp1NewName = isUco.submit(shp1.getName());
		String shp2NewName = isUco.submit(shp2.getName());
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null, ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null,"29898",null,"US", ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,shp1NewName,"K448-TEST-SKU1","TEST-SKU1-NAME","3.14",11,7,"100.5","9.25","100","90","100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,shp2NewName,"K448-TEST-SKU2","TEST-SKU2-NAME","2.71",17,5,"260.7","9.25","100","90","120"));
		String savedUnsName = this.uco.insertDraft(u_ShpToBeInsert);
		assertTrue(this.isInDtoList(savedUnsName));
		*/
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testRetrieve_U_ShipmentListContainsNo_I_Shipment(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		ShipmentIvs shp1 = this.isUco.get(draftName1);
		ShipmentIvs shp2 = this.isUco.get(draftName2);
		String shp1NewName = isUco.submit(shp1.getName());
		String shp2NewName = isUco.submit(shp2.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null,"29898",null,"US", ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,shp1NewName, "K448-TEST-SKU1","TEST-SKU1-NAME","3.14",11,7,"100.5","9.25","100","90","100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,shp2NewName, "K448-TEST-SKU2","TEST-SKU2-NAME","2.71",17,5,"260.7","9.25","100","90","120"));
		String savedShpNameUS = this.uco.insertDraft(u_ShpToBeInsert);
		assertTrue(this.isInDtoList(savedShpNameUS));
		assertTrue(!this.isInDtoList(draftName1));
		assertTrue(!this.isInDtoList(draftName2));
		*/
		MockAuth.logout();
	}

	@Test @Transactional("transactionManager")
	public void testQueryAvailableInventoryShipmentNameList(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		List<String> availableNameList = this.uco.getAvailableIvsNames("K2","US");
		assertTrue(!availableNameList.contains(draftName1));
		assertTrue(!availableNameList.contains(draftName1));
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.isUco.accept(pickedName1);
		this.isUco.accept(pickedName2);
		this.isUco.confirm(pickedName1);
		this.isUco.confirm(pickedName2);
		availableNameList = this.uco.getAvailableIvsNames("K2","US");
		assertTrue(availableNameList.contains(pickedName1));
		assertTrue(availableNameList.contains(pickedName2));
		*/

		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testInsertDraft(){
		this.prepareTestProductDataInDB();
		
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		
		MockAuth.login(authenticationManager,"ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentUnsImplForTest uns=new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null,null,null,"US",ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		uns.setExportFxRateToEur("0.02988");
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25","100","90","100"));
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25","100","90","120"));
		String newName = this.uco.insertDraft(uns);
		assertTrue(newName.contains("DRAFT"));
		uns.setName(newName);
		uns.setInvoiceNumber("(TBD)");
		uns.setBuyerCompany("K3");
		uns.setCurrency(Currency.USD);
		uns.setAmountTotal("29,898.00");
		uns.setStatus(ShipmentStatus.SHPT_DRAFT);
		uns.setExportSrcCurrency("TWD");
		uns.setExportDstCurrency("USD");
		assertEquals(uns,this.uco.get(newName));
		*/
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testInsertDraftWithExportInfo(){
		this.prepareTestProductDataInDB();
		
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");

		ShipmentUnsImplForTest uns = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null, null,"29898",null,"US", ShippingMethod.SEA_FREIGHT, "2014-11-11 00:00 +0000","2014-11-17",null,null,"0.031415","2014-12-17 00:00 +0000", "2014-12-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14", 11, 7,"100.5","9.25", "100", "90", "100"));
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71", 17, 5,"260.7","9.25", "100", "90", "120"));
		String newName = this.uco.insertDraft(uns);
		assertTrue(newName.contains("DRAFT"));
		
		uns.setName(newName);
		uns.setInvoiceNumber("(TBD)");
		uns.setBuyerCompany("K3");
		uns.setCurrency(Currency.USD);
		uns.setAmountTotal("29,898.00");
		uns.setStatus(ShipmentStatus.SHPT_DRAFT);
		uns.setExportSrcCurrency("TWD");
		uns.setExportDstCurrency("USD");
		
		assertEquals(uns,this.uco.get(newName));
		*/

		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testFreezeDraftShipment(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");
		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedIvsName1 = isUco.submit(shp1.getName());
		String pickedIvsName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");

		Ivs pickedIvs1 = this.isUco.get(pickedIvsName1);
		Ivs pickedIvs2 = this.isUco.get(pickedIvsName2);
		this.isUco.accept(pickedIvsName1);
		this.isUco.accept(pickedIvsName2);
		this.isUco.confirm(pickedIvsName1);
		this.isUco.confirm(pickedIvsName2);
		IvsLineItem ivs1Line1 = pickedIvs1.getLineItems().get(0);
		IvsLineItem ivs1Line2 = pickedIvs1.getLineItems().get(1);
		IvsLineItem ivs1Line3 = pickedIvs1.getLineItems().get(2);
		IvsLineItem ivs2Line1 = pickedIvs2.getLineItems().get(0);
		IvsLineItem ivs2Line2 = pickedIvs2.getLineItems().get(1);
		

		ShipmentUnsImplForTest unsToBeInsert = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null,"29898", null,        "US", ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null, "2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		unsToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedIvsName1,"K448-TEST-SKU1","Jeou Lu 1","3.14", 11, 7,"100.5","9.25", "100", "90", "100"));
		unsToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedIvsName2,"K448-TEST-SKU2","Jeou Lu 2","2.71", 17, 5,"260.7","9.25", "100", "90", "120"));
		String draftNameUS = this.uco.insertDraft(unsToBeInsert);
		String frozenShpName = this.uco.freeze(draftNameUS);
		assertTrue(frozenShpName.contains("UNS-"));

		ShipmentUnsImplForTest expectFrozen = new ShipmentUnsImplForTest( frozenShpName,ShipmentType.UNIFIED,"(TBD)","","",Forwarder.UPS,"K2","K3",Currency.USD,"29,898.00",ShipmentStatus.SHPT_FROZEN,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,"TWD","USD",null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		ShipmentUnsLineItemImplForTest expectFrozenLine1 = new ShipmentUnsLineItemImplForTest(1,1,0,pickedIvsName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25","100","90","100");
		ShipmentUnsLineItemImplForTest expectFrozenLine2 = new ShipmentUnsLineItemImplForTest(2,2,0,pickedIvsName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25","100","90","120");
		expectFrozen.addLineItem(expectFrozenLine1);
		expectFrozen.addLineItem(expectFrozenLine2);
		assertEquals(expectFrozen,this.uco.get(frozenShpName));
		MockAuth.logout();
		
		ProductSkuStockImplSvc expectPsstIvs1Line1 = new ProductSkuStockImplSvc(pickedIvsName1,ivs1Line1.getSkuCode(), pickedIvs1.getSellerCompanyKcode(),pickedIvs1.getBuyerCompanyKcode(),pickedIvs1.getCurrency(),ivs1Line1.getUnitAmount(),Integer.valueOf(ivs1Line1.getQuantity()),Integer.valueOf(expectFrozenLine1.getQuantity()),0,null);
		ProductSkuStockImplSvc expectPsstIvs1Line2 = new ProductSkuStockImplSvc(pickedIvsName1,ivs1Line2.getSkuCode(), pickedIvs1.getSellerCompanyKcode(),pickedIvs1.getBuyerCompanyKcode(),pickedIvs1.getCurrency(),ivs1Line2.getUnitAmount(),Integer.valueOf(ivs1Line2.getQuantity()),                                               0,0,null);
		ProductSkuStockImplSvc expectPsstIvs1Line3 = new ProductSkuStockImplSvc(pickedIvsName1,ivs1Line3.getSkuCode(), pickedIvs1.getSellerCompanyKcode(),pickedIvs1.getBuyerCompanyKcode(),pickedIvs1.getCurrency(),ivs1Line3.getUnitAmount(),Integer.valueOf(ivs1Line3.getQuantity()),                                               0,0,null);		
		ProductSkuStockImplSvc expectPsstIvs2Line1 = new ProductSkuStockImplSvc(pickedIvsName2,ivs2Line1.getSkuCode(), pickedIvs2.getSellerCompanyKcode(),pickedIvs2.getBuyerCompanyKcode(),pickedIvs2.getCurrency(),ivs2Line1.getUnitAmount(),Integer.valueOf(ivs2Line1.getQuantity()),                                               0,0,null);
		ProductSkuStockImplSvc expectPsstIvs2Line2 = new ProductSkuStockImplSvc(pickedIvsName2,ivs2Line2.getSkuCode(), pickedIvs2.getSellerCompanyKcode(),pickedIvs2.getBuyerCompanyKcode(),pickedIvs2.getCurrency(),ivs2Line2.getUnitAmount(),Integer.valueOf(ivs2Line2.getQuantity()),Integer.valueOf(expectFrozenLine2.getQuantity()),0,null);
		ProductSkuStockImplSvc expectPsstUns1Line1 = new ProductSkuStockImplSvc(expectFrozen.getName(),expectFrozenLine1.getSkuCode(),expectFrozen.getSellerCompanyKcode(),expectFrozen.getBuyerCompanyKcode(),expectFrozen.getCurrency(),expectFrozenLine1.getUnitAmount(),Integer.valueOf(expectFrozenLine1.getQuantity()),0,0,expectFrozenLine1.getSourceInventoryShipmentName());
		ProductSkuStockImplSvc expectPsstUns1Line2 = new ProductSkuStockImplSvc(expectFrozen.getName(),expectFrozenLine2.getSkuCode(),expectFrozen.getSellerCompanyKcode(),expectFrozen.getBuyerCompanyKcode(),expectFrozen.getCurrency(),expectFrozenLine2.getUnitAmount(),Integer.valueOf(expectFrozenLine2.getQuantity()),0,0,expectFrozenLine2.getSourceInventoryShipmentName());
		
		assertEquals(expectPsstIvs1Line1,this.uco.getProductSkuStock(pickedIvsName1,ivs1Line1.getSkuCode()));
		assertEquals(expectPsstIvs1Line2,this.uco.getProductSkuStock(pickedIvsName1,ivs1Line2.getSkuCode()));
		assertEquals(expectPsstIvs1Line3,this.uco.getProductSkuStock(pickedIvsName1,ivs1Line3.getSkuCode()));
		assertEquals(expectPsstIvs2Line1,this.uco.getProductSkuStock(pickedIvsName2,ivs2Line1.getSkuCode()));
		assertEquals(expectPsstIvs2Line2,this.uco.getProductSkuStock(pickedIvsName2,ivs2Line2.getSkuCode()));
		assertEquals(expectPsstUns1Line1,this.uco.getProductSkuStock(expectFrozen.getName(),expectFrozenLine1.getSkuCode()));
		assertEquals(expectPsstUns1Line2,this.uco.getProductSkuStock(expectFrozen.getName(),expectFrozenLine2.getSkuCode()));
		*/

	}
	
	@Test @Transactional("transactionManager")
	public void testUpdateDraftData(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");
		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String shp1NewName = isUco.submit(shp1.getName());
		String shp2NewName = isUco.submit(shp2.getName());
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentUnsImplForTest uns = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null,null,null,"US",ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,shp1NewName,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25", "100", "90", "100"));
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,shp2NewName,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25", "100", "90", "120"));
		String newName = this.uco.insertDraft(uns);
		assertTrue(newName.contains("DRAFT"));
		
		uns.setName(newName);
		uns.setInvoiceNumber("(TBD)");
		uns.setBuyerCompany("K3");
		uns.setCurrency(Currency.USD);
		uns.setAmountTotal("29,898.00");
		uns.setStatus(ShipmentStatus.SHPT_DRAFT);
		uns.setExportSrcCurrency("TWD");
		uns.setExportDstCurrency("USD");
		assertEquals(uns,this.uco.get(newName));
		
		uns.getLineItems().clear();
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,shp1NewName,"K448-TEST-SKU3","Jeou Lu 3","2.71",75,1,"117.7","9.25", "70","70", "70"));
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,shp2NewName,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25","100","90","120"));
		this.uco.update(uns);
		uns.setAmountTotal("30,987.00");
		assertEquals(uns,this.uco.get(newName));
		*/
	}
	
	@Test @Transactional("transactionManager")
	public void testUpdateDraftInvoiceNumberAndExportCurrencyExchangeRateAndDstReceivedDateAndCifUnitPrice(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");
		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String shp1NewName = isUco.submit(shp1.getName());
		String shp2NewName = isUco.submit(shp2.getName());
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentUnsImplForTest uns=new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null,"29898",null,"US", ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,shp1NewName,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5",null,"100","90","100"));
		uns.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,shp2NewName,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7",null,"100","90","120"));
		String newName = this.uco.insertDraft(uns);
		assertTrue(newName.contains("DRAFT"));
		
		ShipmentUnsImplForTest toBeupdate = new ShipmentUnsImplForTest(newName,ShipmentType.UNIFIED,"INV","","",Forwarder.UPS,"K2",null,null,"30987.00",ShipmentStatus.SHPT_DRAFT,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,null,null,"0.031415","2014-12-17 00:00 +0000","2014-12-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		toBeupdate.setExportFxRateToEur("0.02911");
		toBeupdate.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,shp1NewName,"K448-TEST-SKU3","Jeou Lu 3","2.71", 75, 1,"117.7","",  "70", "70", "70"));
		toBeupdate.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,shp2NewName,"K448-TEST-SKU2","Jeou Lu 2","2.71", 17, 5,"260.7","9.25", "100", "90","120"));
		this.uco.update(toBeupdate);
		MockAuth.logout();
		ShipmentUnsImplForTest expectUpdated = new ShipmentUnsImplForTest(newName,ShipmentType.UNIFIED,"INV","","",Forwarder.UPS,"K2","K3",Currency.USD,"30,987.00",ShipmentStatus.SHPT_DRAFT,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,"TWD","USD","0.031415","2014-12-17 00:00 +0000","2014-12-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		expectUpdated.setExportFxRateToEur("0.02911");
		expectUpdated.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,shp1NewName,"K448-TEST-SKU3","Jeou Lu 3","2.71", 75, 1,"117.7",null,  "70", "70", "70"));
		expectUpdated.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,shp2NewName,"K448-TEST-SKU2","Jeou Lu 2","2.71", 17, 5,"260.7","9.25", "100", "90","120"));
		assertEquals(expectUpdated,this.uco.get(newName));
		*/
	}

	@Test @Transactional("transactionManager")
	public void testUpdateInvoiceNumberAndStatusAndTrackingNumberAndFbaId(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.isUco.accept(pickedName1);
		this.isUco.accept(pickedName2);
		this.isUco.confirm(pickedName1);
		this.isUco.confirm(pickedName2);
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED, null,"","",Forwarder.UPS,"K2",null, null, "29898", null, "US", ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14", 11, 7,"100.5","9.25", "100", "90", "100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71", 17, 5,"260.7","9.25", "100", "90", "120"));
		String draftNameUS = this.uco.insertDraft(u_ShpToBeInsert);
		String frozenShpName = this.uco.freeze(draftNameUS);
		
		ShipmentUnsImplForTest toBeupdate = new ShipmentUnsImplForTest(frozenShpName,ShipmentType.UNIFIED,"INV777","tcn","fbaId",Forwarder.UPS,"K2",null,null,"29898.00",ShipmentStatus.SHPT_IN_TRANSIT,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());                     
		toBeupdate.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.140",11,7,"100.5","9.25","100.00","90.00","100.00"));
		toBeupdate.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.710",17,5,"260.7","9.25","100.00","90.00","120.00"));
		this.uco.update(toBeupdate);
		
		ShipmentUnsImplForTest expectUpdated = new ShipmentUnsImplForTest(frozenShpName,ShipmentType.UNIFIED,"INV777","tcn","fbaId",Forwarder.UPS,"K2","K3",Currency.USD,"29,898.00",ShipmentStatus.SHPT_IN_TRANSIT,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,"TWD","USD",null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());                     
		expectUpdated.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25","100","90","100"));
		expectUpdated.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25","100","90","120"));
		assertEquals(expectUpdated,this.uco.get(frozenShpName));
		
		assertEquals(ShipmentStatus.SHPT_IN_TRANSIT, this.isUco.get(pickedName1).getStatus());
		assertEquals(ShipmentStatus.SHPT_IN_TRANSIT, this.isUco.get(pickedName2).getStatus());
		*/
		MockAuth.logout();
	}
		
	@Test @Transactional("transactionManager")
	public void testUpdateExpectedShippingDate (){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.isUco.accept(pickedName1);
		this.isUco.accept(pickedName2);
		this.isUco.confirm(pickedName1);
		this.isUco.confirm(pickedName2);
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED, null,"","",Forwarder.UPS,"K2",null, null, "29898", null, "US", ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1, "K448-TEST-SKU1","Jeou Lu 1","3.14", 11, 7,"100.5","9.25", "100", "90", "100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2, "K448-TEST-SKU2","Jeou Lu 2","2.71", 17, 5,"260.7","9.25", "100", "90", "120"));
		String draftNameUS = this.uco.insertDraft(u_ShpToBeInsert);
		String frozenShpName = this.uco.freeze(draftNameUS);
		
		ShipmentUnsImplForTest toBeupdate = new ShipmentUnsImplForTest(frozenShpName,ShipmentType.UNIFIED,"INV777","","",Forwarder.UPS,"K2",null,null, "29898",ShipmentStatus.SHPT_IN_TRANSIT,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000","2014-11-17",null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());                     
		toBeupdate.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.140",11,7,"100.5","9.25","100.00","90.00","100.00"));
		toBeupdate.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.710",17,5,"260.7","9.25","100.00","90.00","120.00"));
		this.uco.update(toBeupdate);
		
		ShipmentUnsImplForTest expectUpdated = new ShipmentUnsImplForTest(frozenShpName,ShipmentType.UNIFIED,"INV777","","",Forwarder.UPS,"K2","K3",Currency.USD, "29,898.00",ShipmentStatus.SHPT_IN_TRANSIT,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000","2014-11-17","TWD","USD",null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());                     
		expectUpdated.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25","100","90","100"));
		expectUpdated.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25","100","90","120"));
		assertEquals(expectUpdated,this.uco.get(frozenShpName));

		*/
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testUpdateExportCurrencyExchangeRateAndDestinationReceivedDate(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.isUco.accept(pickedName1);
		this.isUco.accept(pickedName2);
		this.isUco.confirm(pickedName1);
		this.isUco.confirm(pickedName2);
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null, "29898",null,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25", "100", "90", "100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25", "100", "90", "120"));
		String draftNameUS = this.uco.insertDraft(u_ShpToBeInsert);
		String frozenShpName = this.uco.freeze(draftNameUS);
		
		ShipmentUnsImplForTest toBeupdate = new ShipmentUnsImplForTest(frozenShpName,ShipmentType.UNIFIED,"INV777","","",Forwarder.UPS,"K2",null,null,"29898.00",ShipmentStatus.SHPT_IN_TRANSIT,"US",ShippingMethod.SEA_FREIGHT,"2014-11-11 00:00 +0000",null,null,null,"0.031415","2014-11-17 00:00 +0000","2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());                     
		toBeupdate.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.140",11,7,"100.5","9.25","100.00","90.00","100.00"));
		toBeupdate.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.710",17,5,"260.7","9.25","100.00","90.00","120.00"));
		this.uco.update(toBeupdate);
		
		ShipmentUnsImplForTest expectUpdated = new ShipmentUnsImplForTest(frozenShpName,ShipmentType.UNIFIED,"INV777","","",Forwarder.UPS,"K2","K3",Currency.USD,"29,898.00",ShipmentStatus.SHPT_IN_TRANSIT,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,"TWD","USD","0.031415","2014-11-17 00:00 +0000","2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());                     
		expectUpdated.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25","100","90","100"));
		expectUpdated.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25","100","90","120"));
		assertEquals(expectUpdated,this.uco.get(frozenShpName));
		*/

		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testGetAvailableSkusInInventoryShipment(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		String shp1NewName = isUco.submit(this.isUco.get(draftName1).getName());
		String shp2NewName = isUco.submit(this.isUco.get(draftName2).getName());
		MockAuth.logout();

		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		Map<String,String> availableSkuToNameMap1 = this.uco.getAvailableSkusInInventoryShipment(shp1NewName);
		assertTrue(availableSkuToNameMap1.containsKey("K448-TEST-SKU1"));
		assertTrue(availableSkuToNameMap1.containsKey("K448-TEST-SKU2"));
		Map<String,String> availableSkuToNameMap2 = this.uco.getAvailableSkusInInventoryShipment(shp2NewName);
		assertTrue(availableSkuToNameMap2.containsKey("K448-TEST-SKU1"));
		assertTrue(availableSkuToNameMap2.containsKey("K448-TEST-SKU2"));
		*/
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testGetInventoryShipmentLineItem(){	
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();

		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShpIvsLineItemImpl lineItem1 = new ShpIvsLineItemImpl(1, "K448-TEST-SKU1","Jeou Lu 1","3.14", 11, 7,"100.5", "100", "90", "100");
		ShpIvsLineItemImpl lineItem2 = new ShpIvsLineItemImpl(2, "K448-TEST-SKU2","Jeou Lu 2","2.71", 17, 5,"260.7", "100", "90", "120");
		assertEquals(lineItem1,this.uco.getIvsLineItem(pickedName1).get(0));
		assertEquals(lineItem2,this.uco.getIvsLineItem(pickedName1).get(1));
		
		ShpIvsLineItemImpl lineItem3 = new ShpIvsLineItemImpl(1, "K448-TEST-SKU1","Jeou Lu 1","3.14", 11, 7,"100.5", "100", "90", "100");
		ShpIvsLineItemImpl lineItem4 = new ShpIvsLineItemImpl(2, "K448-TEST-SKU2","Jeou Lu 2","2.71", 17, 5,"260.7", "100", "90", "120");
		assertEquals(lineItem3,this.uco.getIvsLineItem(pickedName2).get(0));
		assertEquals(lineItem4,this.uco.getIvsLineItem(pickedName2).get(1));
		*/

		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testDeleteDraft(){
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String savedShpName1 = this.isUco.createDraft(draftISToBeCreate1);
		String savedShpName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(savedShpName1);
		Ivs shp2 = this.isUco.get(savedShpName2);
		String shp1NewName = isUco.submit(shp1.getName());
		String shp2NewName = isUco.submit(shp2.getName());
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null, ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null,"29898", null,"US", ShippingMethod.SEA_FREIGHT, "2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,shp1NewName,"K448-TEST-SKU1","TEST-SKU1-NAME","3.14", 11, 7,"100.5","9.25","100","90", "100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,shp2NewName,"K448-TEST-SKU2","TEST-SKU2-NAME","2.71", 17, 5,"260.7","9.25","100","90", "120"));
		String savedShpNameUS = this.uco.insertDraft(u_ShpToBeInsert);
		
		String deletedShpName = this.uco.deleteDraft(savedShpNameUS);
		assertEquals(null,this.uco.get(deletedShpName));
		*/

		MockAuth.logout();
	}
	
	@Test
	public void testGetDrsCompanyKcodeToNameMap(){
		List<ShipmentStatus> resultList = this.uco.getShipmentStatusList();
		for(ShipmentStatus status: ShipmentStatus.values()){
			assertTrue(resultList.contains(status));
		}
	}
	
	@Test @Transactional("transactionManager")
	public void testCalculateCifAmountTotal() {
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.isUco.accept(pickedName1);
		this.isUco.accept(pickedName2);
		this.isUco.confirm(pickedName1);
		this.isUco.confirm(pickedName2);
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null, "29898",null,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25", "100", "90", "100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7","9.25", "100", "90", "120"));
		String draftNameUS = this.uco.insertDraft(u_ShpToBeInsert);
		ShipmentUns testUns = uco.get(draftNameUS);
		assertEquals("1,498.50", testUns.getCifAmountTotal());
		assertEquals(2, testUns.getLineItems().size());
		assertEquals("712.25", testUns.getLineItems().get(0).getCifSubtotal());
		assertEquals("786.25", testUns.getLineItems().get(1).getCifSubtotal());
*/
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testCalculateCifAmountHasNull() {
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.isUco.accept(pickedName1);
		this.isUco.accept(pickedName2);
		this.isUco.confirm(pickedName1);
		this.isUco.confirm(pickedName2);
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null, "29898",null,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5","9.25", "100", "90", "100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7",null, "100", "90", "120"));
		String draftNameUS = this.uco.insertDraft(u_ShpToBeInsert);
		ShipmentUns testUns = uco.get(draftNameUS);
		assertEquals("712.25", testUns.getCifAmountTotal());
		assertEquals(2, testUns.getLineItems().size());
		assertEquals("712.25", testUns.getLineItems().get(0).getCifSubtotal());
		assertEquals(null, testUns.getLineItems().get(1).getCifSubtotal());
		*/

		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager")
	public void testCalculateCifAmountAllNull() {
		this.prepareTestProductDataInDB();
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		/*
		String draftName1 = this.isUco.createDraft(draftISToBeCreate1);
		String draftName2 = this.isUco.createDraft(draftISToBeCreate2);
		Ivs shp1 = this.isUco.get(draftName1);
		Ivs shp2 = this.isUco.get(draftName2);
		String pickedName1 = isUco.submit(shp1.getName());
		String pickedName2 = isUco.submit(shp2.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.isUco.accept(pickedName1);
		this.isUco.accept(pickedName2);
		this.isUco.confirm(pickedName1);
		this.isUco.confirm(pickedName2);
		ShipmentUnsImplForTest u_ShpToBeInsert = new ShipmentUnsImplForTest(null,ShipmentType.UNIFIED,null,"","",Forwarder.UPS,"K2",null,null, "29898",null,"US",ShippingMethod.SEA_FREIGHT,"2014-11-17 00:00 +0000",null,null,null,null,null,"2014-11-17 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,pickedName1,"K448-TEST-SKU1","Jeou Lu 1","3.14",11,7,"100.5",null, "100", "90", "100"));
		u_ShpToBeInsert.addLineItem(new ShipmentUnsLineItemImplForTest(2,2,0,pickedName2,"K448-TEST-SKU2","Jeou Lu 2","2.71",17,5,"260.7",null, "100", "90", "120"));
		String draftNameUS = this.uco.insertDraft(u_ShpToBeInsert);
		ShipmentUns testUns = uco.get(draftNameUS);
		assertEquals("0.00", testUns.getCifAmountTotal());
		assertEquals(2, testUns.getLineItems().size());
		assertEquals(null, testUns.getLineItems().get(0).getCifSubtotal());
		assertEquals(null, testUns.getLineItems().get(1).getCifSubtotal());
		*/

		MockAuth.logout();
	}
	
	private void prepareTestProductDataInDB(){
		this.productBaseUco.insert(productBaseToBeCreate);
		this.productSkuUco.insert(productSkuToBeCreate1);
		this.productSkuUco.insert(productSkuToBeCreate2);
		this.productSkuUco.insert(productSkuToBeCreate3);
	}

	private boolean isInDtoList(String shipmentName){
		int pageIndex=1;
		int totalPages=0;
		do {
			DtoList<ShipmentUns> list = this.uco.getList(pageIndex);
			if(pageIndex==1) totalPages = list.getPager().getTotalPages();
			for(ShipmentUns s:list.getItems()){
				if(s.getName().equals(shipmentName)) return true;
			}
			pageIndex+=1;
		} while(pageIndex<=totalPages);
		return false;
	}
}
