package com.drs.sys.core.actors.handlers.command;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.importSellback.ImportSellbackTransactions;
import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.ImportSellbackTransactionHandler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportSellbackTransactionHandler {


    private static TestActorRef drsCmdBus;

    private static ActorSystem system;

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

    @Test @Transactional
    public void testImportSellbackTransaction() {
        new TestKit(system) {
            {
                final Props props = Props.create(ImportSellbackTransactionHandler.class, drsCmdBus);
                final TestActorRef<ImportSellbackTransactionHandler> ref = TestActorRef.create(system,props,"testImportSellbackTransaction1");
                final CompletableFuture<Object> future =
                        PatternsCS.ask(ref, new ImportSellbackTransactions(), 50000).toCompletableFuture();

            }
        };
    }

//    @Test
//    public void testGetBaseProducts() {
//        new TestKit(system) {
//            {
//                final Props props = Props.create(ImportSellbackTransactionHandler.class, drsCmdBus);
//                final TestActorRef<ImportSellbackTransactionHandler> ref = TestActorRef.create(system,props,"testImportSellbackTransaction1");
//                final CompletableFuture<Object> future = PatternsCS
//                        .ask(ref, new GetBaseProducts(190, 1), 50000)
//                        .toCompletableFuture();
//
//            }
//        };
//    }
}
