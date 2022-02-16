package com.kindminds.drs;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.data.transfer.fba.FbaReturnToSupplierItem;
import com.kindminds.drs.api.message.importSellback.ImportSellbackTransactions;
import com.kindminds.drs.core.RegisterCommandHandler;


import com.kindminds.drs.api.data.access.rdb.inventory.FbaReturnToSupplierDao;

import com.kindminds.drs.service.util.MailUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImportSellbackTransactionHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsCmdBus ) {

        return Props.create(ImportSellbackTransactionHandler.class ,
                () -> new ImportSellbackTransactionHandler(drsCmdBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();
    private final ActorRef drsCmdBus ;

    private final String name = self().path().name();
    private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";
    private static final String ADDRESS_NO_REPLY = "drs-noreply@tw.drs.network";
    private static final String MAIL_SUBJECT = "Import Sellback Transactions Message";


    public ImportSellbackTransactionHandler(ActorRef drsCmdBus ) {
        this.drsCmdBus = drsCmdBus;


        drsCmdBus.tell(new RegisterCommandHandler(name , ImportSellbackTransactions.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public AbstractActor.Receive createReceive() {

        return receiveBuilder()
                .match(ImportSellbackTransactions.class, ist ->{
                    Config config = ConfigFactory.load("application.conf");
                    String spreadsheetId = config.getString("drs.sellbackSpreadSheetId");
                    String sheetRange = config.getString("drs.sellbackSheetRange");

                    try {
                        importSellbackTransactions(spreadsheetId, sheetRange);
                    } catch (Exception e){
//                        mailMessage(e.toString());
                        e.printStackTrace();
                    }

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private void importSellbackTransactions(String spreadsheetId, String sheetRange)
            throws IOException, GeneralSecurityException {

        ImportGoogleSheetsUtil sheetsUtil = new ImportGoogleSheetsUtil();

        FbaReturnToSupplierDao fbaDao = springCtx.getBean(FbaReturnToSupplierDao.class);

        Timestamp periodEnd = fbaDao.queryLastPeriodEnd();
        OffsetDateTime drsProcessDate = OffsetDateTime
                .ofInstant(Instant.ofEpochMilli(periodEnd.getTime()), ZoneId.systemDefault());
        //Check if latest settlement period ended last week
        if (drsProcessDate.compareTo(OffsetDateTime.now().minusDays(7)) < 0) {
            return;
        }

        List<List<Object>> values = sheetsUtil.importSheetData(spreadsheetId, sheetRange);

        if (values == null || values.isEmpty()) {
            mailMessage("ImportGoogleSheetsUtil: NO DATA FOUND.");
            return;
        }

        List<String> ivsList = new ArrayList<>();
        for (List<Object> row : values) {
            if (StringUtils.hasText((String)row.get(0)) &&
                    StringUtils.hasText((String)row.get(2)) &&
                    StringUtils.hasText((String)row.get(3))) {

                ivsList.add(((String) row.get(0)).trim());

            } else {
                mailMessage("MISSING REQUIRED DATA FOR COLUMNS 1 TO 3");
                return;
            }
        }

        Map<String, List<String>> unsNameMap = fbaDao.queryIvsToUnsMarketplace(ivsList);

        drsProcessDate = drsProcessDate.minusDays(7);
        List<FbaReturnToSupplierItem> sellbackRecords = new ArrayList<>();
        for (List<Object> row : values) {
            String drsSku = ((String) row.get(2)).trim();
            String ivsName = ((String) row.get(0)).trim();
            //System.out.//println(ivsName);
            String unsName = unsNameMap.get(ivsName).get(0);
            String countryAbbr = unsNameMap.get(ivsName).get(1);
            String marketplace = Marketplace.getMarketplaceNameFromCountry(countryAbbr);
            Integer quantity = Integer.valueOf((String) row.get(3));
            String sellbackType;
            if (row.size() > 3) {
                sellbackType = (String) row.get(4);
            } else {
                sellbackType = null;
            }

            //System.out.//println(sellbackType);

            sellbackRecords.add(new FbaReturnToSupplierItem(
                    drsProcessDate, drsSku, marketplace,
                    quantity, ivsName, unsName, sellbackType));

            drsProcessDate = drsProcessDate.plusSeconds(1);
        }
        if (!fbaDao.queryFbaDataExists(sellbackRecords.get(0).getDrsProcessDate())) {

            Integer numInserted = fbaDao.insertSellbackRecords(sellbackRecords);
            String insertMessage = "INSERTED AMOUNT: " + numInserted + " RECORDS";
            //System.out.//println(insertMessage);
            log.info(insertMessage);
            mailMessage(insertMessage);
        }else{
            //System.out.//println("BBBBBBBBBBBBBBb");
        }
    }

    private void mailMessage(String message) {
        MailUtil mailUtil = springCtx.getBean(MailUtil.class);
        message = "ImportSellbackTransactionHandler:\n" + message;
        mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, MAIL_SUBJECT, message);
    }


}
