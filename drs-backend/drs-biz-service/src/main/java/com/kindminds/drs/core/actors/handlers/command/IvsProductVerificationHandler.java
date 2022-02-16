package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.logistics.ConfirmProduct;
import com.kindminds.drs.api.message.logistics.RejectProduct;
import com.kindminds.drs.api.message.logistics.VerifyProduct;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.core.RegisterCommandHandler;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IvsProductVerificationHandler extends AbstractActor {


    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsCmdBus ) {

        return Props.create(IvsProductVerificationHandler.class ,
                () -> new IvsProductVerificationHandler(drsCmdBus) );
    }

    private final ActorRef drsCmdBus;
    private final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
    private final String name = self().path().name();


    private IvsProductVerificationHandler(ActorRef drsCmdBus) {
        this.drsCmdBus = drsCmdBus;

        drsCmdBus.tell(
                new RegisterCommandHandler(name, VerifyProduct.class.getName(), self()),
                ActorRef.noSender());

        drsCmdBus.tell(
                new RegisterCommandHandler(name, RejectProduct.class.getName(), self()),
                ActorRef.noSender());

        drsCmdBus.tell(
                new RegisterCommandHandler(name, ConfirmProduct.class.getName(), self()),
                ActorRef.noSender());


    }



    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(VerifyProduct.class, ver -> {

                    //todo arthur comment
                    /*
                    verifier = new IvsProductVerifierImpl(ver.shipment());

                    //start process
                    verifier.verifyLineItem(ver.boxNum(), ver.mixedBoxLineSeq());

                    getSender().tell(verifier.getShipmentStatus(), getSelf());
                    */
                })
                .match(RejectProduct.class, ver -> {

                    //todo arthur comment
                    /*
                    verifier = new IvsProductVerifierImpl(ver.shipment());

                    //reject ivs line item
                    verifier.rejectLineItem(ver.boxNum(), ver.mixedBoxLineSeq());

                    getSender().tell(verifier.getShipmentStatus(), getSelf());
                    */
                })
                .match(ConfirmProduct.class, ver -> {

                    //todo arthur comment
                    /*
                    verifier = new IvsProductVerifierImpl(ver.shipment());

                    //apprive ivs line item
                    verifier.confirmLineItem(ver.boxNum(), ver.mixedBoxLineSeq());

                    getSender().tell(verifier.getShipmentStatus(), getSelf());
                    */
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
