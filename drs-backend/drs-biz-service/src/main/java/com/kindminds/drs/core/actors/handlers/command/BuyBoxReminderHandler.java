package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.command.SendBuyBoxReminder;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.core.RegisterCommandHandler;

import com.kindminds.drs.api.data.access.rdb.marketing.BuyBoxReminderDao;

import com.kindminds.drs.api.v1.model.marketing.BuyBoxReminderData;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.util.BigDecimalHelper;
import com.kindminds.drs.v1.model.impl.marketing.BuyBoxReminderDataImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BuyBoxReminderHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsCmdBus ) {

        return Props.create(BuyBoxReminderHandler.class ,
                () -> new BuyBoxReminderHandler(drsCmdBus) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsCmdBus ;

    private BuyBoxReminderDao bbrDao = springCtx.getBean(BuyBoxReminderDao.class);

    private MailUtil mailUtil = springCtx.getBean(MailUtil.class);

    private final String name = self().path().name();
    private static final String[] ACCOUNT_MANAGERS = {"account.managers@tw.drs.network"};
    private static final String[] BCC = {"arthur.wu@drs.network"};
    private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";
    private static final String MAIL_ADDRESS_NOREPLY = "DRS.network <drs-noreply@tw.drs.network>";

    public BuyBoxReminderHandler(ActorRef drsCmdBus ) {
        this.drsCmdBus = drsCmdBus;

        drsCmdBus.tell(new RegisterCommandHandler(name , SendBuyBoxReminder.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public AbstractActor.Receive createReceive() {

        return receiveBuilder()
                .match(SendBuyBoxReminder.class, br ->{

                    Instant reportDate = bbrDao.queryLatestReportDate()
                            .minus(2, ChronoUnit.DAYS);

                    Map<String, List<BuyBoxReminderData>> companyBuyBoxMap =
                            getCompanyToBuyBoxDataMap(reportDate);

                    try {
                        for (String companyCode : companyBuyBoxMap.keySet()) {
                            createMessage(companyCode, companyBuyBoxMap.get(companyCode));
                        }
                    } catch (Exception e) {
                        this.mailUtil.Send(SOFTWARE_ENGINEERS, MAIL_ADDRESS_NOREPLY,
                                "Mail message from BuyBoxReminderHandler ERROR",
                                e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
                    }

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private Map<String, List<BuyBoxReminderData>> getCompanyToBuyBoxDataMap(Instant reportDate) {

        List<Object []>  resultList = bbrDao.queryLowBuyBoxSkus(reportDate);

        List<BuyBoxReminderData> lowBuyBoxList = new ArrayList<>();
        for (Object[] result : resultList) {
            lowBuyBoxList.add(new BuyBoxReminderDataImpl((Date) result[0],
                    (String) result[1], (String) result[2], (String) result[3],
                    (String) result[4], (String) result[5], (BigDecimal) result[6]));
        }

        Map<String, List<BuyBoxReminderData>> companyBuyBoxMap = new TreeMap<>();

        for (BuyBoxReminderData buyBoxData : lowBuyBoxList) {
            if (companyBuyBoxMap.containsKey(buyBoxData.getCompanyCode())) {

                companyBuyBoxMap.get(buyBoxData.getCompanyCode()).add(buyBoxData);

            } else {

                List<BuyBoxReminderData> buyBoxDataList = new ArrayList<>();
                buyBoxDataList.add(buyBoxData);
                companyBuyBoxMap.put(buyBoxData.getCompanyCode(), buyBoxDataList);
            }
        }

        return companyBuyBoxMap;
    }

    private void createMessage(String companyCode, List<BuyBoxReminderData> buyBoxList) {
        String reportDate = new SimpleDateFormat("yyyy-MM-dd")
                .format(buyBoxList.get(0).getReportDate());
        String shortNameEnUs = buyBoxList.get(0).getShortNameEnUs();

        String subject = "Low Buy Box Rate - " + companyCode + " " +
                shortNameEnUs + " " + reportDate;
        StringBuilder message = new StringBuilder();
        message.append("<p>Please refer to the list of SKUs with Buy Box Rate < 95%. Thank you!</p>")
                .append("<table width='80%' border='1' style='text-align:center;border-collapse:collapse;'>")
                .append("<tr><th>Date</th><th>Marketplace</th><th>BP</th><th>SKU</th><th>Amazon Buy Box Rate</th></tr>");

        for (BuyBoxReminderData buyBoxData : buyBoxList) {
            message.append("<tr><td>" + reportDate + "</td><td>");
            message.append(buyBoxData.getMarketplaceName() + "</td><td>");
            message.append(buyBoxData.getBaseCode() + "</td><td>");
            message.append(buyBoxData.getSkuCode() + "</td><td>");
            message.append(BigDecimalHelper.toPercentageString(buyBoxData.getBuyBoxRate(), 2) + "</td></tr>");
        }
        message.append("</table><br>");

        String signature = this.mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY);
        message.append(signature);

        mailMessage(subject, message.toString());
    }

    private void mailMessage(String subject, String message) {
        mailUtil.SendMimeWithBcc(ACCOUNT_MANAGERS, BCC ,MAIL_ADDRESS_NOREPLY, subject, message);
    }
}
