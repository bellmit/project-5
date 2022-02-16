package com.drs.sys.core.actors.handlers.command;

import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.ProcessAdSpendTransaction;
import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.core.actors.handlers.command.ProcessAdSpendTransactionHandler;

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
public class TestProcessAdSpendTransactionHandler {

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
    public void testAdSpendTransaction() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                final Props props = Props.create(ProcessAdSpendTransactionHandler.class, drsCmdBus);
                final TestActorRef<Actor> ref = (TestActorRef<Actor>) TestActorRef.create(system,props,"testProcessAdSpendTransaction1");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, new ProcessAdSpendTransaction("K101"), 30000).toCompletableFuture();
                String result = (String)future.get();
                if(result != null)
                    assert true;
            }
        };
    }
}