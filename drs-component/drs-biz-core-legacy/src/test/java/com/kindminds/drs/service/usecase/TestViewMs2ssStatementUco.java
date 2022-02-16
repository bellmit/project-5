package com.kindminds.drs.service.usecase;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kindminds.drs.api.usecase.accounting.ViewMs2ssStatementUco;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport.Ms2ssSettleableItemReportLineItem;
import com.kindminds.drs.service.test.dto.statement.Ms2ssSettleableItemReportImpl;
import com.kindminds.drs.service.test.dto.statement.Ms2ssSettleableItemReportLineItemImpl;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestViewMs2ssStatementUco {
	
	@Autowired private ViewMs2ssStatementUco uco;
	
	@Test
	public void testGetSettleableItemReportPaymentOnbehalf(){
		Ms2ssSettleableItemReportImpl report = new Ms2ssSettleableItemReportImpl("SSDC payment on behalf of MSDC","2015-12-27","2016-01-09","K3","K2","USD","135.85", new ArrayList<Ms2ssSettleableItemReportLineItem>());
		report.getItemList().add(new Ms2ssSettleableItemReportLineItemImpl("2015-12-30 16:00:00",null,null,"SSDC Payment On Behalf Of MSDC: Import Duty",null,"UNS-K2-6","USD","38.050000","38.050000"));
		report.getItemList().add(new Ms2ssSettleableItemReportLineItemImpl("2015-12-30 16:00:00",null,null,"SSDC Payment On Behalf Of MSDC: Import Duty",null,"UNS-K2-7","USD","97.800000","97.800000"));
		assertEquals(report,this.uco.querySettleableItemReportForPaymentOnBehalf(BillStatementType.OFFICIAL,"STM-K3-K2-16"));
	}

}