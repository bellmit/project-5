package com.kindminds.drs.core.schedule;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestProfitShareEmailReminderHandler {



    private static ActorSystem system;

    @Autowired private AuthenticationManager authenticationManager;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /*
    @Test
    public void testEmailReminderAkka() {
        new TestKit(system) {
            {
                MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
                final Props props = Props.create(ProfitShareEmailReminderHandler.class);
                final TestActorRef<ProfitShareEmailReminderHandler> ref = TestActorRef.create(system,props,"testA");
                final CompletableFuture<Object> future = PatternsCS.ask(ref, "", 120000).toCompletableFuture();
                //assert future.isDone();
            }
        };
    }*/
}
