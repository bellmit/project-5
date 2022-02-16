package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.viewKeyProductStatsUco.GetKeyProductStats;
import com.kindminds.drs.api.message.viewKeyProductStatsUco.GetKeyProductStatsReport;

//import com.kindminds.drs.api.message.viewKeyProductStatsUco.TestImportData;
import com.kindminds.drs.api.message.viewKeyProductStatsUco.GetKeyProductTotalFbaInStock;
import com.kindminds.drs.biz.service.util.BizCoreCtx;

import com.kindminds.drs.api.data.access.rdb.product.ViewKeyProductStatsDaoV2;
import com.kindminds.drs.api.usecase.ViewKeyProductStatsUco;

import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class ViewKeyProductStatsHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsQueryBus ) {

        return Props.create(ViewKeyProductStatsHandler.class ,
                () -> new ViewKeyProductStatsHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get() ;
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();


    public ViewKeyProductStatsHandler(ActorRef drsQueryBus) {
        this.drsQueryBus = drsQueryBus;

        drsQueryBus.tell(new RegisterQueryHandler(name , GetKeyProductStatsReport.class.getName()
                , self()) , ActorRef.noSender());

    }


    @Override
    public Receive createReceive() {

        ViewKeyProductStatsUco uco = (ViewKeyProductStatsUco)springCtx.getBean(
                        ViewKeyProductStatsUco.class);

        ObjectMapper mapper = new ObjectMapper();

        //ViewKeyProductStatsDaoV2 dao =(ViewKeyProductStatsDaoV2)springCtx.getBean(ViewKeyProductStatsDaoV2.class);


        return receiveBuilder()
                .match(GetKeyProductStatsReport.class, kp -> {

                    try {
                        KeyProductStatsReport keyProductStatsReport =
                                uco.getKeyProductStatsReport(kp.isSupplier(),kp.companyKcode());

                        ////System.out.//println(keyProductStatsReport.toString());

                        String jStr = mapper.writeValueAsString(keyProductStatsReport);
                        getSender().tell(jStr, getSelf());

                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }

                })
                /*
                .match(TestImportData.class , t ->{

                    ((com.kindminds.drs.service.control.product.ViewKeyProductStatsUcoImpl)(uco))
                            .saveRptToDb(false , "K2");


                })*/


                .match(GetKeyProductStats.class,kp->{

                })

                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
