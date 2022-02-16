package com.drs.sys.core.actors.handlers.command;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.coupon.ProcessCouponRedemption;
import com.kindminds.drs.api.message.coupon.ProcessCouponRedemptionBySettlementPeriod;
import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.core.actors.handlers.command.ProcessCouponRedemptionHandler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestProcessCouponRedemptionHandler {


    private static TestActorRef drsCmdBus;

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
        drsCmdBus = TestActorRef.create(system, Props.create(DrsCmdBus.class),
                "DrsCmdBus");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testCouponRedemption() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ProcessCouponRedemptionHandler.class, drsCmdBus);
                final TestActorRef<ProcessCouponRedemptionHandler> ref = TestActorRef.create(system,props,"testProcessCouponRedemption1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new ProcessCouponRedemptionBySettlementPeriod(70), 120000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }

    @Test
    public void testCouponRedemptionLatest() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ProcessCouponRedemptionHandler.class, drsCmdBus);
                final TestActorRef<ProcessCouponRedemptionHandler> ref = TestActorRef.create(system,props,"testProcessCouponRedemption2");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new ProcessCouponRedemption(), 120000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }
}
