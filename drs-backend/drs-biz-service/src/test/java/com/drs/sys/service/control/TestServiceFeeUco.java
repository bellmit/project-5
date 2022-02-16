package com.drs.sys.service.control;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.usecase.product.ServiceFeeUco;
import com.kindminds.drs.core.DrsCmdBus;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestServiceFeeUco {
	
	@Autowired private ServiceFeeUco uco;


	private static ActorSystem system;

	private static ActorRef drsCmdBus;

//	@BeforeClass
//	public static void setup() {
//		system = ActorSystem.create();
//		drsCmdBus = system.actorOf(Props.create(DrsCmdBus.class),
//				"DrsCmdBus");
//	}
//
//	@AfterClass
//	public static void teardown() {
//		TestKit.shutdownActorSystem(system);
//		system = null;
//	}

	@Test
	public void testServiceFeeQuotationSend() {
		uco.sendServiceFeeQuotation("robert.lee@drs.network", "K510");
	}
	
}
