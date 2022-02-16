package com.kindminds.drs.core.actors.handlers.query.common;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.GetLatestSettlementPeriod;
import com.kindminds.drs.api.message.GetSettlementPeriodList;
import com.kindminds.drs.api.message.IsLatestSettlementPeriodSettled;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.common.SettlementPeriodListUco;
import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class SettlementPeriodListHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsQueryBus ) {

        return Props.create(SettlementPeriodListHandler.class ,
                () -> new SettlementPeriodListHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();

    public SettlementPeriodListHandler(ActorRef drsQueryBus ) {
        this.drsQueryBus = drsQueryBus;


        drsQueryBus.tell(new RegisterQueryHandler(name , GetSettlementPeriodList.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetLatestSettlementPeriod.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , IsLatestSettlementPeriodSettled.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        SettlementPeriodListUco uco = (SettlementPeriodListUco)springCtx.getBean(
                SettlementPeriodListUco.class);
        ObjectMapper mapper = new ObjectMapper();
        return receiveBuilder()
                .match(GetSettlementPeriodList.class, spl -> {
                    try {

                        List<SettlementPeriod> settlementPeriods =
                                uco.getSettlementPeriodList();

                        getSender().tell(settlementPeriods, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(GetLatestSettlementPeriod.class, lsp -> {
                    try {
                        SettlementPeriod settlementPeriod = uco.getLatestSettlementPeriod();
                        String jStr = mapper.writeValueAsString(settlementPeriod);

                        getSender().tell(jStr, getSelf());

                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(IsLatestSettlementPeriodSettled.class, lsps -> {
                    try {
                        Boolean isSettled = uco.isLatestSettlementPeriodSettled();
                        String jStr = mapper.writeValueAsString(isSettled);
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
