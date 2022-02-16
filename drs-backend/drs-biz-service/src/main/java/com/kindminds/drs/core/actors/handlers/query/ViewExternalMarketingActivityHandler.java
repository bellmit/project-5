package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.viewExternalMarketingActivityUco.GetListOfSkus;
import com.kindminds.drs.api.message.viewExternalMarketingActivityUco.GetMarketingData;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.ViewExternalMarketingActivityUco;
import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingData;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingSkuItem;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ViewExternalMarketingActivityHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsQueryBus ) {

        return Props.create(ViewExternalMarketingActivityHandler.class ,
                () -> new ViewExternalMarketingActivityHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();

    public ViewExternalMarketingActivityHandler(ActorRef drsQueryBus ) {
        this.drsQueryBus = drsQueryBus;


        drsQueryBus.tell(new RegisterQueryHandler(name , GetListOfSkus.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetMarketingData.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        ViewExternalMarketingActivityUco uco = (ViewExternalMarketingActivityUco)springCtx.getBean(
                ViewExternalMarketingActivityUco.class);
        ObjectMapper mapper = new ObjectMapper();
        return receiveBuilder()
                .match(GetListOfSkus.class, gls -> {
                    try {
                        List<ExternalMarketingSkuItem> skuList = uco.getListOfSkus();
                        String jStr = mapper.writeValueAsString(skuList);
                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(GetMarketingData.class, gmd -> {
                    try {
                        ExternalMarketingData marketingData = uco.getMarketingData(
                                gmd.kCode(), gmd.skuCode(),gmd.marketplace());
                        String jStr = mapper.writeValueAsString(marketingData);
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