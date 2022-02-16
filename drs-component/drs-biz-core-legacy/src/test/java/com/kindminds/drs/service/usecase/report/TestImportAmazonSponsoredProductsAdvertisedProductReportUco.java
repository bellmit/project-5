package com.kindminds.drs.service.usecase.report;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSponsoredProductsAdvertisedProductReportUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportAmazonSponsoredProductsAdvertisedProductReportUco {
	
	@Autowired private ImportAmazonSponsoredProductsAdvertisedProductReportUco uco;

	@Test @Transactional
	public void testImport() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-sponsored-products-advertised-product-report-20130101.xlsx").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		this.uco.importFile("1",fileBytes);
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-01-01", "2013-01-05", "K452-1A").getItems().size());
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-01-01", "2013-01-05", "K452-1B").getItems().size());
	}
	
	@Test @Transactional
	public void testImportSameFileTwice() throws IOException {		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-sponsored-products-advertised-product-report-20130101.xlsx").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		this.uco.importFile("1",fileBytes);
		this.uco.importFile("1",fileBytes);
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-01-01", "2013-01-05", "K452-1A").getItems().size());
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-01-01", "2013-01-05", "K452-1B").getItems().size());
	}
	
	@Test @Transactional
	public void testImportTwoDifferentFiles() throws IOException {		
		ClassLoader classLoader = getClass().getClassLoader();
		File file1 = new File(classLoader.getResource("test-report/test-sponsored-products-advertised-product-report-20130101.xlsx").getFile());
		File file2 = new File(classLoader.getResource("test-report/test-sponsored-products-advertised-product-report-20130201.xlsx").getFile());
		byte[] fileBytes1 = Files.readAllBytes(file1.toPath());
		byte[] fileBytes2 = Files.readAllBytes(file2.toPath());
		this.uco.importFile("1",fileBytes1);
		this.uco.importFile("1",fileBytes2);
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-01-01", "2013-01-05", "K452-1A").getItems().size());
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-02-01", "2013-02-05", "K452-1A").getItems().size());
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-01-01", "2013-01-05", "K452-1B").getItems().size());
	}

	@Test @Transactional
	public void testImport20190103() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/Campaign_US_20190103.xlsx").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		this.uco.importFile("1",fileBytes);
	}
	
}
