package com.kindminds.drs.service.usecase.report;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.report.shopify.ImportShopifyOrderUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportShopifyOrderUco {
	
	@Autowired private ImportShopifyOrderUco uco;

	@Test @Transactional
	public void testSaveTestOrder(){
		this.uco.saveTestOrder();
	}
	
	@Test @Transactional
	public void testUpdateTestOrder(){
		this.uco.updateTestOrder();
	} 
}
