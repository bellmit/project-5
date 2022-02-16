package com.drs.sys.core.actors.handlers.command;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.core.DrsCmdBus;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestServiceFeeHandler {

    private static ActorSystem system;

    private static TestActorRef drsCmdBus;

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
    public void testVerifyServiceFee() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {

                //final Props props = Props.create(ServiceFeeHandler.class, drsCmdBus, springCtx);
                //final TestActorRef<ServiceFeeHandler> ref = TestActorRef.create(system,props,"testVerifyServiceFee");
//                final CompletableFuture<Object> future = PatternsCS.ask(ref,
//                        new VerifyServiceFee(null, OnboardingApplicationStatusType.VERIFIED), 10000)
//                        .toCompletableFuture();
//                String result = (String)future.get();
            }
        };
    }
}