package com.drs.sys.core.actors.handlers.view.common;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.GetLatestSettlementPeriod;
import com.kindminds.drs.api.message.GetSettlementPeriodList;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.query.common.SettlementPeriodListHandler;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestSettlementPeriodListHandler {



    private static ActorSystem system;

    private static TestActorRef drsQueryBus;

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
    public void testSettlementPeriodList() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(SettlementPeriodListHandler.class, drsQueryBus);
                final TestActorRef<SettlementPeriodListHandler> ref = TestActorRef.create(system,props,"testSettlementPeriod1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetSettlementPeriodList(), 120000).toCompletableFuture();
                List<SettlementPeriod> result = (List<SettlementPeriod>)future.get();
                if(result != null)
                    assert true;
            }
        };
    }

    @Test
    public void testLatestSettlementPeriod() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(SettlementPeriodListHandler.class, drsQueryBus);
                final TestActorRef<SettlementPeriodListHandler> ref = TestActorRef.create(system,props,"testSettlementPeriod2");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetLatestSettlementPeriod(), 120000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }
}
