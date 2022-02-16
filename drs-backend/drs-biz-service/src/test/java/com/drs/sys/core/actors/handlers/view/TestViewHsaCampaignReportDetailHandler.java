package com.drs.sys.core.actors.handlers.view;

import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetHsaCampaignReportDetail;
import com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetHsaCampaignReportDetailBySettlementPeriod;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.query.ViewHsaCampaignReportDetailHandler;

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
public class TestViewHsaCampaignReportDetailHandler {



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
    public void testGetHsaCampaignReportDetail() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewHsaCampaignReportDetailHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testViewHsaCampaignReportDetail1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetHsaCampaignReportDetail(), 12000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }

    @Test
    public void testGetHsaCampaignReportDetailBySettlementPeriod() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewHsaCampaignReportDetailHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testViewHsaCampaignReportDetail2");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetHsaCampaignReportDetailBySettlementPeriod(87), 12000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }
}