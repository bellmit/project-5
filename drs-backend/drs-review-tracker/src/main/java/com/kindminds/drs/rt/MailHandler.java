package com.kindminds.drs.rt;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.util.MailMessage;
import com.kindminds.drs.rt.RegisterCommandHandler;
import com.kindminds.drs.service.util.MailUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MailHandler  extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private static final String MAIL_ADDRESS_NOREPLY = "DRS.network <drs-noreply@tw.drs.network>";

    public static Props props(ActorRef drsCmdBus , AnnotationConfigApplicationContext springCtx) {

        return Props.create(MailHandler.class ,
                () -> new MailHandler(drsCmdBus , springCtx) );
    }

    private final AnnotationConfigApplicationContext springCtx ;

    private final String name = self().path().name();

    public MailHandler(ActorRef drsCmdBus ,
                       AnnotationConfigApplicationContext springCtx) {
        this.springCtx = springCtx;

        drsCmdBus.tell(new RegisterCommandHandler(name , MailMessage.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        MailUtil mailUtil = springCtx.getBean(MailUtil.class);

        return receiveBuilder()
                .match(MailMessage.class, mm -> {
                    mailUtil.Send(mm.to().split(","),MAIL_ADDRESS_NOREPLY, mm.subject(), mm.body() );
                    getSender().tell("OK", getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
