package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.ProcessAdSpendTransaction;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.ProcessAdSpendTransactionUco;
import com.kindminds.drs.core.RegisterCommandHandler;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ProcessAdSpendTransactionHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsCmdBus) {

        return Props.create(ProcessAdSpendTransactionHandler.class ,
                () -> new ProcessAdSpendTransactionHandler(drsCmdBus) );
    }

    private final AnnotationConfigApplicationContext springCtx  = BizCoreCtx.get();
    private final ActorRef drsCmdBus ;

    private final String name = self().path().name();

    public ProcessAdSpendTransactionHandler(ActorRef drsCmdBus) {
        this.drsCmdBus = drsCmdBus;

        drsCmdBus.tell(new RegisterCommandHandler(name , ProcessAdSpendTransaction.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        ProcessAdSpendTransactionUco uco = (ProcessAdSpendTransactionUco)springCtx.getBean(
                ProcessAdSpendTransactionUco.class);
        ObjectMapper mapper = new ObjectMapper();
        return receiveBuilder()
                .match(ProcessAdSpendTransaction.class, pas -> {
                    try {
                        List<InternationalTransaction> internationalTransactions = uco.processAdSpendTransaction(pas.userCompanyKcode());
                        String jStr = (internationalTransactions != null ? mapper.writeValueAsString(internationalTransactions) : "");
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