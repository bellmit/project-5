package com.kindminds.drs;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.actor.Props;

import com.kindminds.drs.api.message.importSellback.ImportSellbackTransactions;

import com.kindminds.drs.core.DrsCmdBus;
import com.kindminds.drs.core.DrsQueryBus;
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension;

public class GoogleSheetApp
{

    public static void main( String[] args ) throws Exception {

        ExtendedActorSystem system = (ExtendedActorSystem)ActorSystem.create("drsGoogle");


        ActorRef drsCmdBus = system.actorOf(Props.create(DrsCmdBus.class),
                "DrsCmdBus");

        ActorRef drsQueryBus = system.actorOf(Props.create(DrsQueryBus.class),
                "DrsQueryBus");


        //Schedule Handlers
        ActorRef importSellbackActor = system.actorOf(ImportSellbackTransactionHandler.props(drsCmdBus),
                "ImportSellbackTransactionHandler");



        QuartzSchedulerExtension quartzSchedulerExtension = new QuartzSchedulerExtension(system);

      //  quartzSchedulerExtension.schedule("cronImportSellBackTransaction",
        //        importSellbackActor, new ImportSellbackTransactions());


        Thread.sleep(3000);
        drsCmdBus.tell(new ImportSellbackTransactions() ,ActorRef.noSender());



    }
}

