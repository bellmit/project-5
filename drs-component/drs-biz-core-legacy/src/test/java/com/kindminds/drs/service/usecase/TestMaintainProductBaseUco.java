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
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.ProductBaseImplForTest;
import com.kindminds.drs.impl.ProductSkuImplForTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainProductBaseUco {
	
	@Autowired private MaintainProductBaseUco productBaseUco;
	@Autowired private MaintainProductSkuUco productSkuUco;
	@Autowired private AuthenticationManager authenticationManager;
	
	@Test @Transactional
	public void testRetrieveListWithSkuBySupplier(){
		MockAuth.login(authenticationManager, "K448.test", "12345");
		ProductBaseImplForTest toBeCreate448 = new ProductBaseImplForTest("K448",null,"TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductBaseImplForTest toBeCreate408 = new ProductBaseImplForTest("K408",null,"TEST-BASE3", "BP-K408-TEST-BASE3", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductSkuImplForTest skuToBeCreate408 = new ProductSkuImplForTest("K408", "BP-K408-TEST-BASE3", "TEST-SKUB3S1", "K408-TEST-SKUB3S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		this.productBaseUco.insert(toBeCreate448);
		this.productSkuUco.insert(skuToBeCreate448);
		this.productBaseUco.insert(toBeCreate408);
		this.productSkuUco.insert(skuToBeCreate408);
		ProductBaseImplForTest expect448 = new ProductBaseImplForTest(null,null,null,"BP-K448-TEST-BASE1",null,null,null);
		expect448.addSku(skuToBeCreate448);
		BaseProduct result = this.getBaseInDtoList(toBeCreate448.getCodeByDrs());
		assertEquals(expect448,result);
		Assert.assertEquals(null,this.getBaseInDtoList(toBeCreate408.getCodeByDrs()));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testRetrieveListWithSkuByDrsUser(){
		ProductBaseImplForTest toBeCreate448 = new ProductBaseImplForTest("K448",null,"TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductBaseImplForTest toBeCreate408 = new ProductBaseImplForTest("K408",null,"TEST-BASE3", "BP-K408-TEST-BASE3", "small small", "", null);
		ProductSkuImplForTest skuToBeCreate448 = new ProductSkuImplForTest("K448", "BP-K448-TEST-BASE1", "TEST-SKUB1S1", "K448-TEST-SKUB1S1", "Jeou Lu Special", "JLS", "7777777","DRS",Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		ProductSkuImplForTest skuToBeCreate408 = new ProductSkuImplForTest("K408", "BP-K408-TEST-BASE3", "TEST-SKUB3S1", "K408-TEST-SKUB3S1", "Jeou Lu Special", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
		this.productBaseUco.insert(toBeCreate448);
		this.productSkuUco.insert(skuToBeCreate448);
		this.productBaseUco.insert(toBeCreate408);
		this.productSkuUco.insert(skuToBeCreate408);
		
		ProductBaseImplForTest expect448 = new ProductBaseImplForTest(null,null,null,"BP-K448-TEST-BASE1",null,null,null);
		expect448.addSku(skuToBeCreate448);
		ProductBaseImplForTest expect408 = new ProductBaseImplForTest(null,null,null,"BP-K408-TEST-BASE3",null,null,null);
		expect408.addSku(skuToBeCreate408);
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		assertEquals(expect448,this.getBaseInDtoList(toBeCreate448.getCodeByDrs()));
		assertEquals(expect408,this.getBaseInDtoList(toBeCreate408.getCodeByDrs()));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testInsert(){
		ProductBaseImplForTest base = new ProductBaseImplForTest("K448","CAMERA_AND_PHOTO", "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		this.productBaseUco.insert(base);
		MockAuth.login(authenticationManager,"K448.test","12345");
		BaseProduct result = this.productBaseUco.get("BP-K448-TEST-BASE1");
		MockAuth.logout();
		assertEquals(base,result);
	}
	
	@Test(expected=IllegalArgumentException.class) @Transactional
	public void testInsertDuplicate(){
		ProductBaseImplForTest toBeCreate448 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		this.productBaseUco.insert(toBeCreate448);
		this.productBaseUco.insert(toBeCreate448);
	}
	
	@Test @Transactional
	public void testInsertK101(){
		ProductBaseImplForTest pb = new ProductBaseImplForTest("K101","CAMERA_AND_PHOTO","TEST-BASE1",null,"small small","",null);
		this.productBaseUco.insert(pb);
		MockAuth.login(authenticationManager, "internal.supplier@tw.drs.network", "frubRe2e");
		BaseProduct result = this.productBaseUco.get("BP-K101-TEST-BASE1");
		MockAuth.logout();
		pb.setCodeByDrs("BP-K101-TEST-BASE1");
		assertEquals(pb,result);
	}
	
	@Test @Transactional
	public void testUpdate(){
		ProductBaseImplForTest pb = new ProductBaseImplForTest("K448","CAMERA_AND_PHOTO", "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		this.productBaseUco.insert(pb);
		MockAuth.login(authenticationManager, "K448.test", "12345");
		pb.setCategory("BOOKS");
		pb.setCodeBySupplier("SLRC");
		pb.setNameBySupplier("SSS");
		this.productBaseUco.update(pb);
		String codeByDrs = "BP-K448-SLRC";
		BaseProduct updatedResult = this.productBaseUco.get(codeByDrs);
		MockAuth.logout();
		pb.setCodeByDrs(codeByDrs);
		assertEquals(pb,updatedResult);
	}
	
	@Test @Transactional
	public void testDeleteNoChildren(){
		ProductBaseImplForTest toBeCreate448 = new ProductBaseImplForTest("K448",null,"TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		this.productBaseUco.insert(toBeCreate448);
		MockAuth.login(authenticationManager, "K448.test", "12345");
		String deletedBaseCode = productBaseUco.delete(toBeCreate448.getCodeByDrs());
		MockAuth.logout();
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		Assert.assertEquals(null,productBaseUco.get(deletedBaseCode));
		assertEquals(false,this.isInDtoList(deletedBaseCode));
		MockAuth.logout();
		MockAuth.login(authenticationManager, "K448.test", "12345");
		assertEquals(false,this.isInDtoList(deletedBaseCode));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testIsBaseExist(){
		ProductBaseImplForTest toBeCreate448 = new ProductBaseImplForTest("K448",null, "TEST-BASE1", "BP-K448-TEST-BASE1", "small small", "", null);
		ProductBaseImplForTest toBeCreate408 = new ProductBaseImplForTest("K408",null, "TEST-BASE3", "BP-K408-TEST-BASE3", "small small", "", null);
		ProductBaseImplForTest toBeCreate448_2 = new ProductBaseImplForTest("K448",null, "TEST-BASE2", "BP-K448-TEST-BASE2", "small small", "", null);
		this.productBaseUco.insert(toBeCreate448);
		assertTrue(this.productBaseUco.isBaseExist(toBeCreate448.getSupplierKcode(), toBeCreate448.getCodeBySupplier()));
		assertTrue(!this.productBaseUco.isBaseExist(toBeCreate448_2.getSupplierKcode(), toBeCreate448_2.getCodeBySupplier()));
		assertTrue(!this.productBaseUco.isBaseExist(toBeCreate408.getSupplierKcode(), toBeCreate408.getCodeBySupplier()));
		this.productBaseUco.insert(toBeCreate448_2);
		assertTrue(this.productBaseUco.isBaseExist(toBeCreate448.getSupplierKcode(), toBeCreate448.getCodeBySupplier()));
		assertTrue(this.productBaseUco.isBaseExist(toBeCreate448_2.getSupplierKcode(), toBeCreate448_2.getCodeBySupplier()));
		assertTrue(!this.productBaseUco.isBaseExist(toBeCreate408.getSupplierKcode(), toBeCreate408.getCodeBySupplier()));
		this.productBaseUco.insert(toBeCreate408);
		assertTrue(this.productBaseUco.isBaseExist(toBeCreate448.getSupplierKcode(), toBeCreate448.getCodeBySupplier()));
		assertTrue(this.productBaseUco.isBaseExist(toBeCreate448_2.getSupplierKcode(), toBeCreate448_2.getCodeBySupplier()));
		assertTrue(this.productBaseUco.isBaseExist(toBeCreate408.getSupplierKcode(), toBeCreate408.getCodeBySupplier()));
		MockAuth.login(authenticationManager, "K448.test", "12345");
		this.productBaseUco.delete(toBeCreate448.getCodeByDrs());
		assertTrue(!this.productBaseUco.isBaseExist(toBeCreate448.getSupplierKcode(), toBeCreate448.getCodeBySupplier()));
		this.productBaseUco.delete(toBeCreate448_2.getCodeByDrs());
		assertTrue(!this.productBaseUco.isBaseExist(toBeCreate448_2.getSupplierKcode(), toBeCreate448_2.getCodeBySupplier()));
		MockAuth.logout();
		MockAuth.login(authenticationManager, "K408.test", "12345");
		this.productBaseUco.delete(toBeCreate408.getCodeByDrs());
		assertTrue(!this.productBaseUco.isBaseExist(toBeCreate408.getSupplierKcode(), toBeCreate408.getCodeBySupplier()));
		MockAuth.logout();
	}
	
	@Test
	public void testGetCategoryList(){
		List<String> result = this.productBaseUco.getCategoryList();
		assertEquals(32, result.size());
	}
	
	private boolean isInDtoList(String baseCodeByDrs){
		int pageIndex = 1;
		int totalPages = 0;
		do {
			DtoList<BaseProduct> dtoList = this.productBaseUco.getList(pageIndex);
			totalPages = dtoList.getPager().getTotalPages();
			for(BaseProduct bp:dtoList.getItems()){
				if(bp.getCodeByDrs().equals(baseCodeByDrs)) return true;
			}
			pageIndex+=1;
		} while(pageIndex < totalPages);
		return false;
	}
	
	private BaseProduct getBaseInDtoList(String baseCodeByDrs){
		int pageIndex=1;
		DtoList<BaseProduct> dtoList = this.productBaseUco.getList(1);
		int totalPages = dtoList.getPager().getTotalPages();
		do {
			for(BaseProduct bp:dtoList.getItems()){
				if(bp.getCodeByDrs().equals(baseCodeByDrs)) return bp;
			}
			pageIndex+=1;
			dtoList = this.productBaseUco.getList(pageIndex);
		} while(pageIndex < totalPages);
		return null;
	}
}





















