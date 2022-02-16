package com.drs.sys.core.actors.handlers.command;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.command.CalculateOriginalAccountsReceivable;
import com.kindminds.drs.api.message.command.GenerateAccountsReceivableAgingReport;
import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.command.AccountsReceivableAgingHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestAccountsReceivableAgingHandler {

//    private static TestActorRef drsQueryBus;
    private static TestActorRef drsCmdBus;

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
//        drsQueryBus = TestActorRef.create(system, Props.create(DrsQueryBus.class),
//                "DrsQueryBus");

        drsCmdBus = TestActorRef.create(system, Props.create(DrsCmdBus.class),
                "DrsCmdBus");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testCalculateOriginalAccounts() {
        new TestKit(system) {
            {


                drsCmdBus.tell(new CalculateOriginalAccountsReceivable(), ActorRef.noSender());
//                final Props props = Props.create(AccountsReceivableAgingHandler.class, drsCmdBus);
//                final TestActorRef<AccountsReceivableAgingHandler> ref = TestActorRef.create(
//                        system,props,"testCalculateOriginalAccounts");
//
//                final CompletableFuture<Object> future = PatternsCS.ask(ref,
//                        new CalculateOriginalAccountsReceivable(),
//                        10000).toCompletableFuture();
            }
        };
    }

    @Test
    public void testGenerateAccountsReceivableAgingReport() {
        new TestKit(system) {
            {
                final Props props = Props.create(AccountsReceivableAgingHandler.class, drsCmdBus);
                final TestActorRef<AccountsReceivableAgingHandler> ref = TestActorRef.create(
                        system,props,"testGenerateAccountsReceivableAgingReport");

                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new GenerateAccountsReceivableAgingReport(),
                        10000).toCompletableFuture();
//                drsCmdBus.tell(new GenerateAccountsReceivableAgingReport(), ActorRef.noSender());

            }
        };
    }
}
