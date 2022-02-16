package com.kindminds.drs.web.config;

import akka.actor.ActorSystem;
import com.kindminds.drs.service.util.SpringAppCtx;

import javax.annotation.PreDestroy;

public class TerminateBean {

    @PreDestroy
    public void onDestroy() {

        ActorSystem ac = (ActorSystem)SpringAppCtx.get().getBean("actorSystem");
        ac.terminate();

        System.out.println("Spring Container is destroyed!");

    }
}
