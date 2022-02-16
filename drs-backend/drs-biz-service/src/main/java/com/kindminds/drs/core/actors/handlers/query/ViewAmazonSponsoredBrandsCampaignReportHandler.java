package com.kindminds.drs.core.actors.handlers.query;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetAmazonSponsoredBrandsCampaignReport;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetCampaignNames;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetMarketplaceNames;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetSupplierKcodeToShortEnNameMap;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.ViewAmazonSponsoredBrandsCampaignReportUco;
import com.kindminds.drs.core.RegisterQueryHandler;
import com.kindminds.drs.api.v1.model.report.ViewAmazonSponsoredBrandsCampaignReport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ViewAmazonSponsoredBrandsCampaignReportHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsQueryBus ) {

        return Props.create(ViewAmazonSponsoredBrandsCampaignReportHandler.class ,
                () -> new ViewAmazonSponsoredBrandsCampaignReportHandler(drsQueryBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsQueryBus ;

    private final String name = self().path().name();

    public ViewAmazonSponsoredBrandsCampaignReportHandler(ActorRef drsQueryBus ) {
        this.drsQueryBus = drsQueryBus;


        drsQueryBus.tell(new RegisterQueryHandler(name , GetAmazonSponsoredBrandsCampaignReport.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetCampaignNames.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetMarketplaceNames.class.getName()
                , self()) , ActorRef.noSender());

        drsQueryBus.tell(new RegisterQueryHandler(name , GetSupplierKcodeToShortEnNameMap.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {

        ViewAmazonSponsoredBrandsCampaignReportUco uco = (ViewAmazonSponsoredBrandsCampaignReportUco)springCtx.getBean(
                ViewAmazonSponsoredBrandsCampaignReportUco.class);

        ObjectMapper mapper = new ObjectMapper();

        return receiveBuilder()
                .match(GetAmazonSponsoredBrandsCampaignReport.class, gasbcr -> {
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = null;
                        Date endDate = null;
                        if(gasbcr.startDate() != null && !gasbcr.startDate().isEmpty())
                            startDate = simpleDateFormat.parse(gasbcr.startDate());
                        if(gasbcr.endDate() != null && !gasbcr.endDate().isEmpty())
                            endDate = simpleDateFormat.parse(gasbcr.endDate());
                        ViewAmazonSponsoredBrandsCampaignReport report = uco.queryReport(gasbcr.supplierKcode(), gasbcr.marketplaceId(),
                                                                            startDate, endDate, gasbcr.campaignName());

                        String jStr = mapper.writeValueAsString(report);

                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(GetCampaignNames.class, gcn -> {

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
                .match(GetMarketplaceNames.class, gmn -> {
                    try {
                        Map<Integer, String> marketplaces = uco.getMarketplaces();

                        String jStr = mapper.writeValueAsString(marketplaces);

                        getSender().tell(jStr, getSelf());
                    } catch (Exception e) {
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
                    }
                })
                .match(GetSupplierKcodeToShortEnNameMap.class, gsk -> {
                    try {
                        Map<String,String> supplierKcodeToShortEnNameMap = uco.getSupplierKcodeToShortEnNameMap();

                        String jStr = mapper.writeValueAsString(supplierKcodeToShortEnNameMap);

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
