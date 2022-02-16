package com.kindminds.drs.service.usecase;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.CalculateMonthlyStorageFeeUco;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonMonthlyStorageFeeReportUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestCalculateMonthlyStorageFeeUco {
		
	@Autowired private ImportAmazonMonthlyStorageFeeReportUco importAmazonMonthlyStorageFeeReportUco;
	@Autowired private CalculateMonthlyStorageFeeUco calculateMonthlyStorageFeeUco;
	
	@Test @Transactional
	public void testCalculate() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileUS = new File(classLoader.getResource("test-report/us-monthlyStorageFee-201803.tsv").getFile());
//		File fileUS = new File(classLoader.getResource("test-report/us-monthlyStorageFee-new-column-201803.tsv").getFile());
		byte[] fileBytesUS = Files.readAllBytes(fileUS.toPath());
		String importResultUS = this.importAmazonMonthlyStorageFeeReportUco.importFile("101", fileBytesUS);
		assertEquals("2356 new Line(s) inserted",importResultUS);		
		File fileCA = new File(classLoader.getResource("test-report/ca-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytesCA = Files.readAllBytes(fileCA.toPath());
		String importResultCA = this.importAmazonMonthlyStorageFeeReportUco.importFile("103", fileBytesCA);
		assertEquals("2356 new Line(s) inserted",importResultCA);		
		File fileUK = new File(classLoader.getResource("test-report/uk-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytesUK = Files.readAllBytes(fileUK.toPath());
		String importResultUK = this.importAmazonMonthlyStorageFeeReportUco.importFile("102", fileBytesUK);
		assertEquals("720 new Line(s) inserted",importResultUK);
		String result = this.calculateMonthlyStorageFeeUco.calculate("2018","03");
		assertEquals("71 created successfully",result);
	}
	
	@Test @Transactional
	public void testCalculateWithoutData(){
		String result = this.calculateMonthlyStorageFeeUco.calculate("2017","12");
		assertEquals("0 created successfully",result);
	}
		
	@Test @Transactional
	public void testDuplicateCalculate() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileUS = new File(classLoader.getResource("test-report/us-monthlyStorageFee-201803.tsv").getFile());
//		File fileUS = new File(classLoader.getResource("test-report/us-monthlyStorageFee-new-column-201803.tsv").getFile());
		byte[] fileBytesUS = Files.readAllBytes(fileUS.toPath());
		String importResultUS = this.importAmazonMonthlyStorageFeeReportUco.importFile("101", fileBytesUS);
		assertEquals("2356 new Line(s) inserted",importResultUS);		
		File fileCA = new File(classLoader.getResource("test-report/ca-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytesCA = Files.readAllBytes(fileCA.toPath());
		String importResultCA = this.importAmazonMonthlyStorageFeeReportUco.importFile("103", fileBytesCA);
		assertEquals("2356 new Line(s) inserted",importResultCA);		
		File fileUK = new File(classLoader.getResource("test-report/uk-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytesUK = Files.readAllBytes(fileUK.toPath());
		String importResultUK = this.importAmazonMonthlyStorageFeeReportUco.importFile("102", fileBytesUK);
		assertEquals("720 new Line(s) inserted",importResultUK);
		String result1 = this.calculateMonthlyStorageFeeUco.calculate("2018","03");
		assertEquals("71 created successfully",result1);
		String result2 = this.calculateMonthlyStorageFeeUco.calculate("2018","03");
		assertEquals("No need to calculate",result2);
	}

	@Test @Transactional
	public void testcalculateSumOfTotalEstimatedMonthlyStorageFeeUS() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileUS = new File(classLoader.getResource("test-report/us-monthlyStorageFee-201803.tsv").getFile());
//		File fileUS = new File(classLoader.getResource("test-report/us-monthlyStorageFee-new-column-201803.tsv").getFile());
		byte[] fileBytesUS = Files.readAllBytes(fileUS.toPath());
		String importResultUS = this.importAmazonMonthlyStorageFeeReportUco.importFile("101", fileBytesUS);
		assertEquals("2356 new Line(s) inserted",importResultUS);		
		File fileCA = new File(classLoader.getResource("test-report/ca-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytesCA = Files.readAllBytes(fileCA.toPath());
		String importResultCA = this.importAmazonMonthlyStorageFeeReportUco.importFile("103", fileBytesCA);
		assertEquals("2356 new Line(s) inserted",importResultCA);		
		File fileUK = new File(classLoader.getResource("test-report/uk-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytesUK = Files.readAllBytes(fileUK.toPath());
		String importResultUK = this.importAmazonMonthlyStorageFeeReportUco.importFile("102", fileBytesUK);
		assertEquals("720 new Line(s) inserted",importResultUK);
		String result1 = this.calculateMonthlyStorageFeeUco.calculate("2018","03");
		assertEquals("71 created successfully",result1);
		BigDecimal sum = this.calculateMonthlyStorageFeeUco.calculateSumOfTotalEstimatedMonthlyStorageFee("K151", "US", "2018", "03");
		assertEquals(BigDecimal.valueOf(11.91),sum);				
	}
		
	@Test @Transactional
	public void testcalculateSumOfTotalEstimatedMonthlyStorageFeeCA() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();	
		File fileUS = new File(classLoader.getResource("test-report/us-monthlyStorageFee-201803.tsv").getFile());
//		File fileUS = new File(classLoader.getResource("test-report/us-monthlyStorageFee-new-column-201803.tsv").getFile());
		byte[] fileBytesUS = Files.readAllBytes(fileUS.toPath());
		String importResultUS = this.importAmazonMonthlyStorageFeeReportUco.importFile("101", fileBytesUS);
		assertEquals("2356 new Line(s) inserted",importResultUS);		
		File fileCA = new File(classLoader.getResource("test-report/ca-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytesCA = Files.readAllBytes(fileCA.toPath());
		String importResultCA = this.importAmazonMonthlyStorageFeeReportUco.importFile("103", fileBytesCA);
		assertEquals("2356 new Line(s) inserted",importResultCA);		
		File fileUK = new File(classLoader.getResource("test-report/uk-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytesUK = Files.readAllBytes(fileUK.toPath());
		String importResultUK = this.importAmazonMonthlyStorageFeeReportUco.importFile("102", fileBytesUK);
		assertEquals("720 new Line(s) inserted",importResultUK);
		String result1 = this.calculateMonthlyStorageFeeUco.calculate("2018","03");
		assertEquals("71 created successfully",result1);
		BigDecimal sum = this.calculateMonthlyStorageFeeUco.calculateSumOfTotalEstimatedMonthlyStorageFee("K510", "CA", "2018", "03");
		assertEquals(BigDecimal.valueOf(5.57),sum);				
	}
		
}