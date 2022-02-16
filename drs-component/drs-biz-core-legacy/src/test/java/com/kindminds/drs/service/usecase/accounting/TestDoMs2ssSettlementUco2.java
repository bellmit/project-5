package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.api.usecase.accounting.DoMs2ssSettlementUco;
import com.kindminds.drs.util.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestDoMs2ssSettlementUco2 {
	
	@Autowired private DoMs2ssSettlementUco uco;


	@Test @Transactional
	public void doDraft(){

		String stmntStart = "2019-04-28";
		String stmntEnd = "2019-05-11";

		System.out.println("K2-1");
		 this.uco.createDraft("K2",
				"K3",stmntStart,stmntEnd);
		System.out.println("K2-2");
		this.uco.createDraft("K2",
				"K4",stmntStart,stmntEnd);
		System.out.println("K2-3");
		this.uco.createDraft("K2",
				"K5",stmntStart,stmntEnd);
		System.out.println("K2-4");
		this.uco.createDraft("K2",
				"K6",stmntStart,stmntEnd);
		System.out.println("K2-5");
		this.uco.createDraft("K2",
				"K7",stmntStart,stmntEnd);
		System.out.println("K2-6");
		this.uco.createDraft("K2",
				"K8",stmntStart,stmntEnd);
		System.out.println("K2-7");

		//this.uco.createDraft("K2","K9",stmntStart,stmntEnd);


		System.out.println("K3-1");
		//this.uco.createDraft("K3", "K2",stmntStart,stmntEnd);
		System.out.println("K3-2");
		this.uco.createDraft("K3",
				"K4",stmntStart,stmntEnd);
		System.out.println("K3-3");
		this.uco.createDraft("K3",
				"K5",stmntStart,stmntEnd);
		System.out.println("K3-4");
		this.uco.createDraft("K3",
				"K6",stmntStart,stmntEnd);
		System.out.println("K3-5");
		this.uco.createDraft("K3",
				"K7",stmntStart,stmntEnd);
		System.out.println("K3-6");
		this.uco.createDraft("K3",
				"K8",stmntStart,stmntEnd);
		System.out.println("K3-7");
		this.uco.createDraft("K3",
				"K9",stmntStart,stmntEnd);

		System.out.println("K4-1");
		//this.uco.createDraft("K4", "K2",stmntStart,stmntEnd);
		System.out.println("K4-2");
		this.uco.createDraft("K4",
				"K3",stmntStart,stmntEnd);
		System.out.println("K4-3");
		this.uco.createDraft("K4",
				"K5",stmntStart,stmntEnd);
		System.out.println("K4-4");
		this.uco.createDraft("K4",
				"K6",stmntStart,stmntEnd);
		System.out.println("K4-5");
		this.uco.createDraft("K4",
				"K7",stmntStart,stmntEnd);
		System.out.println("K4-6");
		this.uco.createDraft("K4",
				"K8",stmntStart,stmntEnd);
		System.out.println("K4-7");
		this.uco.createDraft("K4",
				"K9",stmntStart,stmntEnd);

		System.out.println("K5-1");
		//this.uco.createDraft("K5","K2",stmntStart,stmntEnd);
		System.out.println("K5-2");
		this.uco.createDraft("K5",
				"K3",stmntStart,stmntEnd);
		System.out.println("K5-3");
		this.uco.createDraft("K5",
				"K4",stmntStart,stmntEnd);
		System.out.println("K5-4");
		this.uco.createDraft("K5",
				"K6",stmntStart,stmntEnd);
		System.out.println("K5-5");
		this.uco.createDraft("K5",
				"K7",stmntStart,stmntEnd);
		System.out.println("K5-6");
		this.uco.createDraft("K5",
				"K8",stmntStart,stmntEnd);
		System.out.println("K5-7");
		this.uco.createDraft("K5",
				"K9",stmntStart,stmntEnd);

		System.out.println("K6-1");
		//this.uco.createDraft("K6", "K2",stmntStart,stmntEnd);
		System.out.println("K6-2");
		this.uco.createDraft("K6",
				"K3",stmntStart,stmntEnd);
		System.out.println("K6-3");
		this.uco.createDraft("K6",
				"K4",stmntStart,stmntEnd);
		System.out.println("K6-4");
		this.uco.createDraft("K6",
				"K5",stmntStart,stmntEnd);
		System.out.println("K6-5");
		this.uco.createDraft("K6",
				"K7",stmntStart,stmntEnd);
		System.out.println("K6-6");
		this.uco.createDraft("K6",
				"K8",stmntStart,stmntEnd);
		System.out.println("K6-7");
		this.uco.createDraft("K6",
				"K9",stmntStart,stmntEnd);


		System.out.println("K7-1");
		//this.uco.createDraft("K7", "K2",stmntStart,stmntEnd);

		System.out.println("K7-2");
		this.uco.createDraft("K7",
				"K3",stmntStart,stmntEnd);
		System.out.println("K7-3");
		this.uco.createDraft("K7",
				"K4",stmntStart,stmntEnd);
		System.out.println("K7-4");
		this.uco.createDraft("K7",
				"K5",stmntStart,stmntEnd);
		System.out.println("K7-5");
		this.uco.createDraft("K7",
				"K6",stmntStart,stmntEnd);
		System.out.println("K7-6");
		this.uco.createDraft("K7",
				"K8",stmntStart,stmntEnd);
		System.out.println("K7-7");
		this.uco.createDraft("K7",
				"K9",stmntStart,stmntEnd);


		System.out.println("K8-1");
		//this.uco.createDraft("K8", "K2",stmntStart,stmntEnd);
		System.out.println("K8-2");
		this.uco.createDraft("K8",
				"K3",stmntStart,stmntEnd);
		System.out.println("K8-3");
		this.uco.createDraft("K8",
				"K4",stmntStart,stmntEnd);
		System.out.println("K8-4");
		this.uco.createDraft("K8",
				"K5",stmntStart,stmntEnd);
		System.out.println("K8-5");
		this.uco.createDraft("K8",
				"K6",stmntStart,stmntEnd);

		System.out.println("K8-6");
		this.uco.createDraft("K8",
				"K7",stmntStart,stmntEnd);
		System.out.println("K8-7");
		this.uco.createDraft("K8",
				"K9",stmntStart,stmntEnd);


		System.out.println("K9-1");
		//this.uco.createDraft("K8", "K2",stmntStart,stmntEnd);
		System.out.println("K9-2");
		this.uco.createDraft("K8",
				"K3",stmntStart,stmntEnd);
		System.out.println("K9-3");
		this.uco.createDraft("K8",
				"K4",stmntStart,stmntEnd);
		System.out.println("K9-4");
		this.uco.createDraft("K8",
				"K5",stmntStart,stmntEnd);
		System.out.println("K9-5");
		this.uco.createDraft("K8",
				"K6",stmntStart,stmntEnd);
		System.out.println("K9-6");
		this.uco.createDraft("K8",
				"K7",stmntStart,stmntEnd);
		System.out.println("K9-7");
		this.uco.createDraft("K8",
				"K9",stmntStart,stmntEnd);




	}
	


	
	private Date toDate(String dateStr) {
		return DateHelper.toDate(dateStr, "yyyy-MM-dd HH:mm:ss Z");
	}
}
