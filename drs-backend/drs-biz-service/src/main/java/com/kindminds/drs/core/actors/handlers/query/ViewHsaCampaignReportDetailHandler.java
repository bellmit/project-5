package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.data.report.HsaCampaignReport;
import com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetCampaignNames;
import com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetHsaCampaignReport;
import com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetHsaCampaignReportDetail;
import com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetHsaCampaignReportDetailBySettlementPeriod;
import com.kindminds.drs.biz.service.util.BizCoreCtx;

import com.kindminds.drs.api.usecase.ViewHsaCampaignReportDetailUco;
import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.report.HsaCampaignReportDetail;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewHsaCampaignReportDetailHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsQueryBus ) {

        return Props.create(ViewHsaCampaignReportDetailHandler.class ,
                () -> new ViewHsaCampaignReportDetailHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();

    private final String name = self().path().name();

    public ViewHsaCampaignReportDetailHandler(ActorRef drsQueryBus ) {


        drsQueryBus.tell(new RegisterQueryHandler(name , GetHsaCampaignReportDetail.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetHsaCampaignReportDetailBySettlementPeriod.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetHsaCampaignReport.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetCampaignNames.class.getName()
                , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {

        ViewHsaCampaignReportDetailUco uco = (ViewHsaCampaignReportDetailUco)springCtx.getBean(
                ViewHsaCampaignReportDetailUco.class);

        ObjectMapper mapper = new ObjectMapper();
        return receiveBuilder()
                .match(GetHsaCampaignReportDetail.class, hcrd -> {
                    try {

                        List<HsaCampaignReportDetail> hsaCampaignReportDetails =
                                uco.getHsaCampaignReportDetail();
                        String jStr = mapper.writeValueAsString(hsaCampaignReportDetails);
                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(GetHsaCampaignReportDetailBySettlementPeriod.class, hcrdsp -> {
                    try {
                        List<HsaCampaignReportDetail> hsaCampaignReportDetails = uco.getHsaCampaignReportDetail(hcrdsp.settlementPeriodId());
                        String jStr = mapper.writeValueAsString(hsaCampaignReportDetails);
                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                }).match(GetHsaCampaignReport.class, r -> {
                    try {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = null;
                        Date endDate = null;
                        if(r.startDate() != null && !r.startDate().isEmpty())
                            startDate = simpleDateFormat.parse(r.startDate());
                        if(r.endDate() != null && !r.endDate().isEmpty())
                            endDate = simpleDateFormat.parse(r.endDate());

                        HsaCampaignReport rpt =  uco.queryReport(r.supplierKcode(),r.marketplaceId(),
                                startDate,endDate,r.campaignName());

                        String jStr = mapper.writeValueAsString(rpt);

                        getSender().tell(jStr, getSelf());

                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                }).match(GetCampaignNames.class, gcn -> {

                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = null;
                        Date endDate = null;
                        if(gcn.startDate() != null && !gcn.startDate().isEmpty())
                            startDate = simpleDateFormat.parse(gcn.startDate());
                        if(gcn.endDate() != null && !gcn.endDate().isEmpty())
                            endDate = simpleDateFormat.parse(gcn.endDate());

                        List<String> campaignNames =
                                uco.queryCampaignNames(gcn.supplierKcode(), gcn.marketplaceId(), startDate, endDate);

                        String jStr = mapper.writeValueAsString(campaignNames);

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