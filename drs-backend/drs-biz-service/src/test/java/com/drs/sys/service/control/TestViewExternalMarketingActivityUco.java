package com.drs.sys.service.control;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.viewExternalMarketingActivityUco.GetListOfSkus;
import com.kindminds.drs.api.message.viewExternalMarketingActivityUco.GetMarketingData;
import com.kindminds.drs.api.usecase.ViewExternalMarketingActivityUco;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.query.ViewExternalMarketingActivityHandler;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingData;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestViewExternalMarketingActivityUco {

    private static TestActorRef drsQueryBus;

    private static ActorSystem system;

    @Autowired private ViewExternalMarketingActivityUco uco;

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
    public void testGetListOfSkus() {
        assertTrue(uco.getListOfSkus().size() > 1260);
    }

    @Test
    public void testGetMarketingData() {
        ExternalMarketingData data = uco.getMarketingData("K510","K510-85U20001R0", "US");
        assertEquals("K510", data.getKCode());
        assertEquals("K510-85U20001R0", data.getSkuCode());
        assertEquals("US", data.getMarketplace());
        assertNotNull(data.getSalesData());
        assertNotNull(data.getActivityData());
    }

    @Test
    public void testGetMarketingDataEmpty() {
        ExternalMarketingData data = uco.getMarketingData("K520","K510-85U20001R0XXXXX", "US");
        assertEquals("K520", data.getKCode());
        assertEquals("K510-85U20001R0XXXXX", data.getSkuCode());
        assertEquals("US", data.getMarketplace());
        assertNotNull(data.getSalesData());
        assertTrue(data.getSalesData().isEmpty());
        assertNotNull(data.getActivityData());
        assertTrue(data.getActivityData().isEmpty());
    }

    @Test
    public void testAkkaGetListOfSkus() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewExternalMarketingActivityHandler.class, drsQueryBus);
                final TestActorRef<ViewExternalMarketingActivityHandler> ref = TestActorRef.create(system,props,"testGetListOfSkus");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new GetListOfSkus(), 10000).toCompletableFuture();
                String result = (String)future.get();
                assertNotNull(result);
                //System.out.//println(result);
            }
        };
    }

    @Test
    public void testAkkaGetMarketingDataK510() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewExternalMarketingActivityHandler.class, drsQueryBus);
                final TestActorRef<ViewExternalMarketingActivityHandler> ref = TestActorRef.create(system,props,"testGetMarketingDataK510");
                final CompletableFuture<Object> future = PatternsCS
                        .ask(ref, new GetMarketingData("K510","K510-85U20001R0", "US"), 10000)
                        .toCompletableFuture();
                String result = (String)future.get();
                assertNotNull(result);
                //System.out.//println(result);
            }
        };
    }

    @Test
    public void testAkkaGetMarketingDataK520() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ViewExternalMarketingActivityHandler.class, drsQueryBus);
                final TestActorRef<ViewExternalMarketingActivityHandler> ref = TestActorRef.create(system,props,"testGetMarketingDataK520");
                final CompletableFuture<Object> future = PatternsCS
                        .ask(ref, new GetMarketingData("K520","K520-TF002C-SC-1AB", "US"), 10000)
                        .toCompletableFuture();
                String result = (String)future.get();
                assertNotNull(result);
                //System.out.//println(result);
            }
        };
    }
}
