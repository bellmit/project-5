package com.drs.sys.core.actors.handlers.command;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.command.SendBuyBoxReminder;
import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.command.BuyBoxReminderHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestBuyBoxReminderHandler {

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
    public void testSendBuyBoxReminder() {
        new TestKit(system) {
            {
                final Props props = Props.create(BuyBoxReminderHandler.class, drsCmdBus);
                final TestActorRef<BuyBoxReminderHandler> ref = TestActorRef.create(
                        system,props,"testSendBuyBoxReminder");

                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new SendBuyBoxReminder(),
                        10000).toCompletableFuture();
            }
        };
    }
}
