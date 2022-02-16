package com.drs.sys.core.actors.handlers.view;

import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.coupon.GetFailedCouponRedemptions;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.query.ProcessCouponRedemptionHandler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestProcessCouponRedemptionHandler {


    private static TestActorRef drsQueryBus;

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
        drsQueryBus = TestActorRef.create(system, Props.create(DrsQueryBus.class),
                "DrsQueryBus");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testGetFailedCouponRedemptions() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ProcessCouponRedemptionHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testGetFailedCouponRedemptions1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetFailedCouponRedemptions(), 120000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }
}
