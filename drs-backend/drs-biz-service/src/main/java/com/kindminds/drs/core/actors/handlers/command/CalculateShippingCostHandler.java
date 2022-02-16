package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.core.RegisterCommandHandler;

import com.kindminds.drs.core.biz.repo.logistics.IvsRepoImpl;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.repo.logistics.IvsRepo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class CalculateShippingCostHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
    private final ActorRef drsCmdBus;

    private final String name = self().path().name();

    public static Props props(ActorRef drsCmdBus) {
        return Props.create(CalculateShippingCostHandler.class,
                () -> new CalculateShippingCostHandler(drsCmdBus));
    }

    private CalculateShippingCostHandler(ActorRef drsCmdBus) {
        this.drsCmdBus = drsCmdBus;


//        drsCmdBus.tell(new RegisterCommandHandler(name,
//                CalculateInventoryShipmentCost.class.getName(), self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
//                .match(CalculateInventoryShipmentCost.class, cisc -> {
//
//                    IvsRepoImpl ivsRepo = new IvsRepoImpl();
//                    Optional<Ivs> opIvs = ivsRepo.findByName(cisc.shipment().getName());
//                    String shippingCost ="";
//                    if(opIvs.isPresent()){
//                        shippingCost =  opIvs.get().calculateInventoryShipmentCost();
//                    }
//
//                    getSender().tell(shippingCost, getSelf());
//                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
