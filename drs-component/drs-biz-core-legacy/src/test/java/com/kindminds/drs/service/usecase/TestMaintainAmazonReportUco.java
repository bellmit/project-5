package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSettlementReportUco;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestMaintainAmazonReportUco {
	
	@Autowired private ImportAmazonSettlementReportUco uco;
	
	@Test
	public void testGetSettlementReportInfoList(){
		List<AmazonSettlementReportInfo> infoList = this.uco.getSettlementReportInfoList();
		String specificSettlementId = "6015058681";
		Boolean containSepcificSettlementId = false;
		for(AmazonSettlementReportInfo info:infoList){
			if(info.getSettlementId().equals(specificSettlementId)) containSepcificSettlementId = true;
		}
		assertTrue(containSepcificSettlementId);
	}
}
