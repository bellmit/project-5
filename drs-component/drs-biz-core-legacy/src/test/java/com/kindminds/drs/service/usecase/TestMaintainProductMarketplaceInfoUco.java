package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import com.kindminds.drs.persist.v1.model.mapping.product.ProductBaseImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductSkuImpl;
import com.kindminds.drs.service.security.MockAuth;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.usecase.product.MaintainProductMarketplaceInfoUco;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo.ProductMarketStatus;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.impl.ProductBaseImplForTest;
import com.kindminds.drs.impl.ProductSkuImplForTest;
import com.kindminds.drs.impl.ProductMarketplaceInfoImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainProductMarketplaceInfoUco {
	
	@Autowired private MaintainProductBaseUco productBaseUco;
	@Autowired private MaintainProductSkuUco productSkuUco;
	@Autowired private MaintainProductMarketplaceInfoUco uco;
	@Autowired private AuthenticationManager authenticationManager;	
	
	@Test @Transactional
	public void testInsert(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductMarketplaceInfoImpl mktInfo448_1_1_US = new ProductMarketplaceInfoImpl("K448-TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K448-1A","USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		this.uco.insert(mktInfo448_1_1_US);
		ProductSkuImplForTest expectSku = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		expectSku.setRegionInfoList(new ArrayList<ProductMarketplaceInfo>());
		ProductMarketplaceInfoImpl expectRegionInfo = new ProductMarketplaceInfoImpl("K448-TEST-SKUB1S1","Jeou Lu Special",Marketplace.AMAZON_COM,"K448-1A","USD",ProductMarketStatus.REGION_LIVE.name(),"36.00","36.60","36.50","5.48","0.15","5.48","2.67","0.72","0.86","21.30","13.50");
		expectSku.addProductSkuRegionInfo(expectRegionInfo);
		MockAuth.login(authenticationManager, "K448.test", "12345");
		SKU resultSku = productSkuUco.get(skuToBeCreate448_1_1.getCodeByDrs());
		MockAuth.logout();
		assertEquals(expectSku,resultSku);
		assertEquals(expectRegionInfo,this.uco.get(mktInfo448_1_1_US.getProductCodeByDrs(), mktInfo448_1_1_US.getMarketplace().getKey()));
	}

	@Test
	public void test(){
//
//		ProductBaseImpl product = new ProductBaseImpl("K103", null, "Arts", "BP-K103-23", "Arts", "", null);
//
//		if(this.productBaseUco.getBaseProduct("BP-K103-23")==null){
//			System.out.println("1111");
//		}else{
//			this.productBaseUco.updateBaseProduct(product);
//		}
//		System.out.println(this.productBaseUco.getBaseProduct("BP-K103-23").getCodeByDrs());



		ProductSkuImpl sku = new ProductSkuImpl("K103", "BP-K103-24", "24-TTTT", "K103-Cell Phones-TTTT", "TTTT", "TTTT", "77", "SUPPLIER", "SKU_DRAFT", "30", false, "");


		this.productSkuUco.updateSku(sku);
//		System.out.println(this.productSkuUco.getProductSku("K103-22-BLUE").getStatus());
	}


	@Test @Transactional @Ignore
	public void testK101Insert(){
		ProductBaseImplForTest baseToBeCreate101_1 = new ProductBaseImplForTest("K101",null, "TEST-BASE1", "BP-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate101_1_1 = new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", "K101-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductMarketplaceInfoImpl mktInfo101_1_1_US = new ProductMarketplaceInfoImpl("TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K132-4","USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50");		
		this.productBaseUco.insert(baseToBeCreate101_1);		
		this.productSkuUco.insert(skuToBeCreate101_1_1);		
		this.uco.insert(mktInfo101_1_1_US);
		ProductSkuImplForTest expectSku = new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", "TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		expectSku.setRegionInfoList(new ArrayList<ProductMarketplaceInfo>());
		ProductMarketplaceInfoImpl expectRegionInfo = new ProductMarketplaceInfoImpl("TEST-SKUB1S1","Jeou Lu Special",Marketplace.AMAZON_COM,"K132-4","USD",ProductMarketStatus.REGION_LIVE.name(),"36.00","36.60","36.50","5.48","0.15","5.48","2.67","0.72","0.86","21.30","13.50");
		expectSku.addProductSkuRegionInfo(expectRegionInfo);
		MockAuth.login(authenticationManager,"ted.hwang@tw.drs.network", "NsFp6Ax5");
		SKU resultSku = productSkuUco.get(skuToBeCreate101_1_1.getCodeBySupplier());
		MockAuth.logout();
		assertEquals(expectSku,resultSku);
		assertEquals(expectRegionInfo,this.uco.get(mktInfo101_1_1_US.getProductCodeByDrs(), mktInfo101_1_1_US.getMarketplace().getKey()));
	}
		
	@Test(expected=IllegalArgumentException.class) @Transactional
	public void testInsertDuplicateMarketplaceFail(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductMarketplaceInfoImpl mktInfo448_1_1_US = new ProductMarketplaceInfoImpl("K448-TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K448-1A","USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		this.uco.insert(mktInfo448_1_1_US);
		this.uco.insert(mktInfo448_1_1_US);
	}
	
	@Test(expected=IllegalArgumentException.class) @Transactional @Ignore
	public void testInsertK101DuplicateMarketplaceFail(){
		ProductBaseImplForTest baseToBeCreate101_1 = new ProductBaseImplForTest("K101",null, "TEST-BASE1", "BP-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate101_1_1 = new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", "K101-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductMarketplaceInfoImpl mktInfo101_1_1_US = new ProductMarketplaceInfoImpl("TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K132-4","USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50");		
		this.productBaseUco.insert(baseToBeCreate101_1);		
		this.productSkuUco.insert(skuToBeCreate101_1_1);		
		this.uco.insert(mktInfo101_1_1_US);
		this.uco.insert(mktInfo101_1_1_US);
	}
		
	@Test @Transactional
	public void testUpdate(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductMarketplaceInfoImpl mktInfo448_1_1_US = new ProductMarketplaceInfoImpl("K448-TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K448-1A","USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		this.uco.insert(mktInfo448_1_1_US);
		ProductMarketplaceInfoImpl regionToUpdate = new ProductMarketplaceInfoImpl("K448-TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K448-1A","USD",ProductMarketStatus.REGION_ONBOARDING.name(),"36.00","36.60","36.50","5.48","0.15","5.48","2.67","0.72","0.86","21.30","13.50");
		MockAuth.login(authenticationManager, "K448.test", "12345");
		this.uco.update(regionToUpdate);		
		ProductSkuImplForTest expectSku = new ProductSkuImplForTest( "K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		expectSku.setRegionInfoList(new ArrayList<ProductMarketplaceInfo>());
		ProductMarketplaceInfoImpl expectRegionInfo = new ProductMarketplaceInfoImpl("K448-TEST-SKUB1S1","Jeou Lu Special",Marketplace.AMAZON_COM,"K448-1A","USD",ProductMarketStatus.REGION_ONBOARDING.name(),"36.00","36.60","36.50","5.48","0.15","5.48","2.67","0.72","0.86","21.30","13.50");
		expectSku.addProductSkuRegionInfo(expectRegionInfo);
		assertEquals(expectSku,this.productSkuUco.get(skuToBeCreate448_1_1.getCodeByDrs()));
		ProductMarketplaceInfo updated = this.uco.get(mktInfo448_1_1_US.getProductCodeByDrs(), mktInfo448_1_1_US.getMarketplace().getKey());
		assertEquals(expectRegionInfo,updated);
		MockAuth.logout();
	}
	
	@Test @Transactional @Ignore
	public void testK101Update(){
		ProductBaseImplForTest baseToBeCreate101_1 = new ProductBaseImplForTest("K101",null, "TEST-BASE1", "BP-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate101_1_1 = new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", "K101-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductMarketplaceInfoImpl mktInfo101_1_1_US = new ProductMarketplaceInfoImpl("TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K132-4","USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50");		
		this.productBaseUco.insert(baseToBeCreate101_1);		
		this.productSkuUco.insert(skuToBeCreate101_1_1);		
		this.uco.insert(mktInfo101_1_1_US);
		ProductMarketplaceInfoImpl regionToUpdate = new ProductMarketplaceInfoImpl("TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K132-4","USD",ProductMarketStatus.REGION_ONBOARDING.name(),"36.00","36.60","36.50","5.48","0.15","5.48","2.67","0.72","0.86","21.30","13.50");
		MockAuth.login(authenticationManager,"ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.uco.update(regionToUpdate);
		ProductSkuImplForTest expectSku = new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", "TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		expectSku.setRegionInfoList(new ArrayList<ProductMarketplaceInfo>());
		ProductMarketplaceInfoImpl expectRegionInfo = new ProductMarketplaceInfoImpl("TEST-SKUB1S1","Jeou Lu Special",Marketplace.AMAZON_COM,"K132-4","USD",ProductMarketStatus.REGION_ONBOARDING.name(),"36.00","36.60","36.50","5.48","0.15","5.48","2.67","0.72","0.86","21.30","13.50");
		expectSku.addProductSkuRegionInfo(expectRegionInfo);
		assertEquals(expectSku,this.productSkuUco.get(skuToBeCreate101_1_1.getCodeBySupplier()));
		ProductMarketplaceInfo updated = this.uco.get(mktInfo101_1_1_US.getProductCodeByDrs(), mktInfo101_1_1_US.getMarketplace().getKey());
		assertEquals(expectRegionInfo,updated);
		MockAuth.logout();				
	}
	
	@Test @Transactional
	public void testDelete(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductMarketplaceInfoImpl mktInfo448_1_1_US = new ProductMarketplaceInfoImpl("K448-TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K448-1A","USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		this.uco.insert(mktInfo448_1_1_US);
		MockAuth.login(authenticationManager, "K448.test", "12345");
		this.uco.delete(mktInfo448_1_1_US.getProductCodeByDrs(), mktInfo448_1_1_US.getMarketplace().getKey());
		ProductSkuImplForTest expectSku = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		SKU resultSku = productSkuUco.get(skuToBeCreate448_1_1.getCodeByDrs());
		MockAuth.logout();
		assertEquals(expectSku,resultSku);
		Assert.assertEquals(null,this.uco.get(mktInfo448_1_1_US.getProductCodeByDrs(), mktInfo448_1_1_US.getMarketplace().getKey()));
	}
	
	@Test @Transactional @Ignore
	public void testK101Delete(){
		ProductBaseImplForTest baseToBeCreate101_1 = new ProductBaseImplForTest("K101",null, "TEST-BASE1", "BP-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate101_1_1 = new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", "K101-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductMarketplaceInfoImpl mktInfo101_1_1_US = new ProductMarketplaceInfoImpl("TEST-SKUB1S1",null,Marketplace.AMAZON_COM,"K132-4","USD", ProductMarketStatus.REGION_LIVE.name(), "36.00","36.60","36.50", null, "0.15", null, "2.67", "0.72", "0.86", null, "13.50");		
		this.productBaseUco.insert(baseToBeCreate101_1);		
		this.productSkuUco.insert(skuToBeCreate101_1_1);		
		this.uco.insert(mktInfo101_1_1_US);
		MockAuth.login(authenticationManager,"ted.hwang@tw.drs.network", "NsFp6Ax5");
		this.uco.delete(mktInfo101_1_1_US.getProductCodeByDrs(), mktInfo101_1_1_US.getMarketplace().getKey());
		ProductSkuImplForTest expectSku = new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", "TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		SKU resultSku = productSkuUco.get(skuToBeCreate101_1_1.getCodeBySupplier());		
		MockAuth.logout();
		assertEquals(expectSku,resultSku);
		Assert.assertEquals(null,this.uco.get(mktInfo101_1_1_US.getProductCodeByDrs(), mktInfo101_1_1_US.getMarketplace().getKey()));
	}
		
	@Test @Transactional
	public void testGetRegionStatusList(){
		List<String> list = this.uco.getProductSkuMarketplaceStatusList();
		assertTrue(list.contains(ProductMarketStatus.REGION_ABSENT.name()));
		assertTrue(list.contains(ProductMarketStatus.REGION_ONBOARDING.name()));
		assertTrue(list.contains(ProductMarketStatus.REGION_LIVE.name()));
		assertTrue(list.contains(ProductMarketStatus.REGION_DEACTIVATED.name()));
		assertTrue(list.contains(ProductMarketStatus.REGION_ABORTED.name()));
		assertTrue(list.size()==6);
	}
	
	@Test @Transactional
	public void testGetMarketplaceList(){
		List<Marketplace> marketplaceList = this.uco.getMarketplaceList();
		assertEquals(8,marketplaceList.size());
		assertTrue(marketplaceList.contains(Marketplace.AMAZON_COM));
		assertTrue(marketplaceList.contains(Marketplace.TRUETOSOURCE));
		assertTrue(marketplaceList.contains(Marketplace.AMAZON_CO_UK));
		assertTrue(marketplaceList.contains(Marketplace.AMAZON_CA));
	}
	
	@Test @Transactional
	public void testGetMarketplaceCurrency(){
		Assert.assertEquals("USD",this.uco.getMarketplaceCurrency(Marketplace.AMAZON_COM.name()));
		Assert.assertEquals("USD",this.uco.getMarketplaceCurrency(Marketplace.TRUETOSOURCE.name()));
		Assert.assertEquals("GBP",this.uco.getMarketplaceCurrency(Marketplace.AMAZON_CO_UK.name()));
		Assert.assertEquals("EUR",this.uco.getMarketplaceCurrency(Marketplace.AMAZON_DE.name()));
		Assert.assertEquals("EUR",this.uco.getMarketplaceCurrency(Marketplace.AMAZON_FR.name()));
		
	}

}





















