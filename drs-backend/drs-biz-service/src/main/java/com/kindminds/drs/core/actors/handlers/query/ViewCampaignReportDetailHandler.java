package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.CampaignReport.GetCampaignReportDetail;
import com.kindminds.drs.api.message.CampaignReport.GetCampaignReportDetailBySettlementPeriod;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.ViewCampaignReportDetailUco;
import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.marketing.CampaignReportDetail;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ViewCampaignReportDetailHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsQueryBus) {

        return Props.create(ViewCampaignReportDetailHandler.class ,
                () -> new ViewCampaignReportDetailHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();

    public ViewCampaignReportDetailHandler(ActorRef drsQueryBus ) {
        this.drsQueryBus = drsQueryBus;


        drsQueryBus.tell(new RegisterQueryHandler(name , GetCampaignReportDetail.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetCampaignReportDetailBySettlementPeriod.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        ViewCampaignReportDetailUco uco = (ViewCampaignReportDetailUco)springCtx.getBean(
                ViewCampaignReportDetailUco.class);
        ObjectMapper mapper = new ObjectMapper();
        return receiveBuilder()
                .match(GetCampaignReportDetail.class, crd -> {
                    try {

                        List<CampaignReportDetail> campaignReportDetails =
                                uco.getCampaignReportDetail();
                        String jStr = mapper.writeValueAsString(campaignReportDetails);
                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(GetCampaignReportDetailBySettlementPeriod.class, crdsp -> {
                    try {
                        List<CampaignReportDetail> campaignReportDetails = uco.getCampaignReportDetail(crdsp.settlementPeriodId());
                        String jStr = mapper.writeValueAsString(campaignReportDetails);
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