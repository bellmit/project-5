package com.kindminds.drs.service.usecase.report;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonInventoryHealthReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonInventoryHealthReportImportInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportAmazonInventoryHealthReportUco {
	
	@Autowired private ImportAmazonInventoryHealthReportUco uco;
	
	@Test @Transactional
	public void testImportFileFromAmazonCom() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-amazon-inventory-health-report-from-amazon-com.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		this.uco.importReport("1",fileBytes);
		assertTrue(this.containsInInfoList(Marketplace.AMAZON_COM,"2016-11-17"));
	}
	
	@Test @Transactional
	public void testImportFileFromAmazonCoUk() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-amazon-inventory-health-report-from-amazon-co-uk.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		this.uco.importReport("4",fileBytes);
		assertTrue(this.containsInInfoList(Marketplace.AMAZON_CO_UK, "2013-11-17"));
	}
	
	@Test @Transactional
	public void testImportFileFromAmazonCa() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-amazon-inventory-health-report-from-amazon-ca.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		this.uco.importReport("5",fileBytes);
		assertTrue(this.containsInInfoList(Marketplace.AMAZON_CA, "2014-11-17"));
	}
	
	private boolean containsInInfoList(Marketplace marketplace,String snapshotDate){
		for(AmazonInventoryHealthReportImportInfo info:this.uco.getImportStatuses()){
			if(info.getMarketplace()==marketplace&&info.getSnapshotDate().equals(snapshotDate)) return true;
		}
		return false;
	}
}
