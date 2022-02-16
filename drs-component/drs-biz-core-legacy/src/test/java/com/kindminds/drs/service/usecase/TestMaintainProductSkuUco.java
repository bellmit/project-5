package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.List;

import com.kindminds.drs.service.security.MockAuth;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.impl.ProductBaseImplForTest;
import com.kindminds.drs.impl.ProductSkuImplForTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainProductSkuUco {
	
	@Autowired private MaintainProductBaseUco productBaseUco;
	@Autowired private MaintainProductSkuUco productSkuUco;
	@Autowired private AuthenticationManager authenticationManager;
	
	@Test @Transactional 
	public void testInsertProductBaseAndSku(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448","BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		ProductBaseImplForTest expect = new ProductBaseImplForTest("K448",null,"TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		expect.addSku(skuToBeCreate448_1_1);
		MockAuth.login(authenticationManager, "K448.test", "12345");
		BaseProduct result = productBaseUco.get(baseToBeCreate448_1.getCodeByDrs());
		MockAuth.logout();
		assertEquals(expect,result);
	}

	@Test @Transactional
	public void testInsert(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		ProductSkuImplForTest expectSku = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		MockAuth.login(authenticationManager, "K448.test", "12345");
		SKU result = this.productSkuUco.get(skuToBeCreate448_1_1.getCodeByDrs());
		MockAuth.logout();
		assertEquals(expectSku,result);
	}
	
	@Test @Transactional
	public void testInsertK101(){
		this.productBaseUco.insert(new ProductBaseImplForTest("K101",null, "TEST-BASE1", null, "small small", "", null));
		this.productSkuUco.insert(new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", null, "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST"));
		ProductSkuImplForTest expectSku = new ProductSkuImplForTest("K101", "BP-K101-TEST-BASE1", "TEST-SKUB1S1", "TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		MockAuth.login(authenticationManager, "internal.supplier@tw.drs.network", "frubRe2e");
		SKU result = this.productSkuUco.get(expectSku.getCodeByDrs());
		MockAuth.logout();
		assertEquals(expectSku,result);
	}
	
	@Test @Transactional
	public void testUpdate(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);	
		ProductSkuImplForTest skuToUpdated = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "HQ", "K448-TEST-SKUB1S1", "Jeou Lu Specialized", "JLSs", "777","SUPPLIER", Status.SKU_PREPARING_LAUNCH,"8",false,"NOTE FOR TEST");
		MockAuth.login(authenticationManager, "K448.test", "12345");
		String updatedSkuCode = productSkuUco.update(skuToUpdated);
		ProductSkuImplForTest skuExpected = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "HQ", "K448-HQ", "Jeou Lu Specialized", "JLSs", "777","SUPPLIER", Status.SKU_PREPARING_LAUNCH,"8",false,"NOTE FOR TEST");
		SKU result = productSkuUco.get(updatedSkuCode);
		MockAuth.logout();
		assertEquals(skuExpected,result);
	}
	
	@Test @Transactional
	public void testUpdateOnlyMltAndContainLithium(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);	
		ProductSkuImplForTest skuToUpdated = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "HQ", "K448-TEST-SKUB1S1", "Jeou Lu Specialized", "JLSs", "777","DRS", Status.SKU_PREPARING_LAUNCH,"8",false,"NOTE FOR TEST");
		MockAuth.login(authenticationManager, "K448.test", "12345");
		String updatedSkuCode = this.productSkuUco.updateSkuMltAndContainLithium(skuToUpdated);
		ProductSkuImplForTest skuExpected = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"8",false,"NOTE FOR TEST");
		SKU result = productSkuUco.get(updatedSkuCode);
		MockAuth.logout();
		assertEquals(skuExpected,result);
	}
	
	@Test @Transactional
	public void testDelete(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		MockAuth.login(authenticationManager, "K448.test", "12345");
		assertTrue(this.productSkuUco.isSkuExist("K448", "TEST-SKUB1S1"));
		this.productSkuUco.delete(skuToBeCreate448_1_1.getCodeByDrs());
		MockAuth.logout();
		assertTrue(!this.productSkuUco.isSkuExist("K448", "TEST-SKUB1S1"));
	}
	
	@Test @Transactional
	public void testDeleteByDrsStaff(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null,"TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		MockAuth.login(authenticationManager, "lisa.hong@tw.drs.network", "eCNBs8Ev");
		assertTrue(this.productSkuUco.isSkuExist("K448", "TEST-SKUB1S1"));
		this.productSkuUco.delete(skuToBeCreate448_1_1.getCodeByDrs());
		MockAuth.logout();
		assertTrue(!this.productSkuUco.isSkuExist("K448", "TEST-SKUB1S1"));
	}
	
	@Test @Transactional
	public void testGetProductBaseCodeList(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductBaseImplForTest baseToBeCreate448_2 = new ProductBaseImplForTest("K448",null, "TEST-BASE2", "BP-K448-TEST-BASE2", "small small", "", null);
		ProductBaseImplForTest baseToBeCreate408_1 = new ProductBaseImplForTest("K408",null, "TEST-BASE3", "BP-K408-TEST-BASE3", "small small", "", null);
		productBaseUco.insert(baseToBeCreate448_1);
		productBaseUco.insert(baseToBeCreate448_2);
		productBaseUco.insert(baseToBeCreate408_1);
		List<String> baseCodeList = this.productSkuUco.getBaseCodeList("K448");
		assertTrue(baseCodeList.contains("BP-K448-TEST-BASE1"));
		assertTrue(baseCodeList.contains("BP-K448-TEST-BASE2"));
		assertTrue(!baseCodeList.contains("BP-K408-TEST-BASE1"));
		baseCodeList = this.productSkuUco.getBaseCodeList("K408");
		assertTrue(!baseCodeList.contains("BP-K448-TEST-BASE1"));
		assertTrue(!baseCodeList.contains("BP-K448-TEST-BASE2"));
		assertTrue(baseCodeList.contains("BP-K408-TEST-BASE3"));
	}
	
	@Test @Transactional
	public void testIsSkuExist(){
		ProductBaseImplForTest baseToBeCreate448_1 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductBaseImplForTest baseToBeCreate448_2 = new ProductBaseImplForTest("K448",null, "TEST-BASE2", "BP-K448-TEST-BASE2", "small small", "", null);
		ProductBaseImplForTest baseToBeCreate408_1 = new ProductBaseImplForTest("K408",null, "TEST-BASE3", "BP-K408-TEST-BASE3", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448_1_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductSkuImplForTest skuToBeCreate448_2_1 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE2", "TEST-SKUB2S1", "K448-TEST-SKUB2S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductSkuImplForTest skuToBeCreate408_1_1 = new ProductSkuImplForTest("K408", "BP-K408-TEST-BASE3", "TEST-SKUB3S1", "K408-TEST-SKUB3S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		this.productBaseUco.insert(baseToBeCreate448_1);
		this.productSkuUco.insert(skuToBeCreate448_1_1);
		assertTrue(this.productSkuUco.isSkuExist(skuToBeCreate448_1_1.getSupplierKcode(), skuToBeCreate448_1_1.getCodeBySupplier()));
		assertTrue(!this.productSkuUco.isSkuExist(skuToBeCreate448_2_1.getSupplierKcode(), skuToBeCreate448_2_1.getCodeBySupplier()));
		assertTrue(!this.productSkuUco.isSkuExist(skuToBeCreate408_1_1.getSupplierKcode(), skuToBeCreate408_1_1.getCodeBySupplier()));
		this.productBaseUco.insert(baseToBeCreate448_2);
		this.productSkuUco.insert(skuToBeCreate448_2_1);
		assertTrue(this.productSkuUco.isSkuExist(skuToBeCreate448_1_1.getSupplierKcode(), skuToBeCreate448_1_1.getCodeBySupplier()));
		assertTrue(this.productSkuUco.isSkuExist(skuToBeCreate448_2_1.getSupplierKcode(), skuToBeCreate448_2_1.getCodeBySupplier()));
		assertTrue(!this.productSkuUco.isSkuExist(skuToBeCreate408_1_1.getSupplierKcode(), skuToBeCreate408_1_1.getCodeBySupplier()));
		this.productBaseUco.insert(baseToBeCreate408_1);
		this.productSkuUco.insert(skuToBeCreate408_1_1);
		assertTrue(this.productSkuUco.isSkuExist(skuToBeCreate448_1_1.getSupplierKcode(), skuToBeCreate448_1_1.getCodeBySupplier()));
		assertTrue(this.productSkuUco.isSkuExist(skuToBeCreate448_2_1.getSupplierKcode(), skuToBeCreate448_2_1.getCodeBySupplier()));
		assertTrue(this.productSkuUco.isSkuExist(skuToBeCreate408_1_1.getSupplierKcode(), skuToBeCreate408_1_1.getCodeBySupplier()));
		MockAuth.login(authenticationManager, "K448.test", "12345");
		this.productSkuUco.delete(skuToBeCreate448_1_1.getCodeByDrs());
		assertTrue(!this.productSkuUco.isSkuExist(skuToBeCreate448_1_1.getSupplierKcode(), skuToBeCreate448_1_1.getCodeBySupplier()));
		this.productSkuUco.delete(skuToBeCreate448_2_1.getCodeByDrs());
		assertTrue(!this.productSkuUco.isSkuExist(skuToBeCreate448_2_1.getSupplierKcode(), skuToBeCreate448_2_1.getCodeBySupplier()));
		MockAuth.logout();
		MockAuth.login(authenticationManager, "K408.test", "12345");
		this.productSkuUco.delete(skuToBeCreate408_1_1.getCodeByDrs());
		assertTrue(!this.productSkuUco.isSkuExist(skuToBeCreate408_1_1.getSupplierKcode(), skuToBeCreate408_1_1.getCodeBySupplier()));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testGetSkuStatusList(){
		List<String> list = this.productSkuUco.getSkuStatusList();
		assertTrue(list.contains(Status.SKU_DRAFT.name()));
		assertTrue(list.contains(Status.SKU_PREPARING_LAUNCH.name()));
		assertTrue(list.contains(Status.SKU_ACTIVE.name()));
		assertTrue(list.contains(Status.SKU_DEACTIVATED.name()));
		assertTrue(list.contains(Status.SKU_ABORTED.name()));
		Assert.assertEquals(Status.values().length,list.size());
	}

}





















