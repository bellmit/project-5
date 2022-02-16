package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.logistics.ViewIvsProductDocuments;
import com.kindminds.drs.api.message.logistics.ViewIvsProductInfo;

import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.core.RegisterQueryHandler;



import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ViewIvsProductVerificationHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsQueryBus ) {

        return Props.create(ViewIvsProductVerificationHandler.class ,
                () -> new ViewIvsProductVerificationHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();

    private ViewIvsProductVerificationHandler(ActorRef drsQueryBus) {
        this.drsQueryBus = drsQueryBus;

        drsQueryBus.tell(new RegisterQueryHandler(name , ViewIvsProductInfo.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , ViewIvsProductDocuments.class.getName()
                , self()) , ActorRef.noSender());
    }



    @Override
    public Receive createReceive() {
        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(ViewIvsProductInfo.class, vi -> {

                    //todo arthur comment
                    /*
                    verifier = new IvsProductVerifierImpl(vi.shipment());

                    IvsProductVerifyInfo productInfo = verifier.getIvsProductInfo(vi.boxNum(), vi.mixedBoxLineSeq());

                    String resultJson = mapper.writeValueAsString(productInfo);

                    getSender().tell(resultJson, getSelf());
                    */
                })
                .match(ViewIvsProductDocuments.class, vi -> {
                    String resultJson = mapper.writeValueAsString(
                            "");

                    getSender().tell(resultJson, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
