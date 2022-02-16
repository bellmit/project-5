package com.kindminds.drs.rt;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.usecase.reviewtracker.ImportAmazonReviewReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonReviewReportItem;
import com.kindminds.drs.rt.ImportAmazonReviews;

import com.kindminds.drs.rt.RegisterCommandHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ImportAmazonReviewReportHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsCmdBus , AnnotationConfigApplicationContext springCtx) {

        return Props.create(ImportAmazonReviewReportHandler.class ,
                () -> new ImportAmazonReviewReportHandler(drsCmdBus , springCtx) );
    }

    private final AnnotationConfigApplicationContext springCtx ;

    private final String name = self().path().name();

    public ImportAmazonReviewReportHandler(ActorRef drsCmdBus ,
                                           AnnotationConfigApplicationContext springCtx) {
        this.springCtx = springCtx;

        drsCmdBus.tell(new RegisterCommandHandler(name , ImportAmazonReviews.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {

        ImportAmazonReviewReportUco uco = springCtx.getBean(ImportAmazonReviewReportUco.class);
        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(ImportAmazonReviews.class, iar -> {

                    List<AmazonReviewReportItem> amazonReviews =
                            mapper.readValue(iar.amazonReviewList(), new TypeReference<List<AmazonReviewReportItem>>(){});
                    ////System.out.//println("Improt Handler received");
                    Integer updated = uco.importReviews(amazonReviews);
                    ////System.out.//println("updated => " + updated);
                    getSender().tell(updated, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}