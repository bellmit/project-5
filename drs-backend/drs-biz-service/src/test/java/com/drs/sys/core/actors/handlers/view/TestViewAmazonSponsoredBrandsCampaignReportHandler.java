package com.drs.sys.core.actors.handlers.view;

import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetAmazonSponsoredBrandsCampaignReport;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetCampaignNames;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetMarketplaceNames;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetSupplierKcodeToShortEnNameMap;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.query.ViewAmazonSponsoredBrandsCampaignReportHandler;

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
public class TestViewAmazonSponsoredBrandsCampaignReportHandler {

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
    public void testCampaignNamesNoException() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewAmazonSponsoredBrandsCampaignReportHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testGetCampaignNames1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetCampaignNames("K510", 1, null, null), 120000).toCompletableFuture();
                String result = (String)future.get();
                assert(result != null);
            }
        };
    }

    @Test
    public void testCampaignNamesFilterDate() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewAmazonSponsoredBrandsCampaignReportHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testGetCampaignNames2");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetCampaignNames("K510", 1, "2018-06-01", "2018-10-01"), 120000).toCompletableFuture();
                String result = (String)future.get();
                assert(result != null);
            }
        };
    }

    @Test
    public void testGetMarketplaceNames() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewAmazonSponsoredBrandsCampaignReportHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testGetMarketplaceNames1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetMarketplaceNames(), 120000).toCompletableFuture();
                String result = (String)future.get();
                assert(result != null);
                assert(!result.isEmpty());
            }
        };
    }

    @Test
    public void testGetSupplierKcodeToShortEnNameMap() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewAmazonSponsoredBrandsCampaignReportHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testGetSupplierKcodeToShortEnNameMap1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetSupplierKcodeToShortEnNameMap(), 120000).toCompletableFuture();
                String result = (String)future.get();
                assert(result != null);
                assert(!result.isEmpty());
            }
        };
    }

    @Test
    public void testGetAmazonSponsoredBrandsCampaignReport2() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewAmazonSponsoredBrandsCampaignReportHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testGetAmazonSponsoredBrandsCampaignReport1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetAmazonSponsoredBrandsCampaignReport("K510", 1, "2018-12-05", "2019-01-05",null), 120000).toCompletableFuture();
                String result = (String)future.get();
                assert(result != null);
                assert(!result.isEmpty());
            }
        };
    }

    @Test
    public void testGetAmazonSponsoredBrandsCampaignReportFilterCampaign() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewAmazonSponsoredBrandsCampaignReportHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testGetAmazonSponsoredBrandsCampaignReport2");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetAmazonSponsoredBrandsCampaignReport("K520", 4, null, null,"[K520 Tunai] Headline ads"), 120000).toCompletableFuture();
                String result = (String)future.get();
                assert(result != null);
                assert(!result.isEmpty());
            }
        };
    }

    @Test
    public void testGetAmazonSponsoredBrandsCampaignReport() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewAmazonSponsoredBrandsCampaignReportHandler.class, drsQueryBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testGetAmazonSponsoredBrandsCampaignReport1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetAmazonSponsoredBrandsCampaignReport("K520", 1, null, null,null), 120000).toCompletableFuture();
                String result = (String)future.get();
                assert(result != null);
                assert(!result.isEmpty());
            }
        };
    }
}
