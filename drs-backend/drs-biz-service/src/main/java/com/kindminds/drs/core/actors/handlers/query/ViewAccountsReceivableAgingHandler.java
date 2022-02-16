package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.query.ViewAccountsReceivableAgingList;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.core.RegisterQueryHandler;

import com.kindminds.drs.api.data.access.rdb.accounting.AccountsReceivableAgingDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ViewAccountsReceivableAgingHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsQueryBus ) {

        return Props.create(ViewAccountsReceivableAgingHandler.class ,
                () -> new ViewAccountsReceivableAgingHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();

    private AccountsReceivableAgingDao arAgingDao = springCtx.getBean(AccountsReceivableAgingDao.class);

    public ViewAccountsReceivableAgingHandler(ActorRef drsQueryBus) {
        this.drsQueryBus = drsQueryBus;

        drsQueryBus.tell(new RegisterQueryHandler(name , ViewAccountsReceivableAgingList.class.getName()
                , self()) , ActorRef.noSender());
    }


    @Override
    public Receive createReceive() {
        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(ViewAccountsReceivableAgingList.class, varal -> {
                    String resultJson = mapper.writeValueAsString(
                            arAgingDao.queryAccountReceivableAgingList());

                    getSender().tell(resultJson, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
