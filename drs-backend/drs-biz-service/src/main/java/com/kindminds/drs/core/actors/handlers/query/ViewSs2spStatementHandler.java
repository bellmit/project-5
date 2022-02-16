package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.GetSs2spAdvertisingCostReport;
import com.kindminds.drs.api.message.GetSs2spMarketingActivityExpenseReport;
import com.kindminds.drs.api.message.GetSs2spOtherRefundReport;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.accounting.ViewSs2spStatementUco;
import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionAdvertisingCostReport;
import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionMarketingActivityExpenseReport;
import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionOtherRefundReport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ViewSs2spStatementHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsQueryBus) {

        return Props.create(ViewSs2spStatementHandler.class ,
                () -> new ViewSs2spStatementHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();

    private final String name = self().path().name();


    public ViewSs2spStatementHandler(ActorRef drsQueryBus ) {


        drsQueryBus.tell(new RegisterQueryHandler(name , GetSs2spAdvertisingCostReport.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetSs2spOtherRefundReport.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name, GetSs2spMarketingActivityExpenseReport.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {

        ViewSs2spStatementUco uco =(ViewSs2spStatementUco) springCtx.getBean(
                "ViewSs2spStatementUcoImpl4Be");

        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(GetSs2spAdvertisingCostReport.class, acr -> {
                    try {
                        ProfitShareSubtractionAdvertisingCostReport report =
                                uco.getSs2spAdvertisingCostReport(acr.statementName(), acr.countryCode());

                        String jStr = mapper.writeValueAsString(report);

                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(GetSs2spOtherRefundReport.class, orr -> {
                    try {
                        ProfitShareSubtractionOtherRefundReport report =
                                uco.getSs2spOtherRefundReport(orr.statementName(), orr.countryCode());

                        String jStr = mapper.writeValueAsString(report);

                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(GetSs2spMarketingActivityExpenseReport.class, maer -> {
                    try {
                        ProfitShareSubtractionMarketingActivityExpenseReport report =
                                uco.getSs2spMarketingActivityExpenseReport(maer.statementName(), maer.countryCode());

                        String jStr = mapper.writeValueAsString(report);

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
