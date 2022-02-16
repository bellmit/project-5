package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.api.usecase.accounting.DoSs2spSettlementUco;
import com.kindminds.drs.util.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestDoSs2spSettlementUco2 {
	
	@Autowired private DoSs2spSettlementUco uco;


	
	@Test @Transactional("transactionManager")
	public void testCreateDraftStatement(){

		
		String message = this.uco.createDraftForAllSupplier("2019-04-28",
				"2019-05-11");
		System.out.println(message);
	}
	


	private Date toDate(String dateStr) {
		return DateHelper.toDate(dateStr, "yyyy-MM-dd HH:mm:ss Z");
	}

}
