package com.drs.sys.service.control;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.AddProductSkuAsinUco;
import com.kindminds.drs.api.v1.model.product.SkuFnskuAsin;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestAddProductSkuAsinUco {
	
	@Autowired private AddProductSkuAsinUco uco;
	
	@Test
	public void testGetSkuToAsin(){
		List<SkuFnskuAsin> skuToAsin = this.uco.getSkuToAsin("1", "K510");
		assertEquals(19, skuToAsin.size());
		assertEquals("B01M3TIXHH",skuToAsin.get(0).getAsin());
		assertEquals("B07DRCVF6P",skuToAsin.get(1).getAsin());
		assertEquals("B01M68UK38",skuToAsin.get(2).getAsin());
	}
	
	@Test @Transactional
	public void testImportFBAReportUK() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();	
		File file = new File(classLoader.getResource("test-report/testSkuAsin_20180809_ManageFBAInventory_UK.txt").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String result = this.uco.addFbaData(fileBytes, 4);
		//System.out.//println(result);
		assertEquals("8 record(s) inserted. 257 ASIN(s) updated.",result);
	}
	
	@Test @Transactional
	public void testImportFBAReportCA() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();	
		File file = new File(classLoader.getResource("test-report/testSkuAsin_20180809_ManageFBAInventory_CA.txt").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String result = this.uco.addFbaData(fileBytes, 5);
		//System.out.//println(result);
		assertEquals("0 record(s) inserted. 52 ASIN(s) updated.",result);		
	}
	
	@Test @Transactional
	public void testImportInventoryReportCA() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();	
		File file = new File(classLoader.getResource("test-report/testSkuAsin_Inventory+Report+08-09-2018_CA.txt").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String result = this.uco.addInventoryData(fileBytes, 5);
		assertEquals("0 record(s) inserted. ",result);		
	}
	
}