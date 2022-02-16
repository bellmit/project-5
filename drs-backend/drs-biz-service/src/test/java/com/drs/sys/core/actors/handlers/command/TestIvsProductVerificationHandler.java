package com.drs.sys.core.actors.handlers.command;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.api.message.logistics.ConfirmProduct;
import com.kindminds.drs.api.message.logistics.RejectProduct;

import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.core.DrsQueryBus;
import com.kindminds.drs.core.actors.handlers.command.IvsProductVerificationHandler;

//import com.kindminds.drs.data.dto.logistics.ShipmentIvs;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

//import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestIvsProductVerificationHandler {

    private static TestActorRef drsQueryBus;
    private static TestActorRef drsCmdBus;

    //@Autowired
    //private MaintainShipmentIvsUco ivsUco;

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
    public void testIvsRejectProduct() throws InterruptedException {
        new TestKit(system) {
            {
                //todo arthur comment
                /*
                ShipmentIvs shipment = ivsUco.get("IVS-K616-14");

                drsCmdBus.tell(
                        new RejectProduct(shipment, "K616-Filter-CA", 1, 0),
                        ActorRef.noSender());
                        */

//                final Props props = Props.create(IvsProductVerificationHandler.class, drsCmdBus);
//                final TestActorRef<IvsProductVerificationHandler> ref = TestActorRef.create(
//                        system,props,"testCalculateOriginalAccounts");
//
//                final CompletableFuture<Object> future = PatternsCS.ask(ref,
//                        new RejectProduct(ShipmentStatus.SHPT_AWAIT_PLAN, "IVS-K616-14",
//                                "K616-Filter-CA", 1, 0),
//                        10000).toCompletableFuture();
                Thread.sleep(2000);
            }
        };
    }

    @Test
    public void testIvsConfirmProduct() {
        new TestKit(system) {
            {
                final Props props = Props.create(IvsProductVerificationHandler.class, drsCmdBus);
                final TestActorRef<IvsProductVerificationHandler> ref = TestActorRef.create(
                        system,props,"testIvsConfirmProduct");

                //todo arthur comment

                /*
                ShipmentIvs shipment = ivsUco.get("IVS-K616-14");

                final CompletableFuture<Object> future = PatternsCS.ask(ref,
                        new ConfirmProduct(shipment,"K616-Filter-CA", 1, 0),
                        10000).toCompletableFuture();
                        */


            }
        };
    }
}
