package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.inventory.UpdateProductSkuFbaInventoryAvailabilityUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestUpdateProductSkuFbaInventoryAvailabilityUco {
	
	@Autowired private UpdateProductSkuFbaInventoryAvailabilityUco uco;
	
	@Test @Transactional
	public void testUploadFileAndUpdate() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/product-sku-fba-inventory-availability.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String date = "2016-06-02";
		this.uco.uploadFileAndUpdate(date,fileBytes);
		Map<String,Boolean> updateStatus = this.uco.getUpdateStatus("2016","06");
		assertEquals(true,updateStatus.get(date));
	}
	
	@Test @Transactional
	public void testUploadFileAndUpdateWithUnmatchedDate() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-report/product-sku-fba-inventory-availability.tsv").getFile());
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String date = "2016-06-01";
		String result = this.uco.uploadFileAndUpdate(date,fileBytes);
		assertEquals("Date mismatch, 2016-06-01 and 2016-06-02.", result);
	}
	
	@Test @Transactional
	public void testClearAvailabilityData() throws IOException{
		Map<String,Boolean> status = this.uco.getUpdateStatus("2017","03");
		String date = "2017-03-05";
		assertEquals(true,status.get(date));
		status.put(date,false);
		this.uco.clearAvailabilityData(date);
		Map<String,Boolean> newStatus = this.uco.getUpdateStatus("2017","03");
		assertEquals(status,newStatus);
	}
	
	@Test
	public void testGetFutureMonthStatus() throws IOException{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.format("%02d", calendar.get(Calendar.MONTH)+1);
		Map<String,Boolean> status = this.uco.getUpdateStatus(year,month);
		for(Boolean value:status.values()){
			assertFalse(value);
		}
	}
	
}
