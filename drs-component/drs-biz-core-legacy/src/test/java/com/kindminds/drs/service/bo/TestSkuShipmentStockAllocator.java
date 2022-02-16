package com.kindminds.drs.service.bo;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v2.biz.domain.model.inventory.SkuShipmentStockAllocator;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.impl.*;
import com.kindminds.drs.v1.model.impl.*;

import com.kindminds.drs.api.usecase.logistics.MaintainShipmentIvsUco;
import com.kindminds.drs.api.usecase.logistics.MaintainShipmentUnsUco;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductMarketplaceInfoUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.Forwarder;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.ShipmentUnsLineItem;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo.ProductMarketStatus;
import com.kindminds.drs.api.v1.model.product.SKU.Status;

import com.kindminds.drs.service.security.MockAuth;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestSkuShipmentStockAllocator {
	
	@Autowired private SkuShipmentStockAllocator allocator;
	@Autowired private MaintainProductBaseUco baseUco;
	@Autowired private MaintainProductSkuUco skuUco;
	@Autowired private MaintainProductMarketplaceInfoUco marketInfoUco;
	@Autowired private MaintainShipmentIvsUco ivsUco;

	@Autowired private MaintainShipmentUnsUco unsUco;
	@Autowired private AuthenticationManager authenticationManager;
	
	@Test @Transactional
	public void testRequestEnough(){
		String drsSku = "K448-TEST";// Using data from non-DRS product to test.
	    Integer quantity= 1;
	    this.baseUco.insert(new ProductBaseImplForTest("K448",null, "1", "BP-K448-1","bp", "", null));
	    this.skuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-1", "TEST",drsSku, "JL", "JLA", "777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST"));
	    this.marketInfoUco.insert(new ProductMarketplaceInfoImpl(drsSku,null,Marketplace.AMAZON_COM,drsSku,"USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50"));
	    String ivs1Name = this.prepareShipmentIvs(drsSku,"US",quantity,"245");
	    String uns1Name = this.prepareShipmentUns(drsSku,"US",quantity,ivs1Name,"20.13","0.032149",null);
	    List<SkuShipmentAllocationInfo> expected = new ArrayList<>();
	   // expected.add(new SkuShipmentAllocationInfoImpl(drsSku,uns1Name,ivs1Name,"USD","20.130000","TWD","245.000000","0.050000","0.032149",null) );
	    Assert.assertEquals(expected, this.allocator.requestAllocations(Country.US,drsSku,quantity));
	}
	
	@Test @Transactional
	public void testRequestCrossShipment(){
		String drsSku = "K448-TEST";// Using data from non-DRS product to test.
	    this.baseUco.insert(new ProductBaseImplForTest("K448",null, "1", "BP-K448-1","bp", "", null));
	    this.skuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-1", "TEST",drsSku, "JL", "JLA", "777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST"));
	    this.marketInfoUco.insert(new ProductMarketplaceInfoImpl(drsSku,null,Marketplace.AMAZON_COM,drsSku,"USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50"));
	    String ivs1Name = this.prepareShipmentIvs(drsSku,"US",4,"245");
	    String uns1Name = this.prepareShipmentUns(drsSku,"US",4,ivs1Name,"20.13","0.032149",null);
	    String ivs2Name = this.prepareShipmentIvs(drsSku,"US",2,"250");
	    String uns2Name = this.prepareShipmentUns(drsSku,"US",2,ivs2Name,"21.00","0.032149",null);
	    List<SkuShipmentAllocationInfo> expected = new ArrayList<>();
//	    expected.add(new SkuShipmentAllocationInfoImpl(drsSku,uns1Name,ivs1Name,"USD","20.130000","TWD","245.000000","0.050000","0.032149",null) );
//	    expected.add(new SkuShipmentAllocationInfoImpl(drsSku,uns1Name,ivs1Name,"USD","20.130000","TWD","245.000000","0.050000","0.032149",null) );
//	    expected.add(new SkuShipmentAllocationInfoImpl(drsSku,uns1Name,ivs1Name,"USD","20.130000","TWD","245.000000","0.050000","0.032149",null) );
//	    expected.add(new SkuShipmentAllocationInfoImpl(drsSku,uns1Name,ivs1Name,"USD","20.130000","TWD","245.000000","0.050000","0.032149",null) );
//	    expected.add(new SkuShipmentAllocationInfoImpl(drsSku,uns2Name,ivs2Name,"USD","21.000000","TWD","250.000000","0.050000","0.032149",null) );
	    Assert.assertEquals(expected, this.allocator.requestAllocations(Country.US,drsSku,5));
	}
	
	@Test @Transactional 
	public void testRequestTooMuch(){
		String drsSku = "K448-TEST";// Using data from non-DRS product to test.
	    Integer quantity= 1;
	    this.baseUco.insert(new ProductBaseImplForTest("K448",null, "1", "BP-K448-1","bp", "", null));
	    this.skuUco.insert(new ProductSkuImplForTest("K448", "BP-K448-1", "TEST",drsSku, "JL", "JLA", "777","DRS", Status.SKU_ACTIVE,"7",false,"NOTE FOR TEST"));
	    this.marketInfoUco.insert(new ProductMarketplaceInfoImpl(drsSku,null,Marketplace.AMAZON_COM,drsSku,"USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50"));
	    String ivs1Name = this.prepareShipmentIvs(drsSku,"US",quantity,"245");
	    String uns1Name = this.prepareShipmentUns(drsSku,"US",quantity,ivs1Name,"20.13","0.032149",null);
	    String ivs2Name = this.prepareShipmentIvs(drsSku,"US",quantity,"250");
	    String uns2Name = this.prepareShipmentUns(drsSku,"US",quantity,ivs2Name,"21.00","0.032149",null);
	    List<SkuShipmentAllocationInfo> expected = new ArrayList<>();
//	    expected.add(new SkuShipmentAllocationInfoImpl(drsSku,uns1Name,ivs1Name,"USD","20.130000","TWD","245.000000","0.050000","0.032149",null) );
//	    expected.add(new SkuShipmentAllocationInfoImpl(drsSku,uns2Name,ivs2Name,"USD","21.000000","TWD","250.000000","0.050000","0.032149",null) );
	    try{
	    	this.allocator.requestAllocations(Country.US,drsSku,3);
	    } catch (Exception e) {
	    	assertEquals("Request too much.",e.getMessage() );
		}
	    
	}
	
	private String prepareShipmentIvs(String targetSku,String dstCountry,Integer quantity, String fca){

		//todo arthur comment
		/*
		ShpIvsImpl draftIvs1 = new ShpIvsImpl(null,ShipmentType.INVENTORY,"2015-11-17","2","2015-11-17",null,null,null,null,"5","4900","245","5145","0",null,dstCountry,"SEA_FREIGHT", "2013-11-17 00:00 +0000","","",new ArrayList<IvsLineItem>());
		draftIvs1.addLineItem(new ShpIvsLineItemImpl(1,targetSku,"name","3.14",quantity, 1,fca,"100","90","100"));
		MockAuth.login(authenticationManager, "K448.test", "12345");

		//todo arthur
		//Ivs insertedDraftIvs1 = this.ivsUco.get(this.ivsUco.createDraft(draftIvs1));
		Ivs insertedDraftIvs1 = null;

		String ivsName = ivsUco.submit(insertedDraftIvs1.getName());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.ivsUco.accept(ivsName);
		this.ivsUco.confirm(ivsName);
		MockAuth.logout();
		return ivsName;
		*/

		return "";
	}
		  
	private String prepareShipmentUns(String targetSku,String dstCountry,Integer quantity,String ivsName, String ddpAmount, String rateToMultiply, String rateToEur){
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		ShipmentUnsImplForTest draftUns1 = new ShipmentUnsImplForTest(null, ShipmentType.UNIFIED,null,"","",Forwarder.DHL,"K2",null,null,"402.6",null,dstCountry, ShippingMethod.SEA_FREIGHT, "2013-11-17 00:00 +0000",null,null,null,null,null, "2013-12-01 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());
		draftUns1.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,ivsName,targetSku,"name","3.14",quantity,1,ddpAmount,"9.25","100","90","100"));
		String draftUnsName1 = this.unsUco.insertDraft(draftUns1);
		ShipmentUnsImplForTest unsToBeUpdate1 = new ShipmentUnsImplForTest(draftUnsName1,ShipmentType.UNIFIED,null,"","",Forwarder.DHL,"K2",null,null,"402.6",null,dstCountry,ShippingMethod.SEA_FREIGHT,"2013-11-17 00:00 +0000",null,null,null,rateToMultiply,"2013-12-01 00:00 +0000","2013-12-01 00:00 +0000",new ArrayList<ShipmentUnsLineItem>());                     
		unsToBeUpdate1.addLineItem(new ShipmentUnsLineItemImplForTest(1,1,0,ivsName,targetSku,"name","3.14",quantity, 1,ddpAmount,"9.25","100","90","100"));
		unsToBeUpdate1.setExportFxRateToEur(rateToEur);
		this.unsUco.update(unsToBeUpdate1);
		String unsName = this.unsUco.freeze(this.unsUco.get(draftUnsName1).getName());
		MockAuth.logout();
		return unsName;
	}
	
}
