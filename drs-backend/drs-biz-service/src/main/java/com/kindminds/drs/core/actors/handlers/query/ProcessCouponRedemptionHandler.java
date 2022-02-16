package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.coupon.GetFailedCouponRedemptions;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.ProcessCouponRedemptionUco;
import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemption;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ProcessCouponRedemptionHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsCmdBus ) {

        return Props.create(ProcessCouponRedemptionHandler.class ,
                () -> new ProcessCouponRedemptionHandler(drsCmdBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();

    public ProcessCouponRedemptionHandler(ActorRef drsQueryBus ) {
        this.drsQueryBus = drsQueryBus;


        drsQueryBus.tell(new RegisterQueryHandler(name , GetFailedCouponRedemptions.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        ProcessCouponRedemptionUco uco = (ProcessCouponRedemptionUco)springCtx.getBean(
                ProcessCouponRedemptionUco.class);

        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(GetFailedCouponRedemptions.class, fcr -> {
                    try {
                        String jStr = null;
                        List<CouponRedemption> couponRedemptions =
                                    uco.getFailedCouponRedemptionFees();

                            jStr = mapper.writeValueAsString(couponRedemptions);

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
