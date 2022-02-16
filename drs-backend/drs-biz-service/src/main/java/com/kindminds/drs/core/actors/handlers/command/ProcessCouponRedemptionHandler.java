package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.coupon.ProcessCouponRedemption;
import com.kindminds.drs.api.message.coupon.ProcessCouponRedemptionBySettlementPeriod;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.ProcessCouponRedemptionUco;
import com.kindminds.drs.core.RegisterCommandHandler;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemptionStatus;
import com.kindminds.drs.api.v1.model.message.MessageCode;
import com.kindminds.drs.service.util.MessageImpl;


public class ProcessCouponRedemptionHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsCmdBus) {

        return Props.create(ProcessCouponRedemptionHandler.class ,
                () -> new ProcessCouponRedemptionHandler(drsCmdBus) );
    }


    private final ActorRef drsCmdBus ;


    private final String name = self().path().name();

    public ProcessCouponRedemptionHandler(ActorRef drsCmdBus) {
        this.drsCmdBus = drsCmdBus;


        drsCmdBus.tell(new RegisterCommandHandler(name , ProcessCouponRedemptionBySettlementPeriod.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , ProcessCouponRedemption.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {
        ProcessCouponRedemptionUco uco = (ProcessCouponRedemptionUco)  BizCoreCtx.get().getBean(
                ProcessCouponRedemptionUco.class);

        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(ProcessCouponRedemptionBySettlementPeriod.class, pcr -> {
                    try {
                        String jStr = null;
                        MessageCode msgCode = uco.getCouponProcessStatus();
                        if(msgCode == MessageCode.COUPONS_ELIGIBLE_TO_PROCESS) {
                            CouponRedemptionStatus couponRedemptions =
                                uco.processCouponRedemptionFees(pcr.periodId());

                            jStr = mapper.writeValueAsString(couponRedemptions);
                        }
                        else
                            jStr = mapper.writeValueAsString(new MessageImpl(msgCode));

                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(ProcessCouponRedemption.class, psr -> {
                    try {
                        String jStr = null;
                        MessageCode msgCode = uco.getCouponProcessStatus();
                        if(msgCode == MessageCode.COUPONS_ELIGIBLE_TO_PROCESS) {
                            CouponRedemptionStatus couponRedemptions = uco.processCouponRedemptionFees();
                            jStr = mapper.writeValueAsString(couponRedemptions);
                        }
                        else
                            jStr = mapper.writeValueAsString(new MessageImpl(msgCode));

                        getSender().tell(jStr, getSelf());
                    }
                    catch (Exception e){
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
