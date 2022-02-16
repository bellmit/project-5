package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.query.onboardingApplication.*;
import com.kindminds.drs.core.RegisterCommandHandler;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.OnboardingApplicationRepo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ViewServiceFeeHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsQueryBus , AnnotationConfigApplicationContext springCtx) {

        return Props.create(ViewServiceFeeHandler.class ,
                () -> new ViewServiceFeeHandler(drsQueryBus , springCtx) );
    }

    private final AnnotationConfigApplicationContext springCtx ;
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();


    public ViewServiceFeeHandler(ActorRef drsQueryBus ,
                                 AnnotationConfigApplicationContext springCtx) {
        this.drsQueryBus = drsQueryBus;
        this.springCtx = springCtx;

        drsQueryBus.tell(new RegisterCommandHandler(name , GetBaseCodesToMarketplacesMap.class.getName()
                        , self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {

        OnboardingApplicationRepo onboardingRepo =
                springCtx.getBean(OnboardingApplicationRepo.class);

        return receiveBuilder()
                .match(GetBaseCodesToMarketplacesMap.class, app ->{

                    /*
                    Map<String, List<String>> baseCodesToMarketplaces =
                            onboardingRepo.getBaseCodesToMarketplacesMap(app.companyKcode());
                    getSender().tell(baseCodesToMarketplaces, getSelf());
                    */
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
