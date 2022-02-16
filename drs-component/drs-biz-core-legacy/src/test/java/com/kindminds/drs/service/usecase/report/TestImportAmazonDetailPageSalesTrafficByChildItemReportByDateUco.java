package com.kindminds.drs.service.usecase.report;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco {
	
	@Autowired private ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco uco;
	
	@Test @Transactional
	public void testImport() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-amazon-detail-page-sales-traffic-report-by-childitem-amazon-com.csv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String date = "2016-01-02";
		this.uco.importFile("1",date,fileBytes);
		Map<String,Map<Marketplace,Boolean>> importStatus = this.uco.getImportStatus("2016","1");
		assertEquals(true, importStatus.get(date).get(Marketplace.AMAZON_COM));
	}
	
	@Test @Transactional
	public void testImportSameMarketplaceDateTwice() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-amazon-detail-page-sales-traffic-report-by-childitem-amazon-com.csv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String date = "2016-01-02";
		this.uco.importFile("1",date,fileBytes);
		Assert.assertEquals("Report from Amazon.com at 2016-01-02 has been imported.",this.uco.importFile("1",date,fileBytes));
	}
	
	@Test @Transactional
	public void testDelete() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-amazon-detail-page-sales-traffic-report-by-childitem-amazon-com.csv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String date = "2016-01-02";
		this.uco.importFile("1",date,fileBytes);
		Assert.assertEquals("13 rows has been deleted.", this.uco.delete(date,"1"));
		Map<String,Map<Marketplace,Boolean>> importStatus = this.uco.getImportStatus("2016","1");
		assertEquals(false,importStatus.get(date).get(Marketplace.AMAZON_COM));
	}
}
