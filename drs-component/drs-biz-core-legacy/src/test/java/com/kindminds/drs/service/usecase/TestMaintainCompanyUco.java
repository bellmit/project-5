package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.kindminds.drs.api.v1.model.Company;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.MaintainCompanyUco;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.CompanyImplForTest;
import com.kindminds.drs.impl.ProductBaseImplForTest;
import com.kindminds.drs.impl.ProductSkuImplForTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainCompanyUco {
	
	@Autowired private MaintainCompanyUco uco;
	@Autowired private MaintainProductBaseUco productBaseUco;
	@Autowired private MaintainProductSkuUco productSkuUco;
	
	private static ProductBaseImplForTest productBaseToBeCreateKtestBase;
	private static ProductSkuImplForTest productSkuToBeCreateKtestSku;

	@BeforeClass
	public static void prepareTestData(){
		productBaseToBeCreateKtestBase = new ProductBaseImplForTest("KTEST",null, "BASE1", "BP-KTEST-BASE1", "for testing", "", null);
		productSkuToBeCreateKtestSku = new ProductSkuImplForTest("KTEST", "BP-KTEST-BASE1", "SKU1", "KTEST-SKU1", "for testing", "JLS", "7777777","DRS", Status.SKU_DRAFT,"7",false,"NOTE FOR TEST");
	}
	
	@Test @Transactional
	public void testRetrieveList(){
		List<String> serviceEmailList = Arrays.asList("test@test.com");
		List<String> productEmailList = Arrays.asList("test@test.com");
		String kcode = this.uco.createSupplier(new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測","台北市大安區延吉街62巷9號1F","22222222",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", null, null, null));
		this.productBaseUco.insert(productBaseToBeCreateKtestBase);
		this.productSkuUco.insert(productSkuToBeCreateKtestSku);
		CompanyImplForTest expect = new CompanyImplForTest("KTEST",null,null,null,"測",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
		List<SKU> skuList = new ArrayList<SKU>();
		skuList.add(new ProductSkuImplForTest(null,null,null,"KTEST-SKU1",null,"JLS",null,null,null,null,false,null));
		expect.setSkuList(skuList);
		//assertEquals(expect,this.getCompanyInDtoList(kcode));
	}
	
	@Test @Transactional
	public void testInsertSupplier() {
		List<String> serviceEmailList = Arrays.asList("test@test.com","test1@test.com");
		List<String> productEmailList = Arrays.asList("test@test.com","test1@test.com");
		String kcode = this.uco.createSupplier(new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測","台北市大安區延吉街62巷9號1F","22222222",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", null, null, null));
		CompanyImplForTest expect = new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測","台北市大安區延吉街62巷9號1F","22222222",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", "K2", false, true);
		assertEquals(expect,this.uco.getCompany(kcode)); 
	}
	
	@Test @Transactional
	public void testInsertSupplierWithNullAddressAndPhoneAndServiceMailAddr() {
		String kcode = this.uco.createSupplier(new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測",null,null,null,null,null,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", null, null, null));
		CompanyImplForTest expect = new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測",null,null,null,null,null,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", "K2", false, true);
		assertEquals(expect,this.uco.getCompany(kcode)); 
	}
	
	@Test @Transactional 
	public void testUpdate() {
		List<String> serviceEmailList = Arrays.asList("test@test.com");
		List<String> productEmailList = Arrays.asList("test@test.com");
		String kcode = this.uco.createSupplier(new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測","台北市大安區延吉街62巷9號1F","22222222",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", null, null, null));
		CompanyImplForTest toBeUpdated = new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.T.C","測測","台北市大安區延吉街62巷9號1F","12345678",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", null, null, null);
		this.uco.update(toBeUpdated);
		CompanyImplForTest expect = new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.T.C","測測","台北市大安區延吉街62巷9號1F","12345678",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", "K2", false, true);
		assertEquals(expect,this.uco.getCompany(kcode));
	}
	
	@Test @Transactional 
	public void testUpdatePartial() {
		List<String> serviceEmailList = Arrays.asList("test@test.com");
		List<String> productEmailList = Arrays.asList("test@test.com");
		CompanyImplForTest testCom = new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測","台北市大安區延吉街62巷9號1F","22222222",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", null, null, null);
		String kcode = this.uco.createSupplier(testCom);
		serviceEmailList = Arrays.asList("roger.chen@tw.drs.network","paul.lin@tw.drs.network");
		this.uco.updatePartial(new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測","New Address","87654321",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", null, null, null));
		CompanyImplForTest expect = new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測","New Address","87654321",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", "K2", false, true);
		assertEquals(expect,this.uco.getCompany(kcode));
	}
	
	@Test @Transactional
	public void testDelete() {
		List<String> serviceEmailList = Arrays.asList("test@test.com");
		List<String> productEmailList = Arrays.asList("test@test.com");
		String kcode = this.uco.createSupplier(new CompanyImplForTest("KTEST","TESTCOMPANY","測試","T.C.","測","台北市大安區延吉街62巷9號1F","22222222",serviceEmailList,null,productEmailList,"TWD","TW","87928792","第一商業銀行","大雅分行","1234456789","久祿興業股份有限公司","NOTE", null, null, null));
		this.uco.delete(kcode);
		Assert.assertEquals(null,this.uco.getCompany(kcode));
	}

	@Test
	public void testGetSupplierUserEmailList() {
		List<String> mailList = this.uco.getSupplierUserEmailList("K488");
		assertTrue(mailList.size()>=3);
		assertTrue(mailList.contains("ares.lin@nextdrive.io"));
		assertTrue(mailList.contains("linlin@nextdrive.io"));
		assertTrue(mailList.contains("peter.yang@nextdrive.io"));
	}
	
	private Company getCompanyInDtoList(String kcode){
		DtoList<Company> dtoList = this.uco.retrieveSupplierList(1);
		List<Company> list = dtoList.getItems();
		for(Company c:list){
			if(c.getKcode().equals(kcode)){
				return c;
			}
		}
		for(int i=2;i<=dtoList.getPager().getTotalPages();i++){
			dtoList = uco.retrieveSupplierList(i);
			list = dtoList.getItems();
			for(Company c:list){
				if(c.getKcode().equals(kcode)){
					return c;
				}
			}
		}
		return null;
	}

}