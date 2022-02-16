package com.kindminds.drs.rt;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.viewProductMarketplaceInfo.GetLiveAmazonAsins;
import com.kindminds.drs.api.message.viewProductMarketplaceInfo.GetLiveK101AmazonAsins;
import com.kindminds.drs.api.usecase.product.MaintainProductMarketplaceInfoUco;
import com.kindminds.drs.rt.RegisterQueryHandler;

import com.kindminds.drs.api.v1.model.product.AmazonAsin;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ViewProductMarketplaceInfoHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsQueryBus , AnnotationConfigApplicationContext springCtx) {

        return Props.create(ViewProductMarketplaceInfoHandler.class ,
                () -> new ViewProductMarketplaceInfoHandler(drsQueryBus , springCtx) );
    }

    private final AnnotationConfigApplicationContext springCtx ;

    private final String name = self().path().name();

    public ViewProductMarketplaceInfoHandler(ActorRef drsQueryBus ,
                                             AnnotationConfigApplicationContext springCtx) {
        this.springCtx = springCtx;

        drsQueryBus.tell(new RegisterQueryHandler(name , GetLiveAmazonAsins.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetLiveK101AmazonAsins.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        MaintainProductMarketplaceInfoUco uco = springCtx.getBean(
                MaintainProductMarketplaceInfoUco.class);

        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(GetLiveAmazonAsins.class, glaa -> {

                    try {

                        List<AmazonAsin> amazonAsins =
                                uco.getLiveAmazonAsins();



                        String jStr = mapper.writeValueAsString(amazonAsins);

                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }

                })
                .match(GetLiveK101AmazonAsins.class, gl101aa -> {

                    try {

                        List<AmazonAsin> amazonAsins =
                                uco.getLiveK101AmazonAsins();

                        String jStr = mapper.writeValueAsString(amazonAsins);

                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
