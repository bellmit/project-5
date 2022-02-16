package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.GetSs2spAdvertisingCostReport;
import com.kindminds.drs.api.message.GetSs2spMarketingActivityExpenseReport;
import com.kindminds.drs.api.message.GetSs2spOtherRefundReport;
import com.kindminds.drs.core.DrsQueryBus;

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
public class TestViewSs2spStatementHandler {


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
    public void testGetSs2spAdvertisingCostReport() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewSs2spStatementHandler.class, drsQueryBus);
                final TestActorRef<ViewSs2spStatementHandler> ref = TestActorRef.create(system,props,"testGetSs2spAdvertisingCostReport1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetSs2spAdvertisingCostReport("STM-K600-8", "US"), 120000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }

    @Test
    public void testGetSs2spOtherRefundReport() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewSs2spStatementHandler.class, drsQueryBus);
                final TestActorRef<ViewSs2spStatementHandler> ref = TestActorRef.create(system,props,"testGetSs2spOtherRefundReport1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetSs2spOtherRefundReport("STM-K600-8", "US"), 120000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }

    @Test
    public void testGetSs2spMarketingActivityExpenseReport() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewSs2spStatementHandler.class, drsQueryBus);
                final TestActorRef<ViewSs2spStatementHandler> ref = TestActorRef.create(system,props,"testGetSs2spMarketingActivityExpenseReport1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetSs2spMarketingActivityExpenseReport("STM-K600-8", "US"), 120000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }
}
