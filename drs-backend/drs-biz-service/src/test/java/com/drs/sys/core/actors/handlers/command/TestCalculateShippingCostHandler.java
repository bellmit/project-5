package com.drs.sys.core.actors.handlers.command;


import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.kindminds.drs.core.DrsCmdBus;

//import com.kindminds.drs.data.dto.logistics.ShipmentIvs;
//import com.kindminds.drs.data.dto.logistics.ShipmentIvs.ShipmentIvsLineItem;


import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentIvsDao;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestCalculateShippingCostHandler {


    private static TestActorRef drsCmdBus;
    private static ActorSystem system;

    @Autowired
    private MaintainShipmentIvsDao dao;

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
    public void testCalculateShippingCost() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {

                //todo arthur comment
                /*
                List<ShipmentIvsLineItem> lineItems = new ArrayList<>();
                lineItems.add(new ShipmentIvsLineItemImpl(
                        1, 0, "K510-85U05001R0", 600, 1220, 47.0, 44.0, 20.0, 7.560, 10, 60));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        2, 0, "K510-85U06B01R0-Blue", 500, 510, 53.5, 30.0, 19.0, 5.350, 20, 25));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        3, 0, "K510-85U06R01R0-Orange", 500, 510, 53.5, 30.0, 19.0, 5.350, 20, 25));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        4, 0, "K510-85U13G01R0", 200, 820, 52.0, 30.0, 19.0, 5.600, 20, 10));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        5, 0, "K510-85U13S01R0", 200, 820, 52.0, 30.0, 19.0, 5.600, 20, 10));
                ShipmentIvsImpl shipment = new ShipmentIvsImpl("IVS-K510-DRAFT1", "3", "US", "SEA_FREIGHT", lineItems);
                //System.out.//println("shipment: " + shipment);
                final Props props = Props.create(CalculateShippingCostHandler.class, drsCmdBus);
                final TestActorRef<CalculateShippingCostHandler> ref =
                        TestActorRef.create(system, props, "testCalculateShippingCost1");
                final CompletableFuture<Object> future = PatternsCS
                        .ask(ref, new CalculateInventoryShipmentCost(shipment), 10000)
                        .toCompletableFuture();
                String shippingCost = (String) future.get();
                //System.out.//println("Shipping Cost: " + shippingCost);
                */
//                assertEquals(new BigDecimal(70011), shippingCost);
            }
        };
    }

    @Test
    public void testCalculateShippingCostMixed() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                //todo arthur comment
                /*
                List<ShipmentIvsLineItem> lineItems = new ArrayList<>();
                lineItems.add(new ShipmentIvsLineItemImpl(
                        1, 0, "K510-85U05001R0", 600, 1220, 47.0, 44.0, 20.0, 7.560, 10, 60));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        2, 0, "K510-85U06B01R0-Blue", 500, 510, 53.5, 30.0, 19.0, 5.350, 20, 25));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        3, 0, "K510-85U06R01R0-Orange", 500, 510, 53.5, 30.0, 19.0, 5.350, 20, 25));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        4, 0, "K510-85U13G01R0", 200, 820, 52.0, 30.0, 19.0, 5.600, 20, 10));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        5, 0, "K510-85U13S01R0", 200, 820, 52.0, 30.0, 19.0, 5.600, 20, 10));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        6, 1, "K510-85U13G01R0", 5, 820, 45.5, 35.5, 24.0, 9.255, 5, 1));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        6, 2, "K510-85U13S01R0", 5, 820, 45.5, 35.5, 24.0, 9.255, 5, 0));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        6, 3, "K510-85U05001R0", 5, 1220, 45.5, 35.5, 24.0, 9.255, 5, 0));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        6, 4, "K510-85U06R01R0-Orange", 5, 510, 45.5, 35.5, 24.0, 9.255, 5, 0));
                lineItems.add(new ShipmentIvsLineItemImpl(
                        6, 5, "K510-85U06B01R0-Blue", 5, 510, 45.5, 35.5, 24.0, 9.255, 5, 0));
                ShipmentIvsImpl shipment = new ShipmentIvsImpl("IVS-K510-DRAFT1", "3", "US", "SEA_FREIGHT", lineItems);
                //System.out.//println("shipment: " + shipment);
                final Props props = Props.create(CalculateShippingCostHandler.class, drsCmdBus);
                final TestActorRef<CalculateShippingCostHandler> ref =
                        TestActorRef.create(system, props, "testCalculateShippingCost2");
                final CompletableFuture<Object> future = PatternsCS
                        .ask(ref, new CalculateInventoryShipmentCost(shipment), 10000)
                        .toCompletableFuture();
                String shippingCost = (String) future.get();
                //System.out.//println("Shipping Cost: " + shippingCost);
                */
//                assertEquals(new BigDecimal(70802), shippingCost);
            }
        };
    }

    @Test
    public void testCalculateShippingCostGet() throws ExecutionException, InterruptedException {
        new TestKit(system) {
            {
                //todo arthur comment
                /*
                ShipmentIvs shipment = dao.query("IVS-K510-68");
                //System.out.//println("shipment: " + shipment);
                final Props props = Props.create(CalculateShippingCostHandler.class, drsCmdBus);
                final TestActorRef<CalculateShippingCostHandler> ref =
                        TestActorRef.create(system, props, "testCalculateShippingCost3");
                final CompletableFuture<Object> future = PatternsCS
                        .ask(ref, new CalculateInventoryShipmentCost(shipment), 10000)
                        .toCompletableFuture();
                String shippingCost = (String) future.get();
                //System.out.//println("Shipping Cost: " + shippingCost);
                */
//                assertEquals(new BigDecimal(70011), shippingCost);
            }
        };
    }

}
