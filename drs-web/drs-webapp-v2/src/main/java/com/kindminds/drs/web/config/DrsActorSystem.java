package com.kindminds.drs.web.config;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.Bean;
import akka.actor.ActorRef;
import scala.concurrent.duration.Duration;
import akka.util.Timeout;
import scala.concurrent.Await;
import akka.actor.ActorPath;
import com.kindminds.drs.service.util.SpringAppCtx;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.TimeoutException;


public class DrsActorSystem {

	public static ActorSystem actorSystem = (ActorSystem) SpringAppCtx.get().getBean("actorSystem") ;
	private  static ActorRef cmdBus  = null;
	private static ActorRef queryBus = null;
	private static int cmdBusCount = 0;
	private static int queryBusCount = 0;

	public static ActorRef drsCmdBus() {

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		ActorPath path = ActorPath.fromString("akka://drs@localhost:5001/user/DrsCmdBus");

		try {
			cmdBus =  Await.result(
					actorSystem.
							actorSelection(path).resolveOne(timeout), timeout.duration());

		} catch (Exception e) {
			e.printStackTrace();

			if (cmdBusCount < 10) {
				cmdBus = DrsActorSystem.drsCmdBus();
			} else {
				cmdBus = null;
			}

			cmdBusCount += 1;
		}

		return cmdBus;
	}

	public static ActorRef drsQueryBus() {

		Timeout timeout = new Timeout(Duration.create(3, "seconds"));
		ActorPath path = ActorPath.fromString("akka://drs@localhost:5001/user/DrsQueryBus");

		try {
			queryBus =  Await.result(
					actorSystem.
							actorSelection(path).resolveOne(timeout), timeout.duration());

		} catch (Exception e) {
			e.printStackTrace();

			if (queryBusCount < 10) {
				queryBus = DrsActorSystem.drsQueryBus();
			} else {
				queryBus = null;
			}

			queryBusCount += 1;

			//println(queryBus?.path())
		}



		return queryBus;
	}


	
}