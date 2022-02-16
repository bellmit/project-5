package com.kindminds.drs.core.schedule;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.data.access.rdb.accounting.ProfitCostShareStatementsDao;
import com.kindminds.drs.api.usecase.accounting.ViewSs2spStatementUco;

import com.kindminds.drs.api.v1.model.report.ProfitShareInvoiceEmailReminder;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport;
import com.kindminds.drs.UserInfo;

import com.kindminds.drs.enums.BillStatementType;


import com.kindminds.drs.service.usecase.interfaces.ViewSs2spStatementInternalUco;
import com.kindminds.drs.service.util.MailUtil;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Locale;

public class ProfitShareEmailReminderHandler extends AbstractActor{

    private BillStatementType type = BillStatementType.OFFICIAL;

    private AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get();

    private ViewSs2spStatementInternalUco viewSs2spStatementUco;

    private ProfitCostShareStatementsDao profitCostShareStatementsDao;

    private MailUtil mailUtil;

    private UserDao userDao;

    private MessageSource messageSource;


    private static final String MAIL_ADDRESS_NOREPLY = "DRS.network <drs-noreply@tw.drs.network>";
    private static final String EMAIL_SOFTWARE_DEVS = "software.engineering@drs.network";
    private static final String EMAIL_ACCOUNTING= "accounting@drs.network";

    public static Props props() {

        return Props.create(ProfitShareEmailReminderHandler.class ,
                () -> new ProfitShareEmailReminderHandler() );
    }

    private ProfitShareEmailReminderHandler() {

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(message -> {
                    profitCostShareStatementsDao = (ProfitCostShareStatementsDao)springCtx.getBean(
                            ProfitCostShareStatementsDao.class);

                    mailUtil = (MailUtil)springCtx.getBean(
                            MailUtil.class);

                    userDao = (UserDao)springCtx.getBean(
                            UserDao.class);

                    messageSource = (MessageSource)springCtx.getBean(
                            MessageSource.class);

                    viewSs2spStatementUco = (ViewSs2spStatementInternalUco)springCtx.getBean(
                            ViewSs2spStatementUco.class);

                    Config config = ConfigFactory.load("application.conf");
                    boolean sendNotify = config.getBoolean("drs.sendNotify");
                    if(sendNotify)
                        processEmailReminders();
                }).build();
    }

    private void processEmailReminders() {
        List<ProfitShareInvoiceEmailReminder> profitShareReminderList = profitCostShareStatementsDao.querySuppliersWithNoInvoiceCreated(type);

        sendSupplierProfitShareReminder(profitShareReminderList);
    }

    private void sendSupplierProfitShareReminder(List<ProfitShareInvoiceEmailReminder> profitShareReminderList) {

        for(ProfitShareInvoiceEmailReminder psr : profitShareReminderList) {
            try{
                UserInfo userInfo = userDao.findUserByUserID(psr.getUserName());
                Locale userLocale = userInfo.getLocale();
                String subject = this.messageSource.getMessage("profitShareEmailReminder.subject", null, userLocale);

                String body = createEmailBody(psr, userLocale);
                String [] mailTo = {userDao.queryUserMail(userInfo.getUserId())};
                String [] bcc = {EMAIL_ACCOUNTING};
                this.mailUtil.SendMimeWithBcc(mailTo, bcc, MAIL_ADDRESS_NOREPLY, subject, body);
            }
            catch(Exception e){
                this.mailUtil.Send(EMAIL_SOFTWARE_DEVS, MAIL_ADDRESS_NOREPLY, "Create message from Profit Share Invoice Reminder ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
            }
        }
    }

    private String createEmailBody(ProfitShareInvoiceEmailReminder psr, Locale userLocale) {
        StringBuilder body = new StringBuilder();
        body.append(this.messageSource.getMessage("profitShareEmailReminder.intro", null, userLocale))
                .append("<table width='75%' border='1' style='border-collapse:collapse;' align='center'>")
                .append("<tr><th>").append(this.messageSource.getMessage("profitShareEmailReminder.settlementPeriod", null, userLocale)).append("</th>")
                .append("<th>").append(this.messageSource.getMessage("profitShareEmailReminder.statementId", null, userLocale)).append("</th>")
                .append("<th>").append(this.messageSource.getMessage("profitShareEmailReminder.subtotal", null, userLocale)).append("</th>")
                .append("<th>").append(this.messageSource.getMessage("profitShareEmailReminder.tax", null, userLocale)).append("</th>")
                .append("<th>").append(this.messageSource.getMessage("profitShareEmailReminder.total", null, userLocale)).append("</th></tr>");
        Ss2spProfitShareReport ss2sp = viewSs2spStatementUco.getSs2spProfitShareReportInternal(type, psr.getStatementName());
        String settlementPeriod = psr.getPeriodStartUtc() + " - " + psr.getPeriodEndUtc();
        body.append(createTableRow(settlementPeriod, psr.getStatementName(), ss2sp.getAmountSubTotal(),ss2sp.getAmountTax(), ss2sp.getAmountTotal()));

        body.append("</table><br><br>");

        body.append("https://access.drs.network/statements/" + psr.getStatementName()).append("<br><br>");
        String signature = this.mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY);
        body.append(signature);
        return body.toString();
    }

    private StringBuilder createTableRow(String settlementPeriod, String settlementId, String subtotal, String tax, String total) {
        String trStartHTML = "<tr align='center'><td>";
        String trEndHTML = "</td></tr>";
        String tdHTML = "</td><td>";
        StringBuilder row = new StringBuilder();
        return row.append(trStartHTML)
                .append(settlementPeriod).append(tdHTML)
                .append(settlementId).append(tdHTML)
                .append(subtotal).append(tdHTML)
                .append(tax).append(tdHTML)
                .append(total).append(trEndHTML);
    }
}
