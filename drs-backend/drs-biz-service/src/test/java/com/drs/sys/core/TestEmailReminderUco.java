package com.drs.sys.core;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;

import com.kindminds.drs.api.message.emailReminderUco.AutomateSendLTSFReminder;
import com.kindminds.drs.api.message.emailReminderUco.SendLTSFReminderToCurrentUser;
import com.kindminds.drs.api.message.emailReminderUco.UpdateFeeToSendReminder;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.core.actors.handlers.command.EmailReminderHandler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kindminds.drs.api.usecase.EmailReminderUco;
import com.kindminds.drs.api.v1.model.accounting.SupplierLongTermStorageFee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestEmailReminderUco {
	
	@Autowired private EmailReminderUco uco;

	private static ActorSystem system;

	private static ActorRef drsCmdBus;

	@Autowired private AuthenticationManager authenticationManager;

	@BeforeClass
	public static void setup() {
		system = ActorSystem.create();
		drsCmdBus = system.actorOf(Props.create(DrsCmdBus.class),
				"DrsCmdBus");
	}

	@AfterClass
	public static void teardown() {
		TestKit.shutdownActorSystem(system);
		system = null;
	}

	private List<String> mapKeysToList(Map<String,String> suppliers) {
		return new ArrayList<String>(suppliers.keySet());
	}
	
	private String ListToString(List<String> kCodeList) {
		StringBuilder kCodes = new StringBuilder();
		String prefix = "";
		for (String kCode : kCodeList) {
			kCodes.append(prefix);
			prefix = ",";
			kCodes.append(kCode);
		}
		return kCodes.toString();
	}
	
	@Test
	public void testEmptyGetExcludedSuppliers() {
		new TestKit(system) {
			{
				List<String> kCodes = mapKeysToList(uco.getExcludedSuppliers());
				uco.updateLongTermStorageReminder(ListToString(kCodes));
				List<String> results = mapKeysToList(uco.getExcludedSuppliers());
				assertTrue(results.isEmpty());
//				MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
				final Props props = Props.create(EmailReminderHandler.class ,
						() -> new EmailReminderHandler(drsCmdBus ) );
				final TestActorRef<EmailReminderHandler> ref = TestActorRef.create(system,props,"testA");
				final CompletableFuture<Object> future1 = PatternsCS.ask(ref, new UpdateFeeToSendReminder(100.0), 120000).toCompletableFuture();
				//System.out.//println(uco.getFeeToSendReminder());
				assertEquals(100.0, uco.getFeeToSendReminder(), 0.001);
				assert future1.isDone();
			}
		};
	}
	
//	@Test
//	public void testEmptyGetIncludedSuppliers() {
//		List<String> kcodes = mapKeysToList(uco.getIncludedSuppliers());
//		uco.updateLongTermStorageReminder(ListToString(kcodes));
//		List<String> results = mapKeysToList(uco.getIncludedSuppliers());
//		assertTrue(results.isEmpty());
//	}
//
//	@Test
//	public void testGetIncludedSuppliers() {
//		List<String> kcodes = mapKeysToList(uco.getIncludedSuppliers());
//		uco.updateLongTermStorageReminder(ListToString(kcodes));
//		kcodes = new ArrayList<String>();
//		kcodes.add("K520");
//		uco.updateLongTermStorageReminder(ListToString(kcodes));
//		List<String> results = mapKeysToList(uco.getIncludedSuppliers());
//		assertEquals(1, results.size());
//		assertEquals("K520", results.get(0));
//	}
//
//	@Test
//	public void testGetExcludedSuppliers() {
//		List<String> kcodes = mapKeysToList(uco.getExcludedSuppliers());
//		uco.updateLongTermStorageReminder(ListToString(kcodes));
//		List<String> testList = new ArrayList<String>();
//		testList.add("K510");
//		uco.updateLongTermStorageReminder(ListToString(testList));
//		List<String> results = mapKeysToList(uco.getExcludedSuppliers());
//		assertEquals(1, results.size());
//		assertEquals("K510", results.get(0));
//	}
//
//	@Test
//	public void testQueryFeeToSendReminder() {
//		uco.updateFeeToSendReminder(-100.0);
//		Double fee = uco.getFeeToSendReminder();
//		assertEquals((Double)(-100.0), fee);
//		uco.updateFeeToSendReminder(50.0);
//		fee = uco.getFeeToSendReminder();
//		assertEquals((Double)50.0, fee);
//	}
//
//	@Test
//	public void testEmptyUpdateLongTermStorageReminder() {
//		List<String> included1 = mapKeysToList(uco.getIncludedSuppliers());
//		List<String> excluded1 = mapKeysToList(uco.getIncludedSuppliers());
//		List<String> kcodes = new ArrayList<String>();
//		uco.updateLongTermStorageReminder(ListToString(kcodes));
//		List<String> included2 = mapKeysToList(uco.getIncludedSuppliers());
//		List<String> excluded2 = mapKeysToList(uco.getIncludedSuppliers());
//		assertEquals(included1.size(), included2.size());
//		assertEquals(excluded1.size(), excluded2.size());
//	}
//
//	@Test
//	public void testEmptyGetSuppliersFeeOverLimit() {
//		List<String> kCodes = new ArrayList<String>();
//		assertTrue(uco.getSuppliersOverFeeLimit(ListToString(kCodes)).isEmpty());
//	}
//
//	@Test
//	public void testGetSuppliersFeeOverLimit() {
//		uco.updateFeeToSendReminder(50.0);
//		BigDecimal limit = BigDecimal.valueOf(uco.getFeeToSendReminder());
//		List<String> kCodes = mapKeysToList(uco.getIncludedSuppliers());
//		for (SupplierLongTermStorageFee result : uco.getSuppliersOverFeeLimit(ListToString(kCodes))) {
//			assertTrue(result.getKcode() != null);
//			assertTrue(result.getMarketplace() != null);
//			assertTrue(result.getCurrency() != null);
//			assertTrue(result.getOneYearStorageFee().add(result.getSixMonthStorageFee()).compareTo(limit) >= 0);
//		}
//	}
	
}
