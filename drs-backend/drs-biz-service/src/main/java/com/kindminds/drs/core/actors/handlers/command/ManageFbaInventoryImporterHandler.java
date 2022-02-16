package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.manageFbaInventoryFileImporter.ImportFbaInventoryFile;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.report.amazon.ManageFbaInventoryFileImporter;
import com.kindminds.drs.core.RegisterCommandHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ManageFbaInventoryImporterHandler extends AbstractActor {


    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsCmdBus ) {
        return Props.create(ManageFbaInventoryImporterHandler.class ,
                () -> new ManageFbaInventoryImporterHandler(drsCmdBus) );
    }

    private final ActorRef drsCmdBus ;
    private final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
    private final String name = self().path().name();

    public ManageFbaInventoryImporterHandler(ActorRef drsCmdBus ) {
        this.drsCmdBus = drsCmdBus;

        drsCmdBus.tell(new RegisterCommandHandler(name , ImportFbaInventoryFile.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {

        ManageFbaInventoryFileImporter importer =
                springCtx.getBean(ManageFbaInventoryFileImporter.class);

        return receiveBuilder()
                .match(ImportFbaInventoryFile.class, imf -> {

                    String result = importer.importFbaInventoryFile(imf.fileData(), imf.fileName());

                    getSender().tell(result, getSelf());

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
