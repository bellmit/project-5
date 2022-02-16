package com.kindminds.drs.service.usecase.report;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonMonthlyStorageFeeReportUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportAmazonMonthlyStorageFeeReportUco {
	
	@Autowired private ImportAmazonMonthlyStorageFeeReportUco uco;
	
	@Test @Transactional
	public void testImporReportUS() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-us-monthlyStorageFee-201803.tsv").getFile());		
//		File file = new File(classLoader.getResource("test-report/test-us-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String result = this.uco.importFile("101", fileBytes);
		assertEquals("10 new Line(s) inserted",result);		
	}
	
	@Test @Transactional
	public void testImporReportUSTwice() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-us-monthlyStorageFee-201803.tsv").getFile());
//		File file = new File(classLoader.getResource("test-report/test-us-monthlyStorageFee-new-column-201803.tsv").getFile());		
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String result1 = this.uco.importFile("101", fileBytes);
		assertEquals("10 new Line(s) inserted",result1);
		String result2 = this.uco.importFile("101", fileBytes);
		assertEquals("duplicated imported",result2);		
	}
		
	@Test @Transactional
	public void testImportReportUK() throws IOException, ParseException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-uk-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String result = this.uco.importFile("102", fileBytes);
		assertEquals("7 new Line(s) inserted",result);
	}
	
	@Test @Transactional
	public void testImporReportUKTwice() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/test-uk-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String result1 = this.uco.importFile("102", fileBytes);
		assertEquals("7 new Line(s) inserted",result1);
		String result2 = this.uco.importFile("102", fileBytes);
		assertEquals("duplicated imported",result2);		
	}
		
	@Test @Transactional
	public void testImportTwoDifferentFiles() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileUS = new File(classLoader.getResource("test-report/test-us-monthlyStorageFee-201803.tsv").getFile());
//		File fileUS = new File(classLoader.getResource("test-report/test-us-monthlyStorageFee-new-column-201803.tsv").getFile());
		File fileUK = new File(classLoader.getResource("test-report/test-uk-monthlyStorageFee-201803.tsv").getFile());
		byte[] fileUSBytes = Files.readAllBytes(fileUS.toPath());
		byte[] fileUKBytes = Files.readAllBytes(fileUK.toPath());
		String result1 = this.uco.importFile("101",fileUSBytes);
		assertEquals("10 new Line(s) inserted",result1);
		String result2 = this.uco.importFile("102",fileUKBytes);
		assertEquals("7 new Line(s) inserted",result2);				
	}

}