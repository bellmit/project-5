package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.calculateMonthlyStorageFeeUco.Calculate;
import com.kindminds.drs.api.message.calculateMonthlyStorageFeeUco.CalculateSumOfTotalEstimatedMonthlyStorageFee;
import com.kindminds.drs.api.message.calculateMonthlyStorageFeeUco.GetMonths;
import com.kindminds.drs.api.message.calculateMonthlyStorageFeeUco.GetYears;


import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.CalculateMonthlyStorageFeeUco;
import com.kindminds.drs.core.RegisterCommandHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class CalculateMonthlyStorageFeeHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsCmdBus) {

        return Props.create(CalculateMonthlyStorageFeeHandler.class ,
                () -> new CalculateMonthlyStorageFeeHandler(drsCmdBus) );
    }

    private final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
    private final ActorRef drsCmdBus ;

    private final String name = self().path().name();


    public CalculateMonthlyStorageFeeHandler(ActorRef drsCmdBus) {
        this.drsCmdBus = drsCmdBus;

        drsCmdBus.tell(new RegisterCommandHandler(name , GetYears.class.getName()
                        , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetMonths.class.getName()
                        , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , Calculate.class.getName()
                , self()) , ActorRef.noSender());


        drsCmdBus.tell(new RegisterCommandHandler(name , CalculateMonthlyStorageFeeUco.class.getName()
                , self()) , ActorRef.noSender());


    }


    @Override
    public Receive createReceive() {

        CalculateMonthlyStorageFeeUco uco =
                (CalculateMonthlyStorageFeeUco)springCtx.getBean(CalculateMonthlyStorageFeeUco.class);


        return receiveBuilder()
                .match(GetYears.class, gy -> {

                    try {
                        List<String> years = uco.getYears();
                        getSender().tell(years, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }

                }).match(GetMonths.class, gm ->{
                    final List<String> months = uco.getMonths();
                    getSender().tell(months, getSelf());

                }).match(CalculateSumOfTotalEstimatedMonthlyStorageFee.class, cm ->{

                    uco.calculateSumOfTotalEstimatedMonthlyStorageFee(cm.supplierKcode(), cm.country(), cm.year(), cm.month());

                }).match(Calculate.class, c ->{

                    String calculate = uco.calculate(c.year(), c.month());
                    getSender().tell(calculate, getSelf());

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
