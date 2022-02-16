package com.kindminds.drs.service.usecase.report;

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

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonCampaignPerformanceReportUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportAmazonCampaignPerformanceReportUco {
	
	@Autowired private ImportAmazonCampaignPerformanceReportUco uco;
	
	@Test @Transactional
	public void testImport() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/campaign-performance-report-2013-11-17.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		this.uco.importFile("1",fileBytes);
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-11-17", "2013-11-18", "K452-1A").getItems().size());
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-11-17", "2013-11-18", "K452-1B").getItems().size());
	}
	
	@Test @Transactional
	public void testImportSameFileTwice() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/campaign-performance-report-2013-11-17.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		this.uco.importFile("1",fileBytes);
		this.uco.importFile("1",fileBytes);
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-11-17", "2013-11-18", "K452-1A").getItems().size());
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-11-17", "2013-11-18", "K452-1B").getItems().size());
	}
	
	@Test @Transactional
	public void testImportTwoDifferentFiles() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file1 = new File(classLoader.getResource("test-report/campaign-performance-report-2013-11-17.tsv").getFile());
		File file2 = new File(classLoader.getResource("test-report/campaign-performance-report-2013-12-18.tsv").getFile());
		byte[] file1Bytes = Files.readAllBytes(file1.toPath());
		byte[] file2Bytes = Files.readAllBytes(file2.toPath());
		this.uco.importFile("1",file1Bytes);
		this.uco.importFile("1",file2Bytes);
		Assert.assertEquals(8,this.uco.getBriefLineItem(1,"1","2013-11-17", "2013-11-19", "K452-1A").getItems().size());
		Assert.assertEquals(4,this.uco.getBriefLineItem(1,"1","2013-11-17", "2013-11-19", "K452-1B").getItems().size());
	}
}
