package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.message.importAmazonDateRangeReportUco.GetImportStatus;
import com.kindminds.drs.api.message.importAmazonDateRangeReportUco.GetMarketplaces;
import com.kindminds.drs.api.message.importAmazonDateRangeReportUco.ImportReport;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonDateRangeReportUco;
import com.kindminds.drs.core.RegisterCommandHandler;
import com.kindminds.drs.api.v1.model.report.DateRangeImportStatus;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ImportAmazonDateRangeReportHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsCmdBus ) {

        return Props.create(ImportAmazonDateRangeReportHandler.class ,
                () -> new ImportAmazonDateRangeReportHandler(drsCmdBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
    private final ActorRef drsCmdBus ;

    private final String name = self().path().name();


    public ImportAmazonDateRangeReportHandler(ActorRef drsCmdBus ) {
        this.drsCmdBus = drsCmdBus;


        drsCmdBus.tell(new RegisterCommandHandler(name , GetMarketplaces.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetImportStatus.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , ImportReport.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {

       // ImportAmazonDateRangeReportUco uco =
         //       (ImportAmazonDateRangeReportUco)springCtx.getBean(ImportAmazonDateRangeReportUco.class);

        return receiveBuilder()
                .match(GetMarketplaces.class, gm -> {
                   // List<Marketplace> marketplaces = uco.getMarketplaces();
                   // getSender().tell(marketplaces, getSelf());

                }).match(GetImportStatus.class, gis ->{
                  //  List<DateRangeImportStatus> importStatuses = uco.getImportStatus();
                   // getSender().tell(importStatuses, getSelf());

                }).match(ImportReport.class, rp ->{
                   // String result = uco.importReport(rp.marketplaceId(), rp.fileBytes());
                   // getSender().tell(result, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
