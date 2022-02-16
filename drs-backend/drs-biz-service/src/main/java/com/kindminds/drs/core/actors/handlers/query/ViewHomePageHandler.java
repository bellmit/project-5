package com.kindminds.drs.core.actors.handlers.query;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.viewHomePageUco.GetSs2spStatementListReport;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.ViewHomePageUco;
import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.report.StatementListReport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ViewHomePageHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsQueryBus ) {

        return Props.create(ViewHomePageHandler.class ,
                () -> new ViewHomePageHandler(drsQueryBus) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();


    public ViewHomePageHandler(ActorRef drsQueryBus ) {
        this.drsQueryBus = drsQueryBus;


        drsQueryBus.tell(new RegisterQueryHandler(name , GetSs2spStatementListReport.class.getName()
                , self()) , ActorRef.noSender());



    }


    @Override
    public Receive createReceive() {

        ViewHomePageUco uco = (ViewHomePageUco)springCtx.getBean(
                ViewHomePageUco.class);

        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(GetSs2spStatementListReport.class, gs -> {

                    try {

                        StatementListReport ss2spStatementListReport =
                                uco.getSs2spStatementListReport(gs.userCompanyKcode());

                        String jStr = mapper.writeValueAsString(ss2spStatementListReport);

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
