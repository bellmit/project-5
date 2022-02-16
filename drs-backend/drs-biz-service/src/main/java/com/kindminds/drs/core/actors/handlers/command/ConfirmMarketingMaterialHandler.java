package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.confirmMarketingMaterialUco.*;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.product.ConfirmMarketingMaterialUco;
import com.kindminds.drs.core.RegisterCommandHandler;
import com.kindminds.drs.api.v1.model.product.ConfirmMarketingMaterialComment;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ConfirmMarketingMaterialHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsCmdBus) {

        return Props.create(ConfirmMarketingMaterialHandler.class ,
                () -> new ConfirmMarketingMaterialHandler(drsCmdBus) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsCmdBus ;

    private final String name = self().path().name();


    public ConfirmMarketingMaterialHandler(ActorRef drsCmdBus ) {
        this.drsCmdBus = drsCmdBus;


        drsCmdBus.tell(new RegisterCommandHandler(name , SendEmail.class.getName()
                        , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , AddComment.class.getName()
                        , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetComments.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , ConfirmMarketingMaterialUco.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {

        ConfirmMarketingMaterialUco uco =
                (ConfirmMarketingMaterialUco)springCtx.getBean(ConfirmMarketingMaterialUco.class);

        return receiveBuilder()
                .match(SendEmail.class, se -> {
                    uco.sendEmail(se.emailType(), se.marketplace(),
                            se.productBaseCode(), se.productCodeName(), se.supplierKCode());

                }).match(AddComment.class, ac ->{
                    uco.addComment(ac.userId(), ac.marketplace(), ac.productBaseCode(), ac.supplierKcode(),
                            ac.contents(), ac.baseCodeAndName());

                }).match(GetComments.class, gc ->{
                    List<ConfirmMarketingMaterialComment> comments = uco.getComments(
                                gc.marketplaceId(), gc.productBaseCode());
                    getSender().tell(comments, getSelf());

                }).match(AutoRenotifySuppliers.class, ars -> {
                    uco.autoRenotifySuppliers();
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
