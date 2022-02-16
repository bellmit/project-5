package com.drs.sys.core.actors.handlers.command;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.command.CreateMarketingActivity;
import com.kindminds.drs.api.message.command.DeleteMarketingActivity;
import com.kindminds.drs.api.message.command.UpdateMarketingActivity;
import com.kindminds.drs.api.message.query.marketing.*;
import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.command.marketing.MarketingActivityCmdHandler;
import com.kindminds.drs.core.actors.handlers.query.marketing.MarketingActivityQueryHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMarketingActivityHandler {

    private static TestActorRef drsQueryBus;
    private static TestActorRef drsCmdBus;

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
        drsQueryBus = TestActorRef.create(system, Props.create(DrsQueryBus.class),
                "DrsQueryBus");

        drsCmdBus = TestActorRef.create(system, Props.create(DrsCmdBus.class),
                "DrsCmdBus");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testAkkaGetMarketingActivityList() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                Date startDate = null, endDate = null;

                try {
                    startDate = new SimpleDateFormat("yyyy-MM-dd").parse("1950-02-05");
                    endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-09-20");
                    //System.out.//println("start Date: " + startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final Props props = Props.create(MarketingActivityQueryHandler.class, drsQueryBus);
                final TestActorRef<MarketingActivityQueryHandler> ref = TestActorRef.create(system,props,"testGetMarketingActivityList");
                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new GetMarketingActivityByFilters("US", "K520", startDate, endDate, 1),
                        10000).toCompletableFuture();
                String result = (String)future.get();
                assertNotNull(result);
                //System.out.//println(result);
            }
        };
    }

    @Test
    public void testInvalidDateFilters() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                Date startDate = null, endDate = null;

                try {
                    startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-09-20");
                    endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-05-20");
                    //System.out.//println("start Date: " + startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final Props props = Props.create(MarketingActivityQueryHandler.class, drsQueryBus);
                final TestActorRef<MarketingActivityQueryHandler> ref = TestActorRef.create(system,props,"testInvalidDateFilters");
                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new GetMarketingActivityByFilters("", "", startDate, endDate, 1),
                        10000).toCompletableFuture();
                String result = (String)future.get();
                assertNotNull(result);
                //System.out.//println(result);
            }
        };
    }

    @Test
    public void testAkkaCreateMarketingActivity() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                String activityJson = "{\"country\":\"BLA\",\"code\":\"\",\"name\":\"\"," +
                        "\"doneBy\":\"Robert\",\"startDate\":\"2018-08-12T16:00:00.000Z\",\"endDate\":\"\"," +
                        "\"platform\":\"\",\"activity\":\"\",\"initialAmount\":\"\",\"budgetFinalAmount\":\"\"," +
                        "\"discount\":\"\",\"unitOfMeasure\":\"\",\"details\":\"\"," +
                        "\"link1\":\"TEST\",\"link2\":\"\",\"link3\":\"\",\"link4\":\"\",\"link5\":\"\",\"link6\":\"\"," +
                        "\"link7\":\"\",\"link8\":\"\",\"link9\":\"\",\"link10\":\"\"," +
                        "\"considerationsProblemToSolve\":\"\",\"originalText\":\"CREATETEST\",\"newText\":\"\",\"suggestionsForSupplier\":\"\"}";
                final Props props = Props.create(MarketingActivityCmdHandler.class, drsCmdBus);
                final TestActorRef<MarketingActivityCmdHandler> ref = TestActorRef.create(system,props,"testCreateMarketingActivity");
                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new CreateMarketingActivity(activityJson),
                        10000).toCompletableFuture();
                Object result = future.get();
                assertNotNull(result);
                //System.out.//println("result: " + result);
            }
        };
    }

    @Test
    public void testUpdateMarketingActivity() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                String activityJson = "{\"_id\":\"5d1c74a3ccc6c7547210dd86\",\"country\":\"UK\"," +
                        "\"code\":\"zcvx\",\"name\":\"TUNAI Bluetooth Button\"," +
                        "\"doneBy\":\"Rob\",\"startDate\":\"2019-05-12T19:36:20.000Z\"," +
                        "\"endDate\":\"\",\"platform\":\"Amazon\"," +
                        "\"activity\":\"Amz sponsored ad\",\"initialAmount\":\"\"," +
                        "\"budgetFinalAmount\":\"\",\"discount\":\"\",\"unitOfMeasure\":\"\"," +
                        "\"details\":\"Auto: Increased bid for Substitutes \",\"link1\":\"\"," +
                        "\"link2\":\"\",\"link3\":\"\",\"link4\":\"\",\"link5\":\"\"," +
                        "\"link6\":\"\",\"link7\":\"\",\"link8\":\"\",\"link9\":\"\"," +
                        "\"link10\":\"\",\"considerationsProblemToSolve\":\"\"," +
                        "\"originalText\":\"ANOTHER\",\"newText\":\"TESTING\"," +
                        "\"suggestionsForSupplier\":\"\"}";
                final Props props = Props.create(MarketingActivityCmdHandler.class, drsCmdBus);
                final TestActorRef<MarketingActivityCmdHandler> ref = TestActorRef.create(system,props,"testUpdateMarketingActivity");
                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new UpdateMarketingActivity(activityJson),
                        10000).toCompletableFuture();
                Object result = future.get();
                assertNotNull(result);
                //System.out.//println("result: " + result);
            }
        };
    }

    @Test
    public void testDeleteMarketingActivity() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                String activityId = "5cff6e63cbc5856e25e7aa64";
                final Props props = Props.create(MarketingActivityCmdHandler.class, drsCmdBus);
                final TestActorRef<MarketingActivityCmdHandler> ref = TestActorRef.create(system,props,"testDeleteMarketingActivity");
                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new DeleteMarketingActivity(activityId),
                        10000).toCompletableFuture();
                Object result = future.get();
                assertNotNull(result);
                //System.out.//println("result: " + result);
            }
        };
    }

    @Test
    public void testFindActivityById() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                String activityId = "5d1c6eb37d5f4a6db4977402";
                final Props props = Props.create(MarketingActivityQueryHandler.class, drsQueryBus);
                final TestActorRef<MarketingActivityQueryHandler> ref = TestActorRef.create(system,props,"testFindActivityById");
                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new GetMarketingActivityById(activityId),
                        10000).toCompletableFuture();
                Object result = future.get();
                assertNotNull(result);
                //System.out.//println("result: " + result);
            }
        };
    }
}
