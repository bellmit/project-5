package com.kindminds.drs.service.usecase.report;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSponsoredProductsSearchTermReportUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportAmazonSponsoredProductsSearchTermReport {

	@Autowired private ImportAmazonSponsoredProductsSearchTermReportUco uco;
	
	@Test @Transactional
	public void testBasicImport() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/Sponsored-Products-Search-term-report.xlsx").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String result = this.uco.importFile("1",fileBytes);
		assertEquals("99 line(s) processed",result);
	}
		
}
